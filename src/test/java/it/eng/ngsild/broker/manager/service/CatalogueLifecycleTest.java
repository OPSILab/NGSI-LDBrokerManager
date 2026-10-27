package it.eng.ngsild.broker.manager.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import it.eng.idra.beans.dcat.DcatDataset;
import it.eng.ngsild.broker.manager.model.Configurations;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogueLifecycleTest {
  @Test void reconciliationsToSameBrokerDoNotOverlapAcrossServiceInstances() throws Exception {
    try(Fixture fixture=new Fixture()) {
      fixture.datasets.put("900001",datasets("900001",1,"one"));fixture.datasets.put("900002",datasets("900002",1,"two"));
      fixture.holdWrites=true;
      CatalogueService first=fixture.service(),second=fixture.service();
      java.util.concurrent.ExecutorService pool=java.util.concurrent.Executors.newFixedThreadPool(2);
      try {
        java.util.concurrent.Future<Integer> a=pool.submit(()->first.start(fixture.config("900001")));
        assertTrue(fixture.firstWrite.await(5,java.util.concurrent.TimeUnit.SECONDS));
        java.util.concurrent.CountDownLatch secondStarted=new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.Future<Integer> b=pool.submit(()->{secondStarted.countDown();return second.start(fixture.config("900002"));});
        assertTrue(secondStarted.await(5,java.util.concurrent.TimeUnit.SECONDS));
        assertFalse(fixture.overlappingWrite.await(300,java.util.concurrent.TimeUnit.MILLISECONDS));
        fixture.releaseWrites.countDown();
        assertEquals(204,a.get(10,java.util.concurrent.TimeUnit.SECONDS));assertEquals(204,b.get(10,java.util.concurrent.TimeUnit.SECONDS));
        assertEquals(1,fixture.maxWriters.get());assertEquals(3,fixture.owned("900001").size());assertEquals(3,fixture.owned("900002").size());
        fixture.holdWrites=false;
        java.util.concurrent.Future<Integer> remove=pool.submit(()->first.delete(fixture.config("900001")));
        java.util.concurrent.Future<Integer> publish=pool.submit(()->second.start(fixture.config("900002")));
        assertEquals(204,remove.get(10,java.util.concurrent.TimeUnit.SECONDS));assertEquals(204,publish.get(10,java.util.concurrent.TimeUnit.SECONDS));
        assertTrue(fixture.owned("900001").isEmpty());assertEquals(3,fixture.owned("900002").size());assertNull(fixture.failure.get());
      } finally {fixture.releaseWrites.countDown();pool.shutdownNow();}
    }
  }
  @Test void independentBrokersCanReconcileWhileAnotherBrokerIsBusy() throws Exception {
    try(Fixture first=new Fixture();Fixture second=new Fixture()) {
      first.holdWrites=true;second.holdWrites=true;
      java.util.concurrent.ExecutorService pool=java.util.concurrent.Executors.newFixedThreadPool(2);
      try {
        CatalogueService a=first.service(),b=second.service();
        java.util.concurrent.Future<Integer> one=pool.submit(()->a.start(first.config("900001")));
        java.util.concurrent.Future<Integer> two=pool.submit(()->b.start(second.config("900002")));
        assertTrue(first.firstWrite.await(5,java.util.concurrent.TimeUnit.SECONDS));assertTrue(second.firstWrite.await(5,java.util.concurrent.TimeUnit.SECONDS));
        first.releaseWrites.countDown();second.releaseWrites.countDown();
        assertEquals(204,one.get(10,java.util.concurrent.TimeUnit.SECONDS));assertEquals(204,two.get(10,java.util.concurrent.TimeUnit.SECONDS));
        assertNull(first.failure.get());assertNull(second.failure.get());
      } finally {first.releaseWrites.countDown();second.releaseWrites.countDown();pool.shutdownNow();}
    }
  }
  @Test void linkedAgentsServicesSeriesAndRecordsAreRemovedOnlyForTheirOwner() throws Exception {
    try (Fixture fixture = new Fixture()) {
      for (String owner : new String[]{"900001","900002"}) {
        JSONObject dataset = new JSONObject().put("id", owner+"-dataset").put("nodeID", owner)
            .put("publisher",new JSONObject().put("resourceUri","https://example.org/shared-publisher"))
            .put("inSeries",new JSONArray().put(new JSONObject().put("resourceUri","https://example.org/shared-series")))
            .put("distributions",new JSONArray().put(new JSONObject().put("id",owner+"-distribution")
                .put("accessService",new JSONArray().put(new JSONObject().put("resourceUri","https://example.org/shared-service")))));
        fixture.datasets.put(owner,Collections.singletonList(new Gson().fromJson(dataset.toString(),DcatDataset.class)));
        fixture.catalogues.put(owner,new JSONObject().put("id",Integer.parseInt(owner)).put("name","Rich fixture").put("isActive",true)
            .put("record",new JSONArray().put(new JSONObject().put("resourceUri","https://example.org/shared-record"))));
      }
      CatalogueService service=fixture.service();
      assertEquals(204,service.start(fixture.config("900001")));
      assertEquals(204,service.start(fixture.config("900002")));
      Set<String> types=new HashSet<>();
      for(String id:fixture.owned("900001"))types.add(fixture.entities.get(id).getString("type"));
      assertTrue(types.containsAll(Arrays.asList("AgentDCAT-AP","DatasetSeries","DataServiceDCAT-AP","CatalogueRecordDCAT-AP")));
      Set<String> other=fixture.owned("900002");
      fixture.datasets.put("900001",Collections.emptyList());
      fixture.catalogues.get("900001").remove("record");
      assertEquals(204,service.delete(fixture.config("900001")));
      assertTrue(fixture.owned("900001").isEmpty());
      assertEquals(other,fixture.owned("900002"));
      assertNull(fixture.failure.get());
    }
  }
  @Test void changedChildIdsAndRemovedDatasetsArePrunedAndDeletionDoesNotDependOnCurrentSql() throws Exception {
    try (Fixture fixture = new Fixture()) {
      fixture.datasets.put("900001", datasets("900001", 1001, "old"));
      fixture.datasets.put("900002", datasets("900002", 1, "other"));
      CatalogueService service = fixture.service();
      assertEquals(204, service.start(fixture.config("900001")));
      assertEquals(204, service.start(fixture.config("900002")));
      assertEquals(2003, fixture.owned("900001").size());
      Set<String> other = fixture.owned("900002");
      fixture.entities.put("urn:external:unowned", new JSONObject().put("id", "urn:external:unowned").put("type", "Dataset"));

      // Full replacement changes child UUIDs and removes a dataset in SQL.
      fixture.datasets.put("900001", datasets("900001", 1000, "new"));
      assertEquals(204, service.start(fixture.config("900001")));
      Set<String> after = fixture.owned("900001");
      assertEquals(2001, after.size());
      assertTrue(after.stream().noneMatch(id -> id.contains("-old-")));
      assertFalse(after.contains("urn:ngsi-ld:Dataset:id:900001-dataset-1000"));
      assertEquals(other, fixture.owned("900002"));
      assertEquals(204, service.start(fixture.config("900001")));
      assertEquals(after, fixture.owned("900001"));

      // All SQL rows may already be gone: ownership must still remove the old graph.
      fixture.datasets.put("900001", Collections.emptyList());
      assertEquals(204, service.delete(fixture.config("900001")));
      assertTrue(fixture.owned("900001").isEmpty());
      assertEquals(204, service.delete(fixture.config("900001")));
      assertEquals(other, fixture.owned("900002"));
      assertTrue(fixture.entities.containsKey("urn:external:unowned"));
      assertNull(fixture.failure.get());
    }
  }

  @Test void failedOrPartialUpsertNeverTriggersStaleEntityDeletion() throws Exception {
    try (Fixture fixture = new Fixture()) {
      fixture.datasets.put("900001", datasets("900001", 1, "old"));
      CatalogueService service = fixture.service();
      assertEquals(204, service.start(fixture.config("900001")));
      Set<String> before = fixture.owned("900001");
      fixture.datasets.put("900001", datasets("900001", 1, "new"));
      for (int response : new int[]{500, 207}) {
        fixture.upsertStatus = response;
        assertEquals(response == 207 ? 502 : 500, service.start(fixture.config("900001")));
        assertEquals(before, fixture.owned("900001"));
        assertEquals(0, fixture.deleteCalls);
      }
      fixture.upsertStatus = 204;
      assertEquals(204, service.start(fixture.config("900001")));
      assertFalse(fixture.entities.containsKey("urn:ngsi-ld:DistributionDCAT-AP:id:900001-old-0"));
      assertNull(fixture.failure.get());
    }
  }

  @Test void deletionUsesOwnershipWhenInactiveCatalogueIsHiddenAndDoesNotHideServerErrors() throws Exception {
    try (Fixture fixture = new Fixture()) {
      fixture.datasets.put("900001", datasets("900001", 1, "kept"));
      fixture.datasets.put("900002", datasets("900002", 1, "other"));
      CatalogueService service = fixture.service();
      assertEquals(204, service.start(fixture.config("900001")));
      assertEquals(204, service.start(fixture.config("900002")));
      Set<String> other = fixture.owned("900002");
      fixture.catalogueStatus = 500;
      assertThrows(org.springframework.web.client.HttpServerErrorException.class,
          () -> service.delete(fixture.config("900001")));
      assertEquals(3, fixture.owned("900001").size());
      assertEquals(0, fixture.deleteCalls);
      fixture.catalogueStatus = 404;
      assertEquals(204, service.delete(fixture.config("900001")));
      assertTrue(fixture.owned("900001").isEmpty());
      assertEquals(other, fixture.owned("900002"));
      assertEquals(204, service.delete(fixture.config("900001")));
      assertNull(fixture.failure.get());
    }
  }

  @Test void deletionDoesNotTrustAShortInventoryPage() throws Exception {
    // S034 (87509 entities): a short page from a loaded broker ended the inventory and 1611 entities stayed.
    try (Fixture fixture = new Fixture()) {
      fixture.datasets.put("900001", datasets("900001", 1500, "big"));
      fixture.datasets.put("900002", datasets("900002", 1, "other"));
      CatalogueService service = fixture.service();
      assertEquals(204, service.start(fixture.config("900001")));
      assertEquals(204, service.start(fixture.config("900002")));
      Set<String> other = fixture.owned("900002");
      assertEquals(3001, fixture.owned("900001").size());
      fixture.shortPagesLeft = 1;
      fixture.datasets.put("900001", Collections.emptyList());
      assertEquals(204, service.delete(fixture.config("900001")));
      assertEquals(0, fixture.shortPagesLeft, "the short page was served");
      assertTrue(fixture.owned("900001").isEmpty());
      assertEquals(other, fixture.owned("900002"));
      assertTrue(fixture.inventoryRequests.stream().allMatch(q -> "idraCatalogueId".equals(q)), "inventory reads only the ownership attribute");
      assertNull(fixture.failure.get());
    }
  }
  @Test void deletionReportsFailureWhenOwnedEntitiesSurvive() throws Exception {
    try (Fixture fixture = new Fixture()) {
      fixture.datasets.put("900001", datasets("900001", 2, "kept"));
      CatalogueService service = fixture.service();
      assertEquals(204, service.start(fixture.config("900001")));
      fixture.ignoreDeletes = true;
      assertEquals(502, service.delete(fixture.config("900001")));
      assertEquals(5, fixture.owned("900001").size());
      assertNull(fixture.failure.get());
    }
  }

  private static List<DcatDataset> datasets(String owner, int count, String version) {
    List<DcatDataset> result = new ArrayList<>();
    Gson gson = new Gson();
    for (int i=0; i<count; i++) {
      result.add(gson.fromJson(new JSONObject().put("id", owner+"-dataset-"+i).put("nodeID", owner)
          .put("distributions", new JSONArray().put(new JSONObject().put("id", owner+"-"+version+"-"+i))).toString(), DcatDataset.class));
    }
    return result;
  }

  private static class Fixture implements AutoCloseable {
    final HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    final Map<String,List<DcatDataset>> datasets = new HashMap<>();
    final Map<String,JSONObject> catalogues = new HashMap<>();
    final Map<String,JSONObject> entities = new ConcurrentHashMap<>();
    final AtomicReference<Throwable> failure = new AtomicReference<>();
    final java.util.concurrent.ExecutorService executor=java.util.concurrent.Executors.newCachedThreadPool();
    final java.util.concurrent.CountDownLatch firstWrite=new java.util.concurrent.CountDownLatch(1),overlappingWrite=new java.util.concurrent.CountDownLatch(1),releaseWrites=new java.util.concurrent.CountDownLatch(1);
    final java.util.concurrent.atomic.AtomicInteger writers=new java.util.concurrent.atomic.AtomicInteger(),maxWriters=new java.util.concurrent.atomic.AtomicInteger();
    volatile boolean holdWrites;
    int upsertStatus = 204;
    volatile int shortPagesLeft;
    volatile boolean ignoreDeletes;
    final List<String> inventoryRequests = Collections.synchronizedList(new ArrayList<>());
    int catalogueStatus = 200;
    int deleteCalls;
    final String base;
    Fixture() throws Exception {
      base = "http://127.0.0.1:"+server.getAddress().getPort();
      server.createContext("/", exchange -> {
        boolean write=exchange.getRequestURI().getPath().contains("/entityOperations/");
        try {
          if(write) {
            int active=writers.incrementAndGet();maxWriters.accumulateAndGet(active,Math::max);firstWrite.countDown();
            if(active>1)overlappingWrite.countDown();
            if(holdWrites && !releaseWrites.await(10,java.util.concurrent.TimeUnit.SECONDS))throw new IllegalStateException("Test write not released");
          }
          handle(exchange);
        }
        catch (Throwable error) { failure.set(error); error.printStackTrace(); respond(exchange, 500, "fixture failed"); }
        finally {if(write)writers.decrementAndGet();}
      });
      server.setExecutor(executor);
      server.start();
    }
    CatalogueService service() throws Exception {
      CatalogueService service = new CatalogueService();
      for (String[] setting : new String[][]{{"idraBasePath",base},{"dcatContextUrl",base+"/context"},{"contextBrokerUrlAllowlist","127.0.0.1"}}) {
        Field field = CatalogueService.class.getDeclaredField(setting[0]); field.setAccessible(true); field.set(service,setting[1]);
      }
      return service;
    }
    Configurations config(String owner) { return new Configurations(owner, base); }
    Set<String> owned(String owner) {
      Set<String> ids = new HashSet<>();
      entities.forEach((id, entity) -> {
        JSONObject attribute = entity.optJSONObject("idraCatalogueId");
        if (attribute != null && owner.equals(attribute.optString("value"))) ids.add(id);
      });
      return ids;
    }
    void handle(HttpExchange exchange) throws Exception {
      String path = exchange.getRequestURI().getPath().replace("//", "/");
      Map<String,String> query = new HashMap<>();
      if (exchange.getRequestURI().getRawQuery()!=null)
        for (String entry:exchange.getRequestURI().getRawQuery().split("&")) {
          String[] pair=entry.split("=",2); query.put(pair[0],URLDecoder.decode(pair[1],StandardCharsets.UTF_8));
        }
      if (path.startsWith("/Idra/api/v1/client/catalogues/")) {
        if (catalogueStatus != 200) { respond(exchange, catalogueStatus, "catalogue not visible"); return; }
        String owner = path.split("/")[6];
        if (path.endsWith("/datasets")) {
          List<DcatDataset> all = datasets.getOrDefault(owner,Collections.emptyList());
          int start = Integer.parseInt(query.getOrDefault("start","0"));
          int end = Math.min(start+Integer.parseInt(query.getOrDefault("rows","1000")),all.size());
          respond(exchange,200,new JSONObject().put("count",all.size()).put("results",new JSONArray(new Gson().toJson(all.subList(Math.min(start,all.size()),end)))).toString());
        } else {
          respond(exchange,200,catalogues.getOrDefault(owner,new JSONObject().put("id",Integer.parseInt(owner)).put("name","Lifecycle fixture").put("isActive",true)).toString());
        }
      } else if (path.endsWith("/entities")) {
        assertNotNull(exchange.getRequestHeaders().getFirst("Link"));
        String owner=query.get("q").split("\"")[1];
        List<String> ids=new ArrayList<>(owned(owner));Collections.sort(ids);
        inventoryRequests.add(query.get("attrs"));
        int start=Integer.parseInt(query.get("offset")),end=Math.min(start+1000,ids.size());
        // Simulates a loaded broker answering one full page with fewer entities than exist.
        if(start==1000 && end-start==1000 && shortPagesLeft>0){shortPagesLeft--;end=start+389;}
        JSONArray page=new JSONArray();for(int i=start;i<end;i++)page.put(entities.get(ids.get(i)));
        respond(exchange,200,page.toString());
      } else if (path.endsWith("/entityOperations/upsert")) {
        JSONArray payload=new JSONArray(new String(exchange.getRequestBody().readAllBytes(),StandardCharsets.UTF_8));
        if(upsertStatus==207) {
          respond(exchange,207,new JSONObject().put("success",new JSONArray()).put("errors",new JSONArray().put(new JSONObject().put("entityId",payload.getJSONObject(0).getString("id")))).toString());
        } else if(upsertStatus!=204) respond(exchange,upsertStatus,"failure");
        else {
          for(Object item:payload) {JSONObject entity=(JSONObject)item;assertNotNull(entity.getJSONObject("idraCatalogueId"));entities.put(entity.getString("id"),entity);}
          respond(exchange,204,"");
        }
      } else if (path.endsWith("/entityOperations/delete")) {
        deleteCalls++;
        JSONArray payload=new JSONArray(new String(exchange.getRequestBody().readAllBytes(),StandardCharsets.UTF_8));
        JSONArray success=new JSONArray(),errors=new JSONArray();
        for(Object item:payload) {
          String id=(String)item;
          if(ignoreDeletes ? entities.containsKey(id) : entities.remove(id)!=null)success.put(id);
          else errors.put(new JSONObject().put("entityId",id).put("error",new JSONObject().put("status",404).put("type","https://uri.etsi.org/ngsi-ld/errors/ResourceNotFound")));
        }
        if(errors.length()>0)respond(exchange,207,new JSONObject().put("success",success).put("errors",errors).toString());
        else respond(exchange,204,"");
      } else respond(exchange,404,"");
    }
    void respond(HttpExchange exchange,int status,String body) throws java.io.IOException {
      byte[] bytes=body.getBytes(StandardCharsets.UTF_8);exchange.getResponseHeaders().set("Content-Type","application/json");
      exchange.sendResponseHeaders(status,bytes.length==0?-1:bytes.length);
      if(bytes.length>0)exchange.getResponseBody().write(bytes);exchange.close();
    }
    public void close() { releaseWrites.countDown();server.stop(0);executor.shutdownNow(); }
  }
}
