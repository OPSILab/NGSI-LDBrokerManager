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
	        if (dataset.getCreator() != null) {
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
		            + "\"value\": \"" + agent + "\" }," 
	                + "\"agentType\": { " 
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + agentType + "\" }" 
	                + " }";
	            
	            if (!allEntities.contains(data)) {
	              allEntities.add(data);
	            }

	         }
	        }

	        if (dataset.getPublisher() != null) {
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
	                + "\"value\": \"" + agent + "\" }," 
	                + "\"agentType\": { " 
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + agentType + "\" }" 
	                + " }";
	            
	            if (!allEntities.contains(data)) {
	              allEntities.add(data);
	            }
	            
	          }       
	        }

	        if (dataset.getRightsHolder() != null) {
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
	                + "\"value\": \"" + agent + "\" }," 
	                + "\"agentType\": { " 
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + agentType + "\" }" 
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
	          
	          // I get the Distributions through Idra API
	          String apiD = idraBasePath + "/Idra/api/v1/" + "/client/catalogues/" + nodeId + "/datasets/" 
	        		  + dataset.getId() + "/distributions/" + d.getId();
	          Map<String, String> headers = new HashMap<String, String>();
	          headers.put("Content-Type", "application/json");
	          RestClient client = new RestClientImpl();
	          String returnedJson = "";
	          HttpResponse response = client.sendGetRequest(apiD, headers);
	          returnedJson = client.getHttpResponseBody(response);
	          JSONObject distrib = new JSONObject(returnedJson);
	          
	          String format = distrib.optString("format", "");
	          String mediaType = distrib.optString("mediaType", "");
	          String titleD = distrib.optString("title", "");
	          String des = distrib.optString("description", "");
	          String stat = distrib.optString("status", ""); // a value between Completed, Deprecated, Under Development, Withdrawn 
	          
	          String idDis = "urn:ngsi-ld:DistributionDCAT-AP:id:" + identificat;
	                
	          //String api = urlCB + "entities/" + idDis;
	          //int status = restRequest(api, "", "GET");
	          //if (status != 200) {
	            
	            String typeDis = "DistributionDCAT-AP";

	            String titleDis = titleD.replaceAll("[^a-zA-Z0-9]", " ");
	            String descr = des.replaceAll("[^a-zA-Z0-9]", " ");

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
	                        titleMap.append("\"").append(lang).append("\":\"").append(tv.replaceAll("\"", "").replaceAll("'", " ")).append("\"");
	                        firstT = false;
	                    }
	                    String dv = det.getDescription();
	                    if (dv != null && !dv.isBlank()) {
	                        if (!firstD2) descMap.append(",");
	                        descMap.append("\"").append(lang).append("\":\"").append(dv.replaceAll("[^a-zA-Z0-9 ]", " ")).append("\"");
	                        firstD2 = false;
	                    }
	                }
	                titleDisJson = firstT
	                    ? "\"title\":{\"type\":\"Property\",\"value\":[\"" + titleDis + "\"]},"
	                    : "\"title\":{\"type\":\"Property\",\"value\":{" + titleMap + "}},";
	                descrDisJson = firstD2
	                    ? "\"description\":{\"type\":\"Property\",\"value\":\"" + descr + "\"},"
	                    : "\"description\":{\"type\":\"Property\",\"value\":{" + descMap + "}},";
	            } else {
	                titleDisJson = "\"title\":{\"type\":\"Property\",\"value\":[\"" + titleDis + "\"]},";
	                descrDisJson = "\"description\":{\"type\":\"Property\",\"value\":\"" + descr + "\"},";
	            }

	            ArrayList<String> languageList = new ArrayList<String>();
	            for(DcatProperty lang: d.getLanguage()) {
	            	if (lang.getValue() != "")
	            		languageList.add("\"" + lang.getValue() + "\"");
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
	            String dataDis = "{ \"id\": \"" + idDis + "\", \"type\": \"" + typeDis + "\","
	                + descrDisJson
	                + titleDisJson
	                + releaseDateDisJson
	                + modifiedDateDisJson
	                + "\"accessUrl\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": [ \"" + d.getAccessUrl().getValue() +  "\" ]" 
	                + " },"
	                + "\"downloadURL\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": [ \"" + d.getDownloadUrl().getValue() + "\" ]" 
	                + " },"
	                + "\"license\": { "
	                + "\"type\": \"Property\","
	                + "\"value\": \"" + d.getLicense().getName().getValue() + "\""
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
	                + "\"value\": \"" + d.getRights().getValue() + "\""
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
	        
	          // ADDING DATASET
	          String des = dataset.getDescription().getValue();
	          String descr = des.replaceAll("[^a-zA-Z0-9]", " ");

	          String t = dataset.getTitle().getValue();
	          //String title = t.replaceAll("[^a-zA-Z0-9]", " ");
	          String title = t.replaceAll("\"", "");
	          title = title.replaceAll("'", " ");

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
	                      titleMap.append("\"").append(lang).append("\":\"").append(tv.replaceAll("\"", "").replaceAll("'", " ")).append("\"");
	                      firstT = false;
	                  }
	                  String dv = det.getDescription();
	                  if (dv != null && !dv.isBlank()) {
	                      if (!firstD2) descMap.append(",");
	                      descMap.append("\"").append(lang).append("\":\"").append(dv.replaceAll("[^a-zA-Z0-9 ]", " ")).append("\"");
	                      firstD2 = false;
	                  }
	              }
	              titleDsJson = firstT
	                  ? "\"title\":{\"type\":\"Property\",\"value\":[\"" + title + "\"]},"
	                  : "\"title\":{\"type\":\"Property\",\"value\":{" + titleMap + "}},";
	              descrDsJson = firstD2
	                  ? "\"description\":{\"type\":\"Property\",\"value\":\"" + descr + "\"},"
	                  : "\"description\":{\"type\":\"Property\",\"value\":{" + descMap + "}},";
	          } else {
	              titleDsJson = "\"title\":{\"type\":\"Property\",\"value\":[\"" + title + "\"]},";
	              descrDsJson = "\"description\":{\"type\":\"Property\",\"value\":\"" + descr + "\"},";
	          }

	          String creator = "";
	          if (dataset.getCreator() != null) {
	            creator = firstValue(dataset.getCreator().getName());
	          }
	          String publisher = "";
	          if (dataset.getPublisher() != null) {
	            publisher = firstValue(dataset.getPublisher().getName());
	          }
	          
	          ArrayList<String> contacts = new ArrayList<String>();
	          for (int i = 0; i < dataset.getContactPoint().size(); i++) {
	        	VcardOrganization v = dataset.getContactPoint().get(i);
	            contacts.add("\"" + v.getHasEmail().getValue() + "\""); 
	          }
//	          if (contacts.size() == 0)
//	        	  contacts.add("");
	          
	          ArrayList<String> themes = new ArrayList<String>();
	          List<SkosConceptTheme> theme = dataset.getTheme();
	          for(SkosConceptTheme tema: theme) {
	            List<SkosPrefLabel> lab = tema.getPrefLabel();
	            for (SkosPrefLabel label: lab)
	            	if(label.getValue() != "") {
	            		themes.add("\"" + label.getValue() + "\"");
	            	}
	          }
	          if (themes.size() == 0)
	        	  themes.add("");
	          
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
	          ArrayList<String> documentation = new ArrayList<String>();
	          for (DcatProperty doc: dataset.getDocumentation()) {
	          	if(doc.getValue() != "")
	          		documentation.add("\"" + doc.getValue() + "\"");
	          }

	          ArrayList<String> language = new ArrayList<String>();
	          for (DcatProperty lan: dataset.getLanguage()) {
	          	if(lan.getValue() != "")
	          		language.add("\"" + lan.getValue() + "\"");
	          }
	          ArrayList<String> otherIdentifier = new ArrayList<String>();
	          for (DcatProperty otId: dataset.getOtherIdentifier()) {
	          	if(otId.getValue() != "") {
	          		otherIdentifier.add("\"" + otId.getValue() + "\"");
	          	}
	          }
	          ArrayList<String> provenance = new ArrayList<String>();
	          for (DcatProperty pr: dataset.getProvenance()) {
	          	if(pr.getValue() != "")
	          		provenance.add("\"" + pr.getValue() + "\"");
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
	          ArrayList<String> versionNotes = new ArrayList<String>();
	          for (DcatProperty verNotes: dataset.getVersionNotes()) {
	          	if(verNotes.getValue() != "")
	          		versionNotes.add("\"" + verNotes.getValue() + "\"");
	          }

	          // dateCreated è la creazione della Entity
	          // dateModified Timestamp of the last modification of the entity. 
	          // This will usually be allocated by the storage platform
	   
	          String temporalJson = "";
	          if (!startDate.isEmpty() || !endDate.isEmpty()) {
	              temporalJson = "\"temporal\":{\"type\":\"Property\",\"value\":[{\"@type\":\"DateTime\",\"@value\":\"" + startDate + "\"},{\"@type\":\"DateTime\",\"@value\":\"" + endDate + "\"}]}, ";
	          }
	          String dateCreatedJson = "";
	          if (dataset.getReleaseDate() != null && dataset.getReleaseDate().getValue() != null && !dataset.getReleaseDate().getValue().isEmpty()) {
	              dateCreatedJson = "\"dateCreated\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\"" + dataset.getReleaseDate().getValue() + "\"}}, ";
	          }
	          String dateModifiedJson = "";
	          if (dataset.getUpdateDate() != null && dataset.getUpdateDate().getValue() != null && !dataset.getUpdateDate().getValue().isEmpty()) {
	              dateModifiedJson = "\"dateModified\":{\"type\":\"Property\",\"value\":{\"@type\":\"DateTime\",\"@value\":\"" + dataset.getUpdateDate().getValue() + "\"}}, ";
	          }
	          String typeDs = "Dataset";
	          String dataDs = "{ \"id\": \"" + idDs + "\", \"type\": \"" + typeDs + "\","
	              + descrDsJson
	              + "\"alternateName\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"\" },"
	              + titleDsJson
	              + "\"landingPage\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": [ \"" + dataset.getLandingPage().getValue() + "\" ]" 
	              + " },"
	              + "\"datasetDistribution\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + allDistributions.toString() 
	              + " },"
	              + "\"contactPoint\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + contacts.toString() 		
	              + " },"
	              + keywordDsJson
	              + "\"theme\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + themes.toString() 			
	              + " },"
	              + "\"documentation\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + documentation.toString()    
	              + " },"
//	              + "\"hasVersion\": { "
//	              + "\"type\": \"Property\","
//	              + "\"value\": " + hasVersion.toString()    	
//	              + " },"
	              + "\"language\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + language.toString()    
	              + " },"
	              + "\"otherIdentifier\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + otherIdentifier.toString()    
	              + " },"
	              + "\"provenance\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + provenance.toString()    
	              + " },"
	              + "\"versionNotes\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": " + versionNotes.toString()    
	              + " },"
	              + "\"version\": { "
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + version + "\" },"
	              + temporalJson
	              + "\"accessRights\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + dataset.getAccessRights().getValue() + "\" },"
	              + dateCreatedJson
	              + dateModifiedJson
	              + "\"publisher\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + publisher + "\" },"
	              + "\"creator\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + creator + "\" },"
	              + "\"frequency\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + frequency + "\" },"		
	              + "\"source\": { " 
	              + "\"type\": \"Property\","
	              + "\"value\": \"" + dataset.getLandingPage().getValue() + "\" }"		// ricontrolla
	              + " }";
	          
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
	        String des = node.getDescription();
	        String description = des.replaceAll("[^a-zA-Z0-9]", " ");
	        
	        String n = node.getName();
	        String name = n.replaceAll("[^a-zA-Z0-9]", " ");
	      
	        String type = "CatalogueDCAT-AP";
	        String data = "{ \"id\": \"" + id + "\", \"type\": \"" + type + "\","
	            + "\"description\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"" + description + "\" }," 
	            + "\"publisher\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"" + node.getPublisherName() + "\" }," 
	            + "\"title\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": [ \"" + name + "\" ]" 
	            + " }, "
	            + "\"name\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"Catalogue\" },"
	            + "\"alternateName\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"\" },"
	            + "\"homepage\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"" + node.getHomepage() + "\" },"
	            + "\"dataProvider\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"\" },"
	            + "\"source\": { " 
	            + "\"type\": \"Property\","
	            + "\"value\": \"" + node.getHost() + "\" },"        // rincontrolla
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
	            + "\"object\": " + allDatasets.toString()             
	            + " }, "
	            + "\"language\": { "
	            + "\"type\": \"Property\","
	            + "\"value\": [ \"" + node.getCountry() + "\" ]" 
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
	            + "\"value\": \"" + agent + "\" }" 
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
	                + "\"name\": { \"type\": \"Property\", \"value\": \"" + creatorName + "\" },"
	                + "\"agentType\": { \"type\": \"Property\", \"value\": \"" + creatorType + "\" }"
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
	    String normalizedBaseUrl = normalizeContextBrokerBaseUrl(rawContextBrokerUrl);
	    return normalizedBaseUrl + "/ngsi-ld/v1/";
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
