package it.eng.ngsild.broker.manager.service;

import com.google.gson.reflect.TypeToken;
import it.eng.idra.beans.dcat.DcatDataService;
import it.eng.idra.beans.dcat.DcatDataset;
import it.eng.idra.beans.dcat.DcatDatasetSeries;
import it.eng.idra.beans.dcat.DcatDetails;
import it.eng.idra.beans.dcat.DcatDistribution;
import it.eng.idra.beans.dcat.DcatKeyword;
import it.eng.idra.beans.dcat.DcatProperty;
import it.eng.idra.beans.dcat.DctLocation;
import it.eng.idra.beans.dcat.DctPeriodOfTime;
import it.eng.idra.beans.dcat.DctStandard;
import it.eng.idra.beans.dcat.FoafAgent;
import it.eng.idra.beans.dcat.Relationship;
import it.eng.idra.beans.dcat.SkosConceptSubject;
import it.eng.idra.beans.dcat.SkosConceptTheme;
import it.eng.idra.beans.dcat.SkosPrefLabel;
import it.eng.idra.beans.dcat.VcardOrganization;
import it.eng.idra.beans.odms.OdmsCatalogue;
import it.eng.idra.utils.GsonUtil;
import it.eng.idra.utils.restclient.RestClient;
import it.eng.idra.utils.restclient.RestClientImpl;
import it.eng.ngsild.broker.manager.model.Configurations;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.TimeZone;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class CatalogueService  {
	
    /** The logger. */
	private static Logger logger = LogManager.getLogger(CatalogueService.class);
	private static final Type DCAT_DATASET_LIST_TYPE = new TypeToken<List<DcatDataset>>() {
	}.getType();
  private static final int DATASET_PAGE_SIZE = 1000;
  // Keep requests below Orion's default 1 MiB limit, counting UTF-8 bytes.
  private static final int MAX_BATCH_BYTES = 900000;

  private static final int PRUNE_ROUNDS = 3;
  // A reconciliation reads ownership, writes batches, then prunes stale IDs.
  // Keep that sequence exclusive for the same configured broker in this JVM.
  private static final java.util.concurrent.ConcurrentMap<String,java.util.concurrent.locks.ReentrantLock> BROKER_LOCKS = new java.util.concurrent.ConcurrentHashMap<>();

	public CatalogueService() {
	}

	
	@Value("${idra.basepath}")
	private String idraBasePath;
  @Value("${dcat.context.url:http://ngsi-broker:8082/dcat3-context.jsonld}")
  private String dcatContextUrl;

	// Comma-separated hostnames trusted as Context Broker even when they resolve
	// to a private/internal address (e.g. the Orion broker on the docker network).
	// SSRF protection in validateContextBrokerUrl still applies to every other host.
	@Value("${contexBroker.url.allowlist:}")
	private String contextBrokerUrlAllowlist;

	public int start(Configurations config) throws Exception {
		return addCatalogueInCb(config);
	}
	
	public int delete(Configurations config) throws Exception {
		return deleteCatalogueFromCb(config);
	}

	
	public int addCatalogueInCb(Configurations config) throws Exception {
    String api = buildContextBrokerApiBase(config.getContextBrokerUrl());
    java.util.concurrent.locks.ReentrantLock lock=BROKER_LOCKS.computeIfAbsent(api,key->new java.util.concurrent.locks.ReentrantLock(true));
    lock.lockInterruptibly();
    try {
    RestTemplate client = new RestTemplate();
    OdmsCatalogue node = client.getForObject(idraBasePath + "/Idra/api/v1/client/catalogues/" + config.getCatalogueId() + "?withImage=false", OdmsCatalogue.class);
    List<JSONObject> entities = NgsiDcatMapper.catalogue(node, getAllCatalogueDatasets(client, config.getCatalogueId()));
    Set<String> previous = ownedIds(api, config.getCatalogueId());
    List<String> payload = entities.stream().map(JSONObject::toString).collect(Collectors.toList());
    int status = postRequestWithCheck(payload, api + "entityOperations/upsert?options=replace", 200);
    if (status == 207 || status < 200 || status >= 300) return status;
    int pruned = pruneOwned(api, config.getCatalogueId(), previous,
        entities.stream().map(e -> e.getString("id")).collect(Collectors.toSet()));
    return pruned == 204 ? status : pruned;
    } finally { lock.unlock(); }
  }
	

	  /**
	   * Delete a Catalogue from the CB.
	  // * @throws Exception 
	  // * @throws MalformedURLException 
	   */
	  public int deleteCatalogueFromCb(Configurations config) throws MalformedURLException, Exception {
    String api = buildContextBrokerApiBase(config.getContextBrokerUrl());
    java.util.concurrent.locks.ReentrantLock lock=BROKER_LOCKS.computeIfAbsent(api,key->new java.util.concurrent.locks.ReentrantLock(true));
    lock.lockInterruptibly();
    try {
    RestTemplate client = new RestTemplate();
    Set<String> owned = ownedIds(api, config.getCatalogueId());
    try {
      OdmsCatalogue node = client.getForObject(idraBasePath + "/Idra/api/v1/client/catalogues/" + config.getCatalogueId() + "?withImage=false", OdmsCatalogue.class);
      List<JSONObject> entities = NgsiDcatMapper.catalogue(node, getAllCatalogueDatasets(client, config.getCatalogueId()));
      owned.addAll(entities.stream().map(e -> e.getString("id")).collect(Collectors.toSet()));
    } catch (HttpClientErrorException.NotFound inactiveOrRemoved) {
      // ClientApi intentionally hides inactive catalogues. Ownership is the persistent
      // deletion inventory and must remain usable after KEEP or local data removal.
      logger.info("Catalogue {} is not visible in ClientApi; deleting its owned entities", config.getCatalogueId());
    }
    return pruneOwned(api, config.getCatalogueId(), owned, Collections.emptySet());
    } finally { lock.unlock(); }
  }
	  

	  /**
	   * Post Request on CB with Check on the number of Entities.
	// * @throws Exception exception.
	   * 
	   */
	  public int postRequestWithCheck(List<String> listId, String api, int maxNumberOfEntities) throws Exception {
    if (maxNumberOfEntities <= 0) throw new IllegalArgumentException("Batch size must be positive");
    int result = 204;
    for (int offset = 0; offset < listId.size();) {
      int end = offset;
      long bytes = 2; // JSON array brackets
      while (end < listId.size() && end - offset < maxNumberOfEntities) {
        long next = listId.get(end).getBytes(java.nio.charset.StandardCharsets.UTF_8).length
            + (end == offset ? 0 : 1);
        if (end > offset && bytes + next > MAX_BATCH_BYTES) break;
        bytes += next;
        end++;
      }
      String data = "[" + String.join(",", listId.subList(offset, end)) + "]";
      int status = restRequest(api, data, "POST");
      // Multi-status must not be overwritten by a later successful batch.
      if (status == 207) result = 207;
      else if (status < 200 || status >= 300) return status;
      offset = end;
    }
    return result == 207 ? 502 : result;
  }
	  
  /**
   * Deletes the catalogue's owned entities that are not in keep, then re-reads ownership and
   * deletes any straggler: offset pages are not a snapshot and a loaded broker can return
   * short pages, so the first inventory alone left 1611 of 87509 entities behind (plan62, S034).
   * Returns 502 if owned entities outside keep survive every round.
   */
  private int pruneOwned(String api, String catalogueId, Set<String> candidates, Set<String> keep)
      throws Exception {
    Set<String> pending = new HashSet<>(candidates);
    pending.removeAll(keep);
    for (int round = 1; round <= PRUNE_ROUNDS; round++) {
      if (!pending.isEmpty()) {
        int status = postRequestWithCheck(pending.stream().map(JSONObject::quote)
            .collect(Collectors.toList()), api + "entityOperations/delete", 200);
        if (status < 200 || status >= 300) return status;
      }
      pending = ownedIds(api, catalogueId);
      pending.removeAll(keep);
      if (pending.isEmpty()) return 204;
      logger.warn("{} entities still owned by catalogue {} after deletion round {}",
          pending.size(), catalogueId, round);
    }
    logger.error("Catalogue {} still owns {} stale entities after {} deletion rounds",
        catalogueId, pending.size(), PRUNE_ROUNDS);
    return 502;
  }

	  private Set<String> ownedIds(String api, String catalogueId) throws Exception {
    Map<String,String> headers = new HashMap<>();
    headers.put("Accept", "application/json");
    headers.put("Link", "<" + dcatContextUrl + ">; rel=\"http://www.w3.org/ns/json-ld#context\"; type=\"application/ld+json\"");
    RestClient client = new RestClientImpl();
    Set<String> ids = new HashSet<>();
    String query = java.net.URLEncoder.encode("idraCatalogueId==\"" + catalogueId + "\"", java.nio.charset.StandardCharsets.UTF_8);
    for (int offset = 0;; offset += 1000) {
      // Only the ownership attribute is needed: full entities made large inventories heavy.
      HttpResponse response = client.sendGetRequest(api + "entities?q=" + query
          + "&attrs=idraCatalogueId&limit=1000&offset=" + offset, headers);
      int status = client.getStatus(response);
      String body = client.getHttpResponseBody(response);
      if (status != 200) throw new IllegalStateException("Cannot enumerate owned NGSI-LD entities: HTTP " + status);
      JSONArray page = new JSONArray(body);
      for (int i=0;i<page.length();i++) ids.add(page.getJSONObject(i).getString("id"));
      // A short page is not proof of the end under load; only an empty page is.
      if (page.length()==0) return ids;
    }
  }

  private String firstValue(List<DcatProperty> properties) {
	    if (properties == null || properties.isEmpty() || properties.get(0) == null
	        || properties.get(0).getValue() == null) {
	      return "";
	    }
	    return properties.get(0).getValue();
	  }

	  // Strips trailing ", " (the convention used by older pre-built JSON fragments
	  // in this file) so the fragment can be safely placed in a List joined with ",".
	  private static String stripTrailingComma(String fragment) {
	    if (fragment == null) return "";
	    String s = fragment.trim();
	    if (s.endsWith(",")) s = s.substring(0, s.length() - 1).trim();
	    return s;
	  }

	  private List<DcatDataset> parseDatasetsFromResponse(String responseBody) throws Exception {
	    if (responseBody == null || responseBody.trim().isEmpty()) {
	      return new ArrayList<DcatDataset>();
	    }
	    JSONObject payload = new JSONObject(responseBody);
	    JSONArray results = payload.optJSONArray("results");
	    if (results == null) {
	      return new ArrayList<DcatDataset>();
	    }
	    return GsonUtil.json2Obj(results.toString(), DCAT_DATASET_LIST_TYPE);
	  }

	  private List<DcatDataset> getAllCatalogueDatasets(RestTemplate restTemplate, String nodeId)
	      throws Exception {
	    List<DcatDataset> datasets = new ArrayList<DcatDataset>();
	    Set<String> seenDatasetIds = new HashSet<String>();
	    int start = 0;

	    while (true) {
	      String url = idraBasePath + "/Idra/api/v1/" + "/client/catalogues/" + nodeId
	          + "/datasets?rows=" + DATASET_PAGE_SIZE + "&start=" + start;
	      ResponseEntity<String> resultDatasets = restTemplate.getForEntity(url, String.class);
	      List<DcatDataset> page = parseDatasetsFromResponse(resultDatasets.getBody());

	      if (page.isEmpty()) {
	        break;
	      }

	      int pageAdded = 0;
	      for (DcatDataset dataset : page) {
	        if (dataset != null && dataset.getId() != null && seenDatasetIds.add(dataset.getId())) {
	          datasets.add(dataset);
	          pageAdded++;
	        }
	      }

	      if (page.size() < DATASET_PAGE_SIZE || pageAdded == 0) {
	        break;
	      }
	      start += DATASET_PAGE_SIZE;
	    }
	    return datasets;
	  }

	  private String buildContextBrokerApiBase(String rawContextBrokerUrl) {
	    validateContextBrokerUrl(rawContextBrokerUrl);
	    String normalizedBaseUrl = normalizeContextBrokerBaseUrl(rawContextBrokerUrl);
	    return normalizedBaseUrl + "/ngsi-ld/v1/";
	  }

	  private void validateContextBrokerUrl(String url) {
	    if (url == null || url.isBlank()) {
	      throw new IllegalArgumentException("Context Broker URL is null or empty");
	    }
	    try {
	      String candidate = url.trim();
	      if (!candidate.startsWith("http://") && !candidate.startsWith("https://")) {
	        candidate = "http://" + candidate;
	      }
	      URI uri = new URI(candidate);
	      String scheme = uri.getScheme();
	      if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
	        throw new IllegalArgumentException("Context Broker URL must use http or https scheme");
	      }
	      String host = uri.getHost();
	      if (host == null || host.isBlank()) {
	        throw new IllegalArgumentException("Context Broker URL has no valid host");
	      }
	      // Trusted internal brokers (e.g. Orion on the docker network) are exempt
	      // from the private-address check; SSRF protection still applies elsewhere.
	      if (isAllowlistedContextBrokerHost(host)) {
	        return;
	      }
	      // Block SSRF to loopback, link-local, private (RFC1918), any-local and multicast.
	      // Resolve every address to reduce DNS multi-record / rebinding bypass.
	      java.net.InetAddress[] addrs = java.net.InetAddress.getAllByName(host);
	      for (java.net.InetAddress addr : addrs) {
	        if (addr.isLoopbackAddress() || addr.isLinkLocalAddress() || addr.isSiteLocalAddress()
	            || addr.isAnyLocalAddress() || addr.isMulticastAddress()) {
	          throw new SecurityException(
	              "Context Broker URL resolves to a non-routable/internal address");
	        }
	      }
	    } catch (java.net.URISyntaxException | java.net.UnknownHostException e) {
	      throw new IllegalArgumentException("Invalid Context Broker URL: " + e.getMessage(), e);
	    }
	  }

	  private boolean isAllowlistedContextBrokerHost(String host) {
	    if (contextBrokerUrlAllowlist == null || contextBrokerUrlAllowlist.isBlank()) {
	      return false;
	    }
	    for (String allowed : contextBrokerUrlAllowlist.split(",")) {
	      if (allowed.trim().equalsIgnoreCase(host)) {
	        return true;
	      }
	    }
	    return false;
	  }

	  private String normalizeContextBrokerBaseUrl(String rawContextBrokerUrl) {
	    if (rawContextBrokerUrl == null || rawContextBrokerUrl.trim().isEmpty()) {
	      throw new IllegalArgumentException("Context broker URL is empty");
	    }

	    String candidate = rawContextBrokerUrl.trim();
	    if (!candidate.startsWith("http://") && !candidate.startsWith("https://")) {
	      candidate = "http://" + candidate;
	    }

	    URI uri = URI.create(candidate);
	    String host = uri.getHost();
	    int port = uri.getPort();
	    String scheme = uri.getScheme() != null ? uri.getScheme() : "http";

	    // Handle malformed host+port without colon, e.g. http://localhost1026/
	    if (host == null) {
	      String noScheme = candidate.replaceFirst("^https?://", "");
	      int slashIdx = noScheme.indexOf('/');
	      String authority = slashIdx >= 0 ? noScheme.substring(0, slashIdx) : noScheme;
	      if (!authority.contains(":")) {
	        int splitIndex = authority.length();
	        while (splitIndex > 0 && Character.isDigit(authority.charAt(splitIndex - 1))) {
	          splitIndex--;
	        }
	        if (splitIndex > 0 && splitIndex < authority.length()) {
	          String fixedHost = authority.substring(0, splitIndex);
	          String fixedPort = authority.substring(splitIndex);
	          try {
	            int parsedPort = Integer.parseInt(fixedPort);
	            if (parsedPort > 0 && parsedPort <= 65535) {
	              host = fixedHost;
	              port = parsedPort;
	            }
	          } catch (NumberFormatException e) {
	            // Keep original candidate; validation below will fail with a clear message.
	          }
	        }
	      }
	    }

	    if (host == null || host.trim().isEmpty()) {
	      throw new IllegalArgumentException(
	          "Invalid context broker URL: " + rawContextBrokerUrl);
	    }

	    StringBuilder normalized = new StringBuilder();
	    normalized.append(scheme).append("://").append(host);
	    if (port > 0) {
	      normalized.append(":").append(port);
	    }
	    return normalized.toString();
	  }
	  
	
	  
		private int restRequest(String api, String data, String requestType) throws Exception {
		    Map<String, String> headers = new HashMap<String, String>();
		    headers.put("Content-Type", "application/ld+json");
		    RestClient client = new RestClientImpl();
		    HttpResponse response = null;
		    if (requestType.equals("POST")) {
		      response = client.sendPostRequest(api, data,
		          MediaType.valueOf("application/ld+json"), headers);
		    }
		    else if (requestType.equals("PUT")) {
		      response = client.sendPutRequest(api, data,
		          MediaType.valueOf("application/ld+json"), headers);
		    }
		    else if (requestType.equals("GET")) {
		      response = client.sendGetRequest(api, headers);
		    }
		    else if (requestType.equals("DELETE")) {
			      response = client.sendDeleteRequest(api, headers); 
			}
		    int status = client.getStatus(response);
		    if (status == 207) {
                String body = client.getHttpResponseBody(response);
                // Orion can report 207 for a fully successful mixed create/update batch.
                // Accept it only when every requested ID is explicitly acknowledged.
                if (completeBatchSuccess(body, data)
                    || (api.endsWith("/entityOperations/delete") && completeDeleteSuccess(body, data))) status = 204;
                else logger.warn("CB incomplete batch response: {}", body);
            }
		    if (status != 200 && status != 201 && status != 204 && status != 207) {
		        try { logger.warn("CB RESPONSE BODY (" + status + "): " + client.getHttpResponseBody(response)); } catch (Exception ex) { logger.warn("Could not read CB response body: " + ex.getMessage()); }
		    }
		    // Release the pooled connection (consume any unread entity).
		    if (response != null) {
		        try { EntityUtils.consumeQuietly(response.getEntity()); } catch (Exception ignore) { /* no-op */ }
		    }
		    return status;
	}

    /** Deleting an already absent entity is successful only if every ID is accounted for. */
    static boolean completeDeleteSuccess(String responseBody, String requestBody) {
        try {
            JSONArray requested = new JSONArray(requestBody);
            Set<String> expected = new HashSet<>(), acknowledged = new HashSet<>();
            for (int i = 0; i < requested.length(); i++) {
                Object id = requested.get(i);
                if (!(id instanceof String) || ((String)id).isEmpty() || !expected.add((String)id)) return false;
            }
            JSONObject body = new JSONObject(responseBody);
            JSONArray success = body.getJSONArray("success"), errors = body.getJSONArray("errors");
            for (int i = 0; i < success.length(); i++) {
                Object id = success.get(i);
                if (!(id instanceof String) || !acknowledged.add((String)id)) return false;
            }
            for (int i = 0; i < errors.length(); i++) {
                JSONObject item = errors.getJSONObject(i), error = item.getJSONObject("error");
                if (error.getInt("status") != 404
                    || !"https://uri.etsi.org/ngsi-ld/errors/ResourceNotFound".equals(error.getString("type"))
                    || !acknowledged.add(item.getString("entityId"))) return false;
            }
            return !expected.isEmpty() && expected.equals(acknowledged);
        } catch (RuntimeException invalidResponse) {
            return false;
        }
    }

    static boolean completeBatchSuccess(String responseBody, String requestBody) {
        try {
            JSONObject body = new JSONObject(responseBody);
            JSONArray errors = body.optJSONArray("errors"), success = body.optJSONArray("success");
            if (errors == null || errors.length() != 0 || success == null) return false;
            JSONArray requested = new JSONArray(requestBody);
            Set<String> expected = new HashSet<>(), acknowledged = new HashSet<>();
            for (int i = 0; i < requested.length(); i++) {
                Object item = requested.get(i);
                String id = item instanceof JSONObject ? ((JSONObject)item).optString("id", "")
                        : item instanceof String ? (String)item : "";
                if (id.isEmpty()) return false;
                expected.add(id);
            }
            for (int i = 0; i < success.length(); i++) {
                Object item = success.get(i);
                if (!(item instanceof String)) return false;
                acknowledged.add((String)item);
            }
            return !expected.isEmpty() && expected.equals(acknowledged);
        } catch (RuntimeException invalidResponse) {
            return false;
        }
    }
	
	
}
