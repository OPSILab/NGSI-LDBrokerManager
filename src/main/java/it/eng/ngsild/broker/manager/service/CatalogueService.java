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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.TimeZone;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpResponse;
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


@Service
public class CatalogueService  {
	
    /** The logger. */
	private static Logger logger = LogManager.getLogger(CatalogueService.class);
	private static final Type DCAT_DATASET_LIST_TYPE = new TypeToken<List<DcatDataset>>() {
	}.getType();
  private static final int DATASET_PAGE_SIZE = 1000;

	public CatalogueService() {
	}

	
	@Value("${idra.basepath}")
	private String idraBasePath;
	
	public int start(Configurations config) throws Exception {
		return addCatalogueInCb(config);
	}
	
	public int delete(Configurations config) throws Exception {
		return deleteCatalogueFromCb(config);
	}

	
	public int addCatalogueInCb(Configurations config) throws Exception {
		
		String nodeId = config.getCatalogueId();
		String urlCB = buildContextBrokerApiBase(config.getContextBrokerUrl());
		
	      ArrayList<String> allDatasets = new ArrayList<String>();
	      ArrayList<String> allEntities = new ArrayList<String>();
	      
	      // I get the catalogue through Idra API
	      RestTemplate restTemplate = new RestTemplate();
	      
	      OdmsCatalogue node = restTemplate
	    	      .getForObject(idraBasePath + "/Idra/api/v1/" + "/client/catalogues/" + nodeId, OdmsCatalogue.class);
	      logger.info("CATALOGUE NAME: " + node.getName());

	      // I get all datasets through Idra API (paginated)
	      List<DcatDataset> datasets = getAllCatalogueDatasets(restTemplate, nodeId);
	      logger.info("Datasets collected for federation: " + datasets.size());

	      // ADDING DATASETS
	      for (DcatDataset dataset : datasets) {

	        String idDataset = "\"urn:ngsi-ld:Catalogue:dataset:" + dataset.getId() + "\"";
	        if (!allDatasets.contains(idDataset)) {
	          allDatasets.add(idDataset);
	        }
	        
	        // AGENT of the DATASET
	        if (dataset.getCreator() != null && dataset.getCreator().getIdentifier() != null
	            && dataset.getCreator().getIdentifier().getValue() != null) {
	          FoafAgent creator = dataset.getCreator();
//	          String identificator = creator.getId();
	          String identificator = creator.getIdentifier().getValue();
	          identificator = identificator.replaceAll("[^a-zA-Z0-9]", "");

	          String idDs = "urn:ngsi-ld:id:" + identificator;     
	          
	          String agentType = "";
	          if (creator.getType() != null) {
	        	  agentType = creator.getType().getValue();
	          }
	         
	          String api = urlCB + "entities/" + idDs;
	          int status = restRequest(api, "", "GET");
	          if (status != 200) {
	          
	            // ADDING AGENT CREATOR
	            String type = "AgentDCAT-AP";
	            String agent = firstValue(creator.getName());
	      
	            String data = "{ \"id\": \"" + idDs + "\", \"type\": \"" + type + "\","
		            + "\"name\": { "
		            + "\"type\": \"Property\","
		            + "\"value\": " + JSONObject.quote(agent) + " },"
	                + "\"agentType\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(agentType) + " }"
	                + " }";

	            if (!allEntities.contains(data)) {
	              allEntities.add(data);
	            }

	         }
	        }

	        if (dataset.getPublisher() != null && dataset.getPublisher().getIdentifier() != null
	            && dataset.getPublisher().getIdentifier().getValue() != null) {
	          FoafAgent publisher = dataset.getPublisher();
//	          String identificator = publisher.getId();
	          String identificator = publisher.getIdentifier().getValue();
	          identificator = identificator.replaceAll("[^a-zA-Z0-9]", "");

	          String idDs = "urn:ngsi-ld:id:" + identificator;     
	          
	          String agentType = "";
	          if (publisher.getType() != null) {
	        	  agentType = publisher.getType().getValue();
	          }

	          String api = urlCB + "entities/" + idDs;
	          int status = restRequest(api, "", "GET");
	          if (status != 200) {
	          
	            // ADDING AGENT PUBLISHER
	            String type = "AgentDCAT-AP";
	            String agent = firstValue(publisher.getName());
	      
	            String data = "{ \"id\": \"" + idDs + "\", \"type\": \"" + type + "\","
	                + "\"name\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(agent) + " },"
	                + "\"agentType\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(agentType) + " }"
	                + " }";

	            if (!allEntities.contains(data)) {
	              allEntities.add(data);
	            }

	          }
	        }

	        if (dataset.getRightsHolder() != null && dataset.getRightsHolder().getIdentifier() != null
	            && dataset.getRightsHolder().getIdentifier().getValue() != null) {
	          FoafAgent holder = dataset.getRightsHolder();
	          
//	          String identificator = holder.getId();
	          String identificator = holder.getIdentifier().getValue();
	          identificator = identificator.replaceAll("[^a-zA-Z0-9]", "");
	          String idDs = "urn:ngsi-ld:id:" + identificator; 
	          
	          String agentType = "";
	          if (holder.getType() != null) {
	        	  agentType = holder.getType().getValue();
	          }

	          String api = urlCB + "entities/" + idDs;
	          int status = restRequest(api, "", "GET");
	          if (status != 200) {
	          
	            // ADDING AGENT RIGHTS HOLDER
	            String type = "AgentDCAT-AP";
	            String agent = firstValue(holder.getName());
	      
	            String data = "{ \"id\": \"" + idDs + "\", \"type\": \"" + type + "\","
	                + "\"name\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(agent) + " },"
	                + "\"agentType\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(agentType) + " }"
	                + " }";

	            if (!allEntities.contains(data)) {
	              allEntities.add(data);
	            }

	          }
	        }
	        
	                
	        // ADDING DISTRIBUTIONS
	        ArrayList<String> allDistributions = new ArrayList<String>();
	        
	        List<DcatDistribution> distributions = dataset.getDistributions();
	        for (DcatDistribution d : distributions) {

	          String idDistribution = "\"urn:ngsi-ld:Dataset:items:" + d.getId() + "\"";
	          if (!allDistributions.contains(idDistribution)) {
	            allDistributions.add(idDistribution);
	          }

	          String identificat = d.getId();
	          
	          // Read distribution fields directly from the Java bean.
	          // distrib.optString("mediaType","") was previously used via an Idra API call,
	          // but DcatProperty fields serialize as {"value":"..."} not plain strings,
	          // so optString always returned "". Read from d directly like the other fields.
	          String format = (d.getFormat() != null && d.getFormat().getValue() != null)
	              ? d.getFormat().getValue() : "";
	          String mediaType = (d.getMediaType() != null && d.getMediaType().getValue() != null)
	              ? d.getMediaType().getValue() : "";
	          String titleD = (d.getTitle() != null && d.getTitle().getValue() != null)
	              ? d.getTitle().getValue() : "";
	          String des = (d.getDescription() != null && d.getDescription().getValue() != null)
	              ? d.getDescription().getValue() : "";
	          String stat = (d.getStatus() != null) ? d.getStatus().toString() : "";
	          
	          String idDis = "urn:ngsi-ld:DistributionDCAT-AP:id:" + identificat;
	                
	          //String api = urlCB + "entities/" + idDis;
	          //int status = restRequest(api, "", "GET");
	          //if (status != 200) {
	            
	            String typeDis = "DistributionDCAT-AP";

	            // Preserve natural-language characters (accents, punctuation, non-Latin
	            // scripts). JSONObject.quote handles JSON escaping safely without losing
	            // Unicode content.
	            String titleDis = titleD;
	            String descr = des;

	            // Build multilingual title/description for distribution
	            List<DcatDetails> disDetails = d.getDistributionDetails();
	            String titleDisJson;
	            String descrDisJson;
	            if (disDetails != null && !disDetails.isEmpty()) {
	                StringBuilder titleMap = new StringBuilder();
	                StringBuilder descMap = new StringBuilder();
	                boolean firstT = true, firstD2 = true;
	                for (DcatDetails det : disDetails) {
	                    String lang = det.getLanguage();
	                    if (lang == null || lang.isBlank()) continue;
	                    String tv = det.getTitle();
	                    if (tv != null && !tv.isBlank()) {
	                        if (!firstT) titleMap.append(",");
	                        titleMap.append(JSONObject.quote(lang)).append(":").append(JSONObject.quote(tv));
	                        firstT = false;
	                    }
	                    String dv = det.getDescription();
	                    if (dv != null && !dv.isBlank()) {
	                        if (!firstD2) descMap.append(",");
	                        descMap.append(JSONObject.quote(lang)).append(":").append(JSONObject.quote(dv));
	                        firstD2 = false;
	                    }
	                }
	                titleDisJson = firstT
	                    ? "\"title\":{\"type\":\"Property\",\"value\":[" + JSONObject.quote(titleDis) + "]},"
	                    : "\"title\":{\"type\":\"Property\",\"value\":{" + titleMap + "}},";
	                descrDisJson = firstD2
	                    ? "\"description\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(descr) + "},"
	                    : "\"description\":{\"type\":\"Property\",\"value\":{" + descMap + "}},";
	            } else {
	                titleDisJson = "\"title\":{\"type\":\"Property\",\"value\":[" + JSONObject.quote(titleDis) + "]},";
	                descrDisJson = "\"description\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(descr) + "},";
	            }

	            // H5: use isEmpty() — != "" confronta riferimenti, non contenuto
	            // H7: JSONArray garantisce serializzazione JSON valida
	            JSONArray languageList = new JSONArray();
	            for (DcatProperty lang : d.getLanguage()) {
	                if (lang.getValue() != null && !lang.getValue().isEmpty())
	                    languageList.put(lang.getValue());
	            }
	            
	            String byteSize = "";
	            if (d.getByteSize() != null) {
	            	byteSize = d.getByteSize().getValue();
	            }
	            String checksum = "";
	            if (d.getChecksum() != null) {
	            	//checksum = d.getChecksum().getChecksumValue().getValue().replaceAll("\"", "");
	            	checksum = d.getChecksum().getChecksumValue().getValue();
	            }

	            // aggiungere dateCreated e dateModified come TIMESTAMP della Entity?
	            
	            String releaseDateDisJson = "";
	            if (d.getReleaseDate() != null && d.getReleaseDate().getValue() != null && !d.getReleaseDate().getValue().isEmpty()) {
	                releaseDateDisJson = "\"releaseDate\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\"" + d.getReleaseDate().getValue() + "\"}}, ";
	            }
	            String modifiedDateDisJson = "";
	            if (d.getUpdateDate() != null && d.getUpdateDate().getValue() != null && !d.getUpdateDate().getValue().isEmpty()) {
	                modifiedDateDisJson = "\"modifiedDate\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\"" + d.getUpdateDate().getValue() + "\"}}, ";
	            }
	            String accessUrlVal = (d.getAccessUrl() != null && d.getAccessUrl().getValue() != null)
	            ? d.getAccessUrl().getValue() : "";
	            String downloadUrlVal = (d.getDownloadUrl() != null && d.getDownloadUrl().getValue() != null)
	            ? d.getDownloadUrl().getValue() : "";
	            String licenseNameVal = (d.getLicense() != null && d.getLicense().getName() != null
	                && d.getLicense().getName().getValue() != null)
	            ? d.getLicense().getName().getValue() : "";
	            String rightsVal = (d.getRights() != null && d.getRights().getValue() != null)
	            ? d.getRights().getValue() : "";
	            JSONArray applicableLegislationDis = new JSONArray();
	            if (d.getApplicableLegislation() != null) {
	                for (DcatProperty al : d.getApplicableLegislation()) {
	                    if (al.getValue() != null && !al.getValue().isEmpty())
	                        applicableLegislationDis.put(al.getValue());
	                }
	            }
	            String dataDis = "{ \"id\": \"" + idDis + "\", \"type\": \"" + typeDis + "\","
	                + descrDisJson
	                + titleDisJson
	                + releaseDateDisJson
	                + modifiedDateDisJson
	                + "\"accessUrl\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": [ " + JSONObject.quote(accessUrlVal) + " ]"
	                + " },"
	                + "\"downloadURL\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": [ " + JSONObject.quote(downloadUrlVal) + " ]"
	                + " },"
	                + "\"license\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(licenseNameVal)
	                + " },"
	                + "\"format\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + format + "\""
	                + " },"
	                + "\"mediaType\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + mediaType + "\""
	                + " },"
	                + "\"rights\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " + JSONObject.quote(rightsVal)
	                + " },"
	                + "\"language\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": " +  languageList.toString()    
	                + " },"
	                + "\"byteSize\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + byteSize + "\""
	                + " },"
	                + "\"checksum\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + checksum + "\""
	                + " },"
	                + "\"status\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + stat + "\""
	                + " }"
	                + (applicableLegislationDis.length() > 0
	                    ? ",\"applicableLegislation\":{\"type\":\"Property\",\"value\":"
	                        + applicableLegislationDis.toString() + "}"
	                    : "")
	                + " }";
	            	
	            if (!allEntities.contains(dataDis)) {
	              allEntities.add(dataDis);
	            }
	          //}
	        } 

	        String idDs = "urn:ngsi-ld:Dataset:id:" + dataset.getId();     
	        //String api = urlCB + "entities/" + idDs;
	        //int status = restRequest(api, "", "GET");
	        //if (status != 200) {
	        
	          // ADDING DATASET — preserve UTF-8 / non-Latin content. JSON escaping is
	          // handled by JSONObject.quote; the old replaceAll("[^a-zA-Z0-9]", " ")
	          // stripped accents and unicode chars from natural-language fields.
	          String des = dataset.getDescription().getValue();
	          String descr = des;

	          String t = dataset.getTitle().getValue();
	          String title = t;

	          // Build multilingual title/description for dataset
	          List<DcatDetails> dsDetails = dataset.getDatasetDetails();
	          String titleDsJson;
	          String descrDsJson;
	          if (dsDetails != null && !dsDetails.isEmpty()) {
	              StringBuilder titleMap = new StringBuilder();
	              StringBuilder descMap = new StringBuilder();
	              boolean firstT = true, firstD2 = true;
	              for (DcatDetails det : dsDetails) {
	                  String lang = det.getLanguage();
	                  if (lang == null || lang.isBlank()) continue;
	                  String tv = det.getTitle();
	                  if (tv != null && !tv.isBlank()) {
	                      if (!firstT) titleMap.append(",");
	                      titleMap.append(JSONObject.quote(lang)).append(":").append(JSONObject.quote(tv));
	                      firstT = false;
	                  }
	                  String dv = det.getDescription();
	                  if (dv != null && !dv.isBlank()) {
	                      if (!firstD2) descMap.append(",");
	                      descMap.append(JSONObject.quote(lang)).append(":").append(JSONObject.quote(dv));
	                      firstD2 = false;
	                  }
	              }
	              titleDsJson = firstT
	                  ? "\"title\":{\"type\":\"Property\",\"value\":[" + JSONObject.quote(title) + "]},"
	                  : "\"title\":{\"type\":\"Property\",\"value\":{" + titleMap + "}},";
	              descrDsJson = firstD2
	                  ? "\"description\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(descr) + "},"
	                  : "\"description\":{\"type\":\"Property\",\"value\":{" + descMap + "}},";
	          } else {
	              titleDsJson = "\"title\":{\"type\":\"Property\",\"value\":[" + JSONObject.quote(title) + "]},";
	              descrDsJson = "\"description\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(descr) + "},";
	          }

	          String creator = "";
	          if (dataset.getCreator() != null) {
	            creator = firstValue(dataset.getCreator().getName());
	          }
	          String publisher = "";
	          if (dataset.getPublisher() != null) {
	            publisher = firstValue(dataset.getPublisher().getName());
	          }
	          
	          // H6 + H7: null-safe contact email, JSONArray for valid JSON
	          JSONArray contacts = new JSONArray();
	          for (VcardOrganization v : dataset.getContactPoint()) {
	              if (v.getHasEmail() != null && v.getHasEmail().getValue() != null
	                      && !v.getHasEmail().getValue().isEmpty()) {
	                  contacts.put(v.getHasEmail().getValue());
	              }
	          }
//	          if (contacts.size() == 0)
//	        	  contacts.add("");
	          
	          // Emit themes preferring the SKOS Concept URI (e.g. EU data-theme authority)
	          // over a free-text label. Falls back to the preferred label when no URI is
	          // available. Old behaviour pushed only the label, losing the authority link.
	          JSONArray themes = new JSONArray();
	          for (SkosConceptTheme tema : dataset.getTheme()) {
	              String uri = tema.getResourceUri();
	              if (uri != null && !uri.isEmpty()) {
	                  themes.put(uri);
	                  continue;
	              }
	              for (SkosPrefLabel label : tema.getPrefLabel()) {
	                  if (label.getValue() != null && !label.getValue().isEmpty()) {
	                      themes.put(label.getValue());
	                  }
	              }
	          }
	          if (themes.length() == 0) themes.put("");
	          
	          // Build multilingual keywords
	          String keywordDsJson;
	          List<DcatKeyword> kwDetails = dataset.getKeywordDetails();
	          if (kwDetails != null && !kwDetails.isEmpty()) {
	              Map<String, List<String>> kwByLang = new LinkedHashMap<>();
	              for (DcatKeyword kw : kwDetails) {
	                  String val = kw.getValue();
	                  if (val == null || val.isBlank()) continue;
	                  String lang = (kw.getLanguage() != null && !kw.getLanguage().isBlank())
	                                ? kw.getLanguage() : "und";
	                  kwByLang.computeIfAbsent(lang, k -> new ArrayList<>()).add(val);
	              }
	              if (!kwByLang.isEmpty()) {
	                  StringBuilder kwMap = new StringBuilder();
	                  boolean firstLang = true;
	                  for (Map.Entry<String, List<String>> entry : kwByLang.entrySet()) {
	                      if (!firstLang) kwMap.append(",");
	                      kwMap.append("\"").append(entry.getKey()).append("\":[")
	                           .append(entry.getValue().stream()
	                               .map(v -> "\"" + v + "\"")
	                               .collect(Collectors.joining(",")))
	                           .append("]");
	                      firstLang = false;
	                  }
	                  keywordDsJson = "\"keyword\":{\"type\":\"Property\",\"value\":{" + kwMap + "}},";
	              } else {
	                  ArrayList<String> keywords = new ArrayList<>();
	                  for (String keyw : dataset.getKeywords()) {
	                      if (!keyw.isEmpty()) keywords.add("\"" + keyw + "\"");
	                  }
	                  if (keywords.isEmpty()) keywords.add("\"\"");
	                  keywordDsJson = "\"keyword\":{\"type\":\"Property\",\"value\":" + keywords + "},";
	              }
	          } else {
	              ArrayList<String> keywords = new ArrayList<>();
	              for (String keyw : dataset.getKeywords()) {
	                  if (!keyw.isEmpty()) keywords.add("\"" + keyw + "\"");
	              }
	              if (keywords.isEmpty()) keywords.add("\"\"");
	              keywordDsJson = "\"keyword\":{\"type\":\"Property\",\"value\":" + keywords + "},";
	          }
	          // H5 + H7: isEmpty() check, JSONArray serialization
	          JSONArray documentation = new JSONArray();
	          for (DcatProperty doc : dataset.getDocumentation()) {
	              if (doc.getValue() != null && !doc.getValue().isEmpty())
	                  documentation.put(doc.getValue());
	          }

	          JSONArray language = new JSONArray();
	          for (DcatProperty lan : dataset.getLanguage()) {
	              if (lan.getValue() != null && !lan.getValue().isEmpty())
	                  language.put(lan.getValue());
	          }
	          JSONArray otherIdentifier = new JSONArray();
	          for (DcatProperty otId : dataset.getOtherIdentifier()) {
	              if (otId.getValue() != null && !otId.getValue().isEmpty())
	                  otherIdentifier.put(otId.getValue());
	          }
	          JSONArray provenance = new JSONArray();
	          for (DcatProperty pr : dataset.getProvenance()) {
	              if (pr.getValue() != null && !pr.getValue().isEmpty())
	                  provenance.put(pr.getValue());
	          }
	          String frequency = "";
	          if (dataset.getFrequency() != null) {
	        	  frequency = dataset.getFrequency().getValue();
	          }
	          
//	          if (dataset.getIsVersionOf() != null) {
//	        	  System.out.println("isVersOf: " + dataset.getIsVersionOf());
//	          }
//	          if (dataset.getHasVersion() != null) {
//	        	  System.out.println("hasVers: " + dataset.getHasVersion());
//	          }
//	          ArrayList<String> hasVersion = new ArrayList<String>();
//	          for (DcatProperty hasV: dataset.getHasVersion()) {
//	          	if(hasV.getValue() != "")
//	          		hasVersion.add("\"" + hasV.getValue() + "\"");
//	          }
	          String startDate = "";
	          String endDate = "";
	          if (dataset.getTemporalCoverage() != null && !dataset.getTemporalCoverage().isEmpty()) {
	        	  if (dataset.getTemporalCoverage().get(0).getStartDate() != null) {
	        		  startDate = dataset.getTemporalCoverage().get(0).getStartDate().getValue();
	        	  }
	        	  if (dataset.getTemporalCoverage().get(0).getEndDate() != null) {
	        		  endDate = dataset.getTemporalCoverage().get(0).getEndDate().getValue();
	        	  }
	          }
	          String version = "";
	          if (dataset.getVersion() != null) {
	        	  version = dataset.getVersion().getValue();
	          }
	          JSONArray versionNotes = new JSONArray();
	          for (DcatProperty verNotes : dataset.getVersionNotes()) {
	              if (verNotes.getValue() != null && !verNotes.getValue().isEmpty())
	                  versionNotes.put(verNotes.getValue());
	          }

	          // dateCreated è la creazione della Entity
	          // dateModified Timestamp of the last modification of the entity. 
	          // This will usually be allocated by the storage platform
	   
	          // ---------------- DCAT Dataset entity assembly (NGSI-LD) ----------------
	          // Build the body as a list of "key":value fragments (each WITHOUT trailing
	          // comma) then join with ", ". Empty fragments are skipped — no empty-value
	          // properties are pushed to Orion-LD.
	          JSONArray applicableLegislationDs = new JSONArray();
	          if (dataset.getApplicableLegislation() != null) {
	              for (DcatProperty al : dataset.getApplicableLegislation()) {
	                  if (al.getValue() != null && !al.getValue().isEmpty())
	                      applicableLegislationDs.put(al.getValue());
	              }
	          }
	          JSONArray hvdCategoryDs = new JSONArray();
	          if (dataset.getHVDCategory() != null) {
	              for (DcatProperty hc : dataset.getHVDCategory()) {
	                  if (hc.getValue() != null && !hc.getValue().isEmpty())
	                      hvdCategoryDs.put(hc.getValue());
	              }
	          }

	          String accessRightsValue = (dataset.getAccessRights() != null
	              && dataset.getAccessRights().getValue() != null)
	                  ? dataset.getAccessRights().getValue() : "";
	          String landingPageValue = (dataset.getLandingPage() != null
	              && dataset.getLandingPage().getValue() != null)
	                  ? dataset.getLandingPage().getValue() : "";
	          String sourceValue = (dataset.getSource() != null && !dataset.getSource().isEmpty()
	              && dataset.getSource().get(0).getValue() != null)
	                  ? dataset.getSource().get(0).getValue() : landingPageValue;

	          String typeDs = "Dataset";
	          java.util.List<String> dsParts = new java.util.ArrayList<>();
	          // Stripped trailing commas from these pre-built fragments to compose cleanly.
	          String descrCore = stripTrailingComma(descrDsJson);
	          if (!descrCore.isEmpty()) dsParts.add(descrCore);
	          String titleCore = stripTrailingComma(titleDsJson);
	          if (!titleCore.isEmpty()) dsParts.add(titleCore);
	          if (!landingPageValue.isEmpty())
	              dsParts.add("\"landingPage\":{\"type\":\"Property\",\"value\":[\"" + landingPageValue + "\"]}");
	          dsParts.add("\"datasetDistribution\":{\"type\":\"Property\",\"value\":["
	              + String.join(",", allDistributions) + "]}");
	          dsParts.add("\"contactPoint\":{\"type\":\"Property\",\"value\":" + contacts.toString() + "}");
	          String keywordCore = stripTrailingComma(keywordDsJson);
	          if (!keywordCore.isEmpty()) dsParts.add(keywordCore);
	          dsParts.add("\"theme\":{\"type\":\"Property\",\"value\":" + themes.toString() + "}");
	          dsParts.add("\"documentation\":{\"type\":\"Property\",\"value\":" + documentation.toString() + "}");
	          dsParts.add("\"language\":{\"type\":\"Property\",\"value\":" + language.toString() + "}");
	          dsParts.add("\"otherIdentifier\":{\"type\":\"Property\",\"value\":" + otherIdentifier.toString() + "}");
	          dsParts.add("\"provenance\":{\"type\":\"Property\",\"value\":" + provenance.toString() + "}");
	          dsParts.add("\"versionNotes\":{\"type\":\"Property\",\"value\":" + versionNotes.toString() + "}");
	          if (version != null && !version.isEmpty())
	              dsParts.add("\"version\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(version) + "}");
	          if (!startDate.isEmpty() && !endDate.isEmpty())
	              dsParts.add("\"temporal\":{\"type\":\"Property\",\"value\":[{\"@type\":\"DateTime\",\"@value\":\""
	                  + startDate + "\"},{\"@type\":\"DateTime\",\"@value\":\"" + endDate + "\"}]}");
	          if (!accessRightsValue.isEmpty())
	              dsParts.add("\"accessRights\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(accessRightsValue) + "}");
	          if (dataset.getReleaseDate() != null && dataset.getReleaseDate().getValue() != null
	              && !dataset.getReleaseDate().getValue().isEmpty())
	              dsParts.add("\"dateCreated\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\""
	                  + dataset.getReleaseDate().getValue() + "\"}}");
	          if (dataset.getUpdateDate() != null && dataset.getUpdateDate().getValue() != null
	              && !dataset.getUpdateDate().getValue().isEmpty())
	              dsParts.add("\"dateModified\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\""
	                  + dataset.getUpdateDate().getValue() + "\"}}");
	          if (publisher != null && !publisher.isEmpty())
	              dsParts.add("\"publisher\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(publisher) + "}");
	          if (creator != null && !creator.isEmpty())
	              dsParts.add("\"creator\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(creator) + "}");
	          if (frequency != null && !frequency.isEmpty())
	              dsParts.add("\"frequency\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(frequency) + "}");
	          if (sourceValue != null && !sourceValue.isEmpty())
	              dsParts.add("\"source\":{\"type\":\"Property\",\"value\":" + JSONObject.quote(sourceValue) + "}");
	          if (applicableLegislationDs.length() > 0)
	              dsParts.add("\"applicableLegislation\":{\"type\":\"Property\",\"value\":"
	                  + applicableLegislationDs.toString() + "}");
	          if (hvdCategoryDs.length() > 0)
	              dsParts.add("\"hvdCategory\":{\"type\":\"Property\",\"value\":"
	                  + hvdCategoryDs.toString() + "}");

	          String dataDs = "{\"id\":\"" + idDs + "\",\"type\":\"" + typeDs + "\","
	              + String.join(",", dsParts) + "}";
	          
	          if (!allEntities.contains(dataDs)) {
	            allEntities.add(dataDs);
	          }
	          
	      }

	      int identificator = node.getId();
	      String id = "urn:ngsi-ld:Catalogue:id:" + identificator;
	      String api = urlCB + "entities/" + id;
          //String api = urlCB + "entities/";
	      //int status = restRequest(api, "", "GET");
	      //if (status != 200) {

	        ZonedDateTime dateModified = node.getLastUpdateDate();
	        DateTime modif = new DateTime(dateModified.toInstant().toEpochMilli(),
		            DateTimeZone.forTimeZone(TimeZone.getTimeZone(dateModified.getZone())));
	        
//	        System.out.println("REG DATE: " + node.getRegisterDate());
//	        String regDate = "";
//	        if (node.getRegisterDate() != null) {
//		        ZonedDateTime dateCreate = node.getRegisterDate();
//		        DateTime created = new DateTime(dateCreate.toInstant().toEpochMilli(),
//		            DateTimeZone.forTimeZone(TimeZone.getTimeZone(dateCreate.getZone())));
//		        regDate = created.toString();
//	        }

//	        System.out.println(" ----------- location :" + node.getLocation());
//	        System.out.println(" ----------- location descrp:" + node.getLocationDescription());
	        // Preserve UTF-8 content; JSONObject.quote handles JSON escaping.
	        String description = node.getDescription() != null ? node.getDescription() : "";
	        String name = node.getName() != null ? node.getName() : "";

	        String type = "CatalogueDCAT-AP";
	        String data = "{ \"id\": \"" + id + "\", \"type\": \"" + type + "\","
	            + "\"description\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": " + JSONObject.quote(description) + " },"
	            + "\"publisher\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": " + JSONObject.quote(node.getPublisherName()) + " },"
	            + "\"title\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": [ " + JSONObject.quote(name) + " ]"
	            + " }, "
	            + "\"name\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": \"Catalogue\" },"
	            + "\"alternateName\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": \"\" },"
	            + "\"homepage\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": " + JSONObject.quote(node.getHomepage()) + " },"
	            + "\"dataProvider\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": \"\" },"
	            + "\"source\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": " + JSONObject.quote(node.getHost()) + " },"
//				+ "\"dateCreated\": { "
//				+ "\"type\": \"Property\","
//				+ "\"value\": { "
//				+ "\"@type\": \"DateTime\","
//				+ "\"@value\": \"" + regDate + "\"  }"
//				+ " }, "
	            + "\"dateModified\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": { "
	            + "\"@type\": \"DateTime\","
	            + "\"@value\": \"" + modif + "\"  }"
	            + " }, "
	            + "\"dataset\": { "
	            + "\"type\": \"Relationship\","
	            + "\"object\": [" + String.join(",", allDatasets) + "]"
	            + " }, "
	            + "\"language\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": [ " + JSONObject.quote(node.getCountry()) + " ]"
	            + " } "
	            + " }";
	        
	        if (!allEntities.contains(data)) {
	          allEntities.add(data);
	        }

	      
	      identificator = node.getId();
	      id = "urn:ngsi-ld:id:" + identificator;   
	      api = urlCB + "entities/" + id;
	      //int status = restRequest(api, "", "GET");
	      //if (status != 200) {
	        type = "AgentDCAT-AP";
	        String agent = node.getPublisherName();
	        identificator = node.getId();
	        id = "urn:ngsi-ld:id:" + identificator;
	        data = "{ \"id\": \"" + id + "\", \"type\": \"" + type + "\","
	            + "\"name\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": " + JSONObject.quote(agent) + " }"
	            + " }";
	        
	        if (!allEntities.contains(data)) {
	          allEntities.add(data);
	        }

	        // DCAT-AP 3: add creator agent for catalogue if present
	        if (node.getCreator() != null) {
	          FoafAgent catCreator = node.getCreator();
	          if (catCreator.getIdentifier() != null && catCreator.getIdentifier().getValue() != null) {
	            String creatorId = catCreator.getIdentifier().getValue().replaceAll("[^a-zA-Z0-9]", "");
	            String creatorUrn = "urn:ngsi-ld:id:" + creatorId;
	            String creatorName = firstValue(catCreator.getName());
	            String creatorType = catCreator.getType() != null ? catCreator.getType().getValue() : "";
	            String creatorData = "{ \"id\": \"" + creatorUrn + "\", \"type\": \"AgentDCAT-AP\","
	                + "\"name\": { \"type\": \"Property\", \"value\": " + JSONObject.quote(creatorName) + " },"
	                + "\"agentType\": { \"type\": \"Property\", \"value\": " + JSONObject.quote(creatorType) + " }"
	                + " }";
	            if (!allEntities.contains(creatorData)) {
	              allEntities.add(creatorData);
	            }
	          }
	        }

	      // POST CREATE request in BATCH
	      int status = 200;
	      api = urlCB + "entityOperations/create";
	      if (allEntities.size() > 200) {
	    	
	        status = postRequestWithCheck(allEntities, api, 200);
	        logger.info("STATUS CREATE CATALOGUE IN THE CB: " + status);
	      } else {
	    	// POST CREATE BATCH 
			logger.info("PAYLOAD TO CB (" + allEntities.size() + " entities): " + allEntities.toString());
	        status = restRequest(api, allEntities.toString(), "POST");
	        logger.info("STATUS CREATE CATALOGUE IN THE CB: " + status);
	      }
//	  if (status != 200 && status != 207 && status != 204 
//	        && status != 201 && status != 301) {
//	      throw new Exception("------------ STATUS POST - CONTEXT BROKER: " + status);
//	  }
	  return status;
	}
	

	  /**
	   * Delete a Catalogue from the CB.
	  // * @throws Exception 
	  // * @throws MalformedURLException 
	   */
	  public int deleteCatalogueFromCb(Configurations config) throws MalformedURLException, Exception {
		  
		  String nodeId = config.getCatalogueId();
		  String urlCB = buildContextBrokerApiBase(config.getContextBrokerUrl());
	      
	      // I get the Catalogue through Idra API
	      RestTemplate restTemplate = new RestTemplate();
	      ResponseEntity<String> result = restTemplate.getForEntity(idraBasePath + "/Idra/api/v1/" 
	      + "/client/catalogues/" + nodeId, String.class); 
	      logger.info("RESULT GET CATALOGUE: " + result.getStatusCodeValue());
	      
	      OdmsCatalogue node = restTemplate
	    	      .getForObject(idraBasePath + "/Idra/api/v1/" + "/client/catalogues/" + nodeId, OdmsCatalogue.class);
	      logger.info("CATALOGUE NAME to delete: " + node.getName());

	      // I get all datasets through Idra API (paginated)
	      List<DcatDataset> datasets = getAllCatalogueDatasets(restTemplate, nodeId);
	      logger.info("Datasets collected for deletion: " + datasets.size());

	      // DELETING
	      ArrayList<String> listId = new ArrayList<String>();
	  
	      // CATALOGUE DELETING
	      int identificator = node.getId();
	      String id = "\"urn:ngsi-ld:Catalogue:id:" + identificator + "\"";
	      listId.add(id);
	      
	      // CATALOGUE AGENT DELETING
	      identificator = node.getId();
	      id = "\"urn:ngsi-ld:id:" + identificator + "\"";
	      listId.add(id);
	      
	      // DATASETS DELETING 
	      //List<DcatDataset> datasets = MetadataCacheManager.getAllDatasetsByOdmsCatalogue(node.getId());
	      for (DcatDataset dataset : datasets) {   
	        String identif = dataset.getId();
	        String idDs = "\"urn:ngsi-ld:Dataset:id:" + identif + "\"";
	        if (!listId.contains(idDs)) {
	          listId.add(idDs);
	        }
	        
	        // DATASETS AGENTS DELETING 
	        // 1. CREATORS
	        if (dataset.getCreator() != null) {
	          identif = dataset.getCreator().getIdentifier().getValue();
	          identif = identif.replaceAll("[^a-zA-Z0-9]", "");
	          idDs = "\"urn:ngsi-ld:id:" + identif + "\"";
	          if (!listId.contains(idDs)) {
	            listId.add(idDs);
	            
	          }
	        }
	        // 2. PUBLISHERS
	        if (dataset.getPublisher() != null) {    
	          identif = dataset.getPublisher().getIdentifier().getValue();
	          identif = identif.replaceAll("[^a-zA-Z0-9]", "");
	          idDs = "\"urn:ngsi-ld:id:" + identif + "\"";
	          if (!listId.contains(idDs)) {
	            listId.add(idDs);
	            
	          }
	        }
	        // 3. RIGHT HOLDERS
	        if (dataset.getRightsHolder() != null) {  
	          identif = dataset.getRightsHolder().getIdentifier().getValue();
	          identif = identif.replaceAll("[^a-zA-Z0-9]", "");
	          idDs = "\"urn:ngsi-ld:id:" + identif + "\"";
	          if (!listId.contains(idDs)) {
	            listId.add(idDs);
	            
	          }
	        }
	        
	        // DISTRIBUTIONS DELETING
	        List<DcatDistribution> distributions = dataset.getDistributions();
	        for (DcatDistribution d : distributions) {
	          String identificat = d.getId();
	          String idDis = "\"urn:ngsi-ld:DistributionDCAT-AP:id:" + identificat + "\"";

	          if (!listId.contains(idDis)) {
	            listId.add(idDis);
	            
	          }

	        } 
	      }

	      String data =  listId.toString();
	      String api = urlCB + "entityOperations/delete";
	      int status = 200;
	      logger.info("Entity IDs queued for deletion from CB: " + listId.size());
	      if (listId.size() > 200) {
	    	
	        status = postRequestWithCheck(listId, api, 200);
	        
	      } else {
	    	  
	    	// POST DELETE BATCH 
	        status = restRequest(api, data, "POST");
	        logger.info("STATUS DELETE " + status);
//	        if (status != 200 && status != 207 && status != 204 && status != -1 
//	            && status != 201 && status != 301) {
//	          throw new Exception("------------ STATUS DELETE DISTRIBUTION - CONTEXT BROKER: " + status);
//	        }
	      }
	      return status;
	  }
	  

	  /**
	   * Post Request on CB with Check on the number of Entities.
	// * @throws Exception exception.
	   * 
	   */
	  public int postRequestWithCheck(List<String> listId, String api, int maxNumberOfEntities) throws Exception {
	    String data =  listId.toString();
	    
	    int r = (listId.size()) % maxNumberOfEntities;
	    int tot = listId.size() - r;
	    int numberOfPost = tot / maxNumberOfEntities;
	    
	    int i = 0;
	    int l = maxNumberOfEntities - 1; // 199
	    
	    int status = 200;
	    
	    for (int k = 0; k < numberOfPost; k++) {
	      List<String> list = listId.subList(i, l + 1);
	      data =  list.toString();

	      status = restRequest(api, data, "POST");
	      
	      if (status != 200 && status != 207 && status != 204 && status != -1 
	          && status != 201 && status != 301) {
	    	  return status;
//	        throw new Exception("------------ STATUS POST - CONTEXT BROKER: " + status);
	      }
	      i += maxNumberOfEntities;
	      l += maxNumberOfEntities;
	    }
	  
	    List<String> list = listId.subList(i, (listId.size()));
	    if (list.size() != 0) {
		    data =  list.toString(); 
	
		    status = restRequest(api, data, "POST");
		    if (status != 200 && status != 207 && status != 204 && status != -1 
		        && status != 201 && status != 301) {
		    	return status;
//		      throw new Exception("------------ STATUS POST - CONTEXT BROKER: " + status);
		    }
	    }
	    return status;
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

	  private static void validateContextBrokerUrl(String url) {
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
	      // Block SSRF to loopback and link-local addresses
	      java.net.InetAddress addr = java.net.InetAddress.getByName(host);
	      if (addr.isLoopbackAddress() || addr.isLinkLocalAddress()) {
	        throw new SecurityException("Context Broker URL resolves to a loopback/link-local address");
	      }
	    } catch (java.net.URISyntaxException | java.net.UnknownHostException e) {
	      throw new IllegalArgumentException("Invalid Context Broker URL: " + e.getMessage(), e);
	    }
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
		    headers.put("Content-Type", "application/json");
		    RestClient client = new RestClientImpl();
		    HttpResponse response = null;
		    if (requestType.equals("POST")) {
		      response = client.sendPostRequest(api, data,
		          MediaType.APPLICATION_JSON_TYPE, headers); 
		    }
		    else if (requestType.equals("PUT")) {
		      response = client.sendPutRequest(api, data,
		          MediaType.APPLICATION_JSON_TYPE, headers); 
		    }
		    else if (requestType.equals("GET")) {
		      response = client.sendGetRequest(api, headers);
		    }
		    else if (requestType.equals("DELETE")) {
			      response = client.sendDeleteRequest(api, headers); 
			}
		    int status = client.getStatus(response);
		    if (status != 200 && status != 201 && status != 204) {
		        try { logger.warn("CB RESPONSE BODY (" + status + "): " + client.getHttpResponseBody(response)); } catch (Exception ex) { logger.warn("Could not read CB response body: " + ex.getMessage()); }
		    }
		    return status;
	}
	
	
}
