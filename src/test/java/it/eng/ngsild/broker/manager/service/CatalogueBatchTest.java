package it.eng.ngsild.broker.manager.service;

import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CatalogueBatchTest {
  @Test void repeatedDeletionAcceptsOnlyAcknowledgedMissingEntitiesAndUpsertStillFails() throws Exception {
    HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
    AtomicInteger calls=new AtomicInteger();
    server.createContext("/",exchange->{
      exchange.getRequestBody().readAllBytes();
      boolean first=calls.incrementAndGet()==1;
      org.json.JSONArray success=new org.json.JSONArray();
      if(first)success.put("urn:test:1");
      org.json.JSONArray errors=new org.json.JSONArray();
      for(String id:first?Arrays.asList("urn:test:2"):Arrays.asList("urn:test:1","urn:test:2"))
        errors.put(new org.json.JSONObject().put("entityId",id).put("error",new org.json.JSONObject()
            .put("status",404).put("type","https://uri.etsi.org/ngsi-ld/errors/ResourceNotFound")));
      byte[] body=new org.json.JSONObject().put("success",success).put("errors",errors).toString().getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type","application/json");
      exchange.sendResponseHeaders(207,body.length);exchange.getResponseBody().write(body);exchange.close();
    });server.start();
    try {
      CatalogueService service=new CatalogueService();
      String base="http://127.0.0.1:"+server.getAddress().getPort();
      List<String> ids=Arrays.asList("\"urn:test:1\"","\"urn:test:2\"");
      assertEquals(204,service.postRequestWithCheck(ids,base+"/entityOperations/delete",200));
      assertEquals(204,service.postRequestWithCheck(ids,base+"/entityOperations/delete",200));
      assertEquals(502,service.postRequestWithCheck(ids,base+"/entityOperations/upsert",200));
    } finally {server.stop(0);}
  }
  @Test void missingDeleteAcknowledgementsAndOtherErrorsRemainFailures() {
    String request="[\"urn:test:1\",\"urn:test:2\"]";
    String missing="{\"entityId\":\"urn:test:2\",\"error\":{\"status\":404,\"type\":\"https://uri.etsi.org/ngsi-ld/errors/ResourceNotFound\"}}";
    String valid="{\"success\":[\"urn:test:1\"],\"errors\":["+missing+"]}";
    assertTrue(CatalogueService.completeDeleteSuccess(valid,request));
    for(String body:Arrays.asList("", "{}", "{\"success\":[\"urn:test:1\"],\"errors\":[]}",
        valid.replace("404","403"),valid.replace("ResourceNotFound","InternalError"),
        valid.replace("urn:test:2","urn:test:unknown"),valid.replace("urn:test:2","urn:test:1")))
      assertFalse(CatalogueService.completeDeleteSuccess(body,request),body);
  }
  @Test void largeMultibyteEntitiesStayBelowBrokerPayloadLimit() throws Exception {
    HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
    List<String> received=Collections.synchronizedList(new ArrayList<>());
    server.createContext("/batch",exchange->{
      byte[] body=exchange.getRequestBody().readAllBytes();
      received.add(new String(body,StandardCharsets.UTF_8));
      exchange.sendResponseHeaders(body.length>1048576?413:204,-1);exchange.close();
    });server.start();
    try {
      List<String> entities=new ArrayList<>();
      for(int i=0;i<3;i++) entities.add("{\"id\":\"urn:test:"+i+"\",\"description\":\""+"é".repeat(200000)+"\"}");
      assertEquals(204,new CatalogueService().postRequestWithCheck(entities,
          "http://127.0.0.1:"+server.getAddress().getPort()+"/batch",200));
      assertEquals(2,received.size());
      List<String> actual=new ArrayList<>();
      for(String batch:received){
        assertTrue(batch.getBytes(StandardCharsets.UTF_8).length<=900000);
        org.json.JSONArray array=new org.json.JSONArray(batch);
        for(int i=0;i<array.length();i++) actual.add(array.getJSONObject(i).getString("id"));
      }
      assertEquals(Arrays.asList("urn:test:0","urn:test:1","urn:test:2"),actual);
    } finally {server.stop(0);}
  }
  @Test void partialBatchCannotBeHiddenByLaterSuccess() throws Exception {
    HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
    AtomicInteger calls=new AtomicInteger();
    server.createContext("/batch",exchange->{
      exchange.getRequestBody().readAllBytes();
      int status=calls.incrementAndGet()==1?207:204;
      exchange.sendResponseHeaders(status,-1);exchange.close();
    });server.start();
    try {
      CatalogueService service=new CatalogueService();
      assertEquals(502,service.postRequestWithCheck(Arrays.asList("{}","{}","{}"),"http://127.0.0.1:"+server.getAddress().getPort()+"/batch",2));
      assertEquals(2,calls.get());
    } finally {server.stop(0);}
  }
  @Test void emptyAndInvalidBatchSizesHaveDefinedBehavior() throws Exception {
    CatalogueService service=new CatalogueService();
    assertEquals(204,service.postRequestWithCheck(Collections.emptyList(),"unused",200));
    assertThrows(IllegalArgumentException.class,()->service.postRequestWithCheck(Collections.emptyList(),"unused",0));
  }
  @Test void completeMixedBatchIsSuccessful() throws Exception {
    HttpServer server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
    server.createContext("/batch",exchange->{
      exchange.getRequestBody().readAllBytes();
      byte[] body="{\"success\":[\"urn:test:1\",\"urn:test:2\"],\"errors\":[]}".getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type","application/json");
      exchange.sendResponseHeaders(207,body.length);exchange.getResponseBody().write(body);exchange.close();
    });server.start();
    try {
      assertEquals(204,new CatalogueService().postRequestWithCheck(
          Arrays.asList("{\"id\":\"urn:test:1\"}","{\"id\":\"urn:test:2\"}"),
          "http://127.0.0.1:"+server.getAddress().getPort()+"/batch",200));
    } finally {server.stop(0);}
  }
  @Test void partialMalformedOrUnacknowledgedResponsesRemainFailures() {
    String request="[{\"id\":\"urn:test:1\"},{\"id\":\"urn:test:2\"}]";
    for(String response:Arrays.asList("", "{}", "{\"success\":[\"urn:test:1\"],\"errors\":[]}",
        "{\"success\":[\"urn:test:1\",\"urn:test:2\"],\"errors\":[{\"entityId\":\"urn:test:2\"}]}",
        "{\"success\":[\"urn:test:1\",\"urn:test:unknown\"],\"errors\":[]}")) {
      assertFalse(CatalogueService.completeBatchSuccess(response,request),response);
    }
  }
}
