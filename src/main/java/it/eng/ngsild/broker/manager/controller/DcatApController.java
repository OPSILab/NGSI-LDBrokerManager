package it.eng.ngsild.broker.manager.controller;



import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.jena.atlas.json.JSON;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import it.eng.ngsild.broker.manager.model.AgentDcatAp;
import it.eng.ngsild.broker.manager.model.CatalogueDcatAp;
import it.eng.ngsild.broker.manager.model.CatalogueRecordDcatAp;
import it.eng.ngsild.broker.manager.model.DataServiceDcatAp;

//import io.swagger.v3.oas.annotations.responses.ApiResponses;

//import it.eng.ngsild.broker.manager.model.AgentDcatAp;
//import it.eng.ngsild.broker.manager.model.CatalogueDcatAp;
//import it.eng.ngsild.broker.manager.model.CatalogueRecordDcatAp;
//import it.eng.ngsild.broker.manager.model.DataServiceDcatAp;
import it.eng.ngsild.broker.manager.model.Dataset;
import it.eng.ngsild.broker.manager.model.DistributionDcatAp;
//import it.eng.ngsild.broker.manager.model.DistributionDcatAp;


//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;

//import javax.validation.constraints.*;

//import java.util.List;
//import java.util.Map;


//controller per esporre l'API per la conversione e l'invio dell'oggetto DCAT-AP:
@RestController
@RequestMapping("/api")
public class DcatApController {
	@Value("${contexBroker.host_orion}")
	private String hostContextBroker;
	
	@Value("${contexBroker.port_orion}")
	private String portContextBroker;
	
	@Autowired
	RestTemplate restTemplate;

	
    //API POST
	@RequestMapping(value = "/dataset", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new dataset into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
            content = @io.swagger.v3.oas.annotations.media.Content (examples = {
                    @ExampleObject(value="{  \r\n"
                    		+ "  \"id\": \"dataset1\",  \r\n"
                    		+ "  \"dateCreated\": \"1985-07-20T10:08:50Z\",  \r\n"
                    		+ "  \"dateModified\": \"2015-07-13T03:09:32Z\",  \r\n"
                    		+ "  \"source\": \"urn:ngsi-ld:Dataset:items:YSWN:41266715\",  \r\n"
                    		+ "  \"name\": \"First table field check. Agency writer size. Meeting nice nothing after ever.\",  \r\n"
                    		+ "  \"alternateName\": \"Apply popular what suddenly environmental at system. Situation son future example task. Machine year positive security better.\",  \r\n"
                    		+ "  \"description\": \"Own fast suffer your. Spend per police. Less skill much run letter shoulder know office. Discuss of director enter process world possible out.\",  \r\n"
                    		+ "  \"dataProvider\": \"Investment five beat become resource individual assume. Yard seat memory bed forget heart crime.\",  \r\n"
                    		+ "  \"owner\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:QZHN:39684072\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:LADQ:07842317\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"seeAlso\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:JGFW:76050330\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:XUMS:21710022\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"type\": \"typeExample\",  \r\n"
                    		+ "  \"datasetDescription\": [  \r\n"
                    		+ "    \"Sit worry pay during TV increase family. Social drop organization method. Fact treatment throw detail.\",  \r\n"
                    		+ "    \"Experience similar officer social us item lay prepare. Price year close better.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"title\":  \"Class skill deal there no language himself. After rule mouth tell economy risk. Glass personal person center.\",\r\n"
                    		+ "  \"contactPoint\": [  \r\n"
                    		+ "    \"Minute write his experience similar right.\",  \r\n"
                    		+ "    \"Experience away remain.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"datasetDistribution\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:KJVK:30944451\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:MMWU:84196227\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"keyword\": [  \r\n"
                    		+ "    \"Free analysis reduce. Owner Republican institution six science a usually. Value land executive design.\",  \r\n"
                    		+ "    \"Bag recently might far plan nearly scene example. Trouble official dream author job claim join different. Success full debate here check attorney size.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"publisher\": \"Statement which consumer product thought total. Nothing concern picture involve paper nor kid.\",  \r\n"
                    		+ "  \"spatial\": [  \r\n"
                    		+ "    {  \r\n"
                    		+ "      \"type\": \"Point\",  \r\n"
                    		+ "      \"coordinates\": [  \r\n"
                    		+ "          \r\n"
                    		+ "        109.478534,\r\n"
                    		+ "        9.922458 \r\n"
                    		+ "      ]  \r\n"
                    		+ "    }  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"temporal\": [  \r\n"
                    		+ "    \"2017-04-03T02:35:57Z\",  \r\n"
                    		+ "    \"1978-06-15T04:39:05Z\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"theme\": [  \r\n"
                    		+ "    \"Win catch job number find number. Leader reason top arrive night. Movement expect security high hair whom three yeah.\",  \r\n"
                    		+ "    \"Respond character continue gun. Grow best choice group manage over find.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"accessRights\": \"non-public\",  \r\n"
                    		+ "  \"creator\": \"Wall true factor several nothing. Mission want kind design. Who cause health father director either cause.\",  \r\n"
                    		+ "  \"documentation\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:EDTJ:28919577\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:GKJO:30040605\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"frequency\": \"Case fine feel that. Government executive issue police chance believe.\",  \r\n"
                    		+ "  \"hasVersion\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:SQSB:90831182\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:FFVZ:69502935\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"identifier\": \"urn:ngsi-ld:Dataset:items:MBNQ:57176010\",  \r\n"
                    		+ "  \"isReferencedBy\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:YQRP:33454193\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:RBND:48628164\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"isVersionOf\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:AMAC:16896252\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:IPSO:04920226\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"landingPage\": \"urn:ngsi-ld:Dataset:items:UMBA:72418275\",  \r\n"
                    		+ "  \r\n"
                    		+ "  \"language\": [  \r\n"
                    		+ "    \"Environment site ability night player. Head able American example call again.\",  \r\n"
                    		+ "    \"Receive my risk leave matter prepare. Worker admit draw others remember establish necessary one.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"otherIdentifier\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:ZNYR:18053145\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:ICBO:96194869\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"provenance\": [  \r\n"
                    		+ "    \"Air success movie nation attention. Fight do natural brother street.\",  \r\n"
                    		+ "    \"Future against sing especially answer sea. Difference effect company.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"qualifiedAttribution\": [  \r\n"
                    		+ "    \"Central born manage evidence data. Answer doctor visit ready physical fact. Quite allow however certain lose heart.\",  \r\n"
                    		+ "    \"Home interesting range ever. Magazine the instead particularly. Late have collection.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"qualifiedRelation\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:ITFK:67369057\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:ZJWX:10596189\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"relatedResource\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:FXEY:35067714\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:YYOL:47950545\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"releaseDate\": \"1983-07-16T12:51:26Z\",  \r\n"
                    		+ "  \r\n"
                    		+ "  \"sample\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:QJPZ:50290394\",  \r\n"
                    		+ "    \"urn:ngsi-ld:Dataset:items:ZSSA:73451152\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"spatialResolution\": [  \r\n"
                    		+ "    864.6,  \r\n"
                    		+ "    864.6  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"temporalResolution\": [  \r\n"
                    		+ "    864.6,  \r\n"
                    		+ "    864.6  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"datasetType\": \"Else memory if. Whose group through despite cause. Sense peace economy travel.\",  \r\n"
                    		+ "  \"updateDate\": \"2017-12-27T03:37:52Z\",  \r\n"
                    		+ "  \"version\": \"Financial role together range. Nice government first policy daughter need kind. Employee source nature add rest human station. Ability management test during foot that course nothing.\",  \r\n"
                    		+ "  \"versionNotes\": [  \r\n"
                    		+ "    \"Sort language ball floor. Your majority feeling fact by four two.\",  \r\n"
                    		+ "    \"Natural explain before something first drug contain start. Party prevent live.\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"wasGeneratedBy\": [  \r\n"
                    		+ "    \"Theory type successful together. Raise study modern miss dog Democrat quickly.\",  \r\n"
                    		+ "    \"Every manage political record word group food break. Picture suddenly drug rule bring determine some forward. Beyond chair recently and.\"  \r\n"
                    		+ "  ]  \r\n"
                    		+ "}  "),
                  
            })))
    public ResponseEntity<?> createDcatAp( @RequestBody JsonNode dataset ) {
		//System.out.println(dataset);
    	Dataset datasetNgsi = new Dataset();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			try {
				node = map.readTree(dataset.toString());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String entityString = datasetNgsi.convertToNgsi(node);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		}
    }
	@RequestMapping(value = "/agentdcatap", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new agent into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
            content = @io.swagger.v3.oas.annotations.media.Content (examples = {
                    @ExampleObject(value="\r\n"
                    		+ "{  \r\n"
                    		+ "  \"id\": \"provapluto2\",   \r\n"
                    		+ "  \"dateCreated\": \"1988-07-01T14:50:52Z\",  \r\n"
                    		+ "  \"dateModified\": \"2000-06-02T13:25:42Z\",  \r\n"
                    		+ "  \"source\": \"Any source for an Agent.\",  \r\n"
                    		+ "  \"name\": \"Agent 10.\",  \r\n"
                    		+ "  \"alternateName\": \"Agent-10.\",  \r\n"
                    		+ "  \"description\": \"organization the Agent 10 belongs to.\",  \r\n"
                    		+ "  \"dataProvider\": \"\",  \r\n"
                    		+ "  \"owner\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:Agent:TBSV:39232621\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"seeAlso\": [  \r\n"
                    		+ "    \"urn:ngsi-ld:AgentAECY:13995407\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"location\": {  \r\n"
                    		+ "    \"type\": \"Point\",  \r\n"
                    		+ "    \"coordinates\": [  \r\n"
                    		+ "      12.934074,  \r\n"
                    		+ "      -149.532943  \r\n"
                    		+ "    ]  \r\n"
                    		+ "  },  \r\n"
                    		+ "\"address\": {\r\n"
                    		+ "       \r\n"
                    		+ "            \"streetAddress\": \"Straße des 17. Juni\",\r\n"
                    		+ "            \"addressRegion\": \"Berlin\",\r\n"
                    		+ "            \"addressLocality\": \"Tiergarten\",\r\n"
                    		+ "            \"postalCode\": \"10557\"\r\n"
                    		+ "        \r\n"
                    		+ "    },  \r\n"
                    		+ "  \"agentName\": [  \r\n"
                    		+ "    \"Agent 10\",  \r\n"
                    		+ "    \"Agente 10\"  \r\n"
                    		+ "  ],  \r\n"
                    		+ "  \"agentType\": \"EU Publications office\" \r\n"
                    		+ "\r\n"
                    		+ "}  "),
                    
              })))
    public ResponseEntity<?> createAgent(@RequestBody JsonNode agent) {
    	AgentDcatAp agentNgsi = new AgentDcatAp();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(agent.toString());
			String entityString = agentNgsi.convertToNgsi(node);
			System.out.println(entityString);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	@RequestMapping(value = "/cataloguedcatap", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new catalogue into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
    content = @io.swagger.v3.oas.annotations.media.Content (examples = {
            @ExampleObject(value="{  \r\n"
            		+ "  \"id\": \"cat1\",  \r\n"
            		+ "  \"dateCreated\": \"1980-03-03T10:01:24Z\",  \r\n"
            		+ "  \"dateModified\": \"1987-12-04T10:44:40Z\",  \r\n"
            		+ "  \"source\": \"\",  \r\n"
            		+ "  \"name\": \"Catalogue\",  \r\n"
            		+ "  \"alternateName\": \"\",  \r\n"
            		+ "  \"description\": \"Interesting art recently book girl yard represent book. Garden style wish blood your ground size.\",  \r\n"
            		+ "  \"dataProvider\": \"european open data portal\",  \r\n"
            		+ "  \"owner\": [  \r\n"
            		+ "    \"urn:ngsi-ld:Catalogue:ZYKY:89462950\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"seeAlso\": [  \r\n"
            		+ "    \"urn:ngsi-ld:Catalogue:ILBA:60770941\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"location\": {  \r\n"
            		+ "    \"type\": \"Point\",  \r\n"
            		+ "    \"coordinates\": [  \r\n"
            		+ "      -83.400987,  \r\n"
            		+ "      0.152532  \r\n"
            		+ "    ]  \r\n"
            		+ "  },  \r\n"
            		+ "  \"address\": {  \r\n"
            		+ "    \"streetAddress\": \"2 Rue Mercier\",  \r\n"
            		+ "    \"addressLocality\": \"Luxembourg\",  \r\n"
            		+ "    \"addressRegion\": \"Luxembourg\",  \r\n"
            		+ "    \"addressCountry\": \"Luxembourg\",  \r\n"
            		+ "    \"postalCode\": \"2985 \",  \r\n"
            		+ "    \"postOfficeBoxNumber\": \"\",  \r\n"
            		+ "    \"areaServed\": \"European Union\"  \r\n"
            		+ "  },  \r\n"
            		+ "  \"dataset\": [  \r\n"
            		+ "      \"urn:ngsi-ld:Dataset:id:iddataset1\",\r\n"
            		+ "    \"urn:ngsi-ld:Dataset:id:iddataset2\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"publisher\": \"Spanish data portal\",  \r\n"
            		+ "  \"title\": [  \r\n"
            		+ "    \"title first\",  \r\n"
            		+ "    \"Secondary title.\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"homepage\": \"ngsi-ld:Catalogue:homepage:ZFAW:13633782\",  \r\n"
            		+ "  \"language\": [  \r\n"
            		+ "    \"ES\",  \r\n"
            		+ "    \"DE\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"licence\": \"Creative Commons 3.0 International\",  \r\n"
            		+ "  \"releaseDate\": \"2004-08-22T22:32:47Z\",  \r\n"
            		+ "  \"spatial_geographic\": [  \r\n"
            		+ "    {  \r\n"
            		+ "      \"type\": \"Point\",  \r\n"
            		+ "      \"coordinates\": [  \r\n"
            		+ "        57.234944,  \r\n"
            		+ "        52.840273  \r\n"
            		+ "      ]  \r\n"
            		+ "    }  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"themes\": [  \r\n"
            		+ "    \"Want couple him finally responsibility begin. Coach join down new major. Happy yard letter then return member.\",  \r\n"
            		+ "    \"Politics road two question offer white. Recognize fight keep blue person create be. Radio edge or improve less special future. Itself detail computer exist.\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"modificationDate\": \"1982-09-02T03:16:28Z\",  \r\n"
            		+ "  \"hasPart\": [  \r\n"
            		+ "    \"urn:ngsi-ld:Catalogue:hasPart:GVZM:66676591\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"isPartOf\": \"urn:ngsi-ld:Catalogue:isPartOf:NXBZ:88517287\",  \r\n"
            		+ "  \"record\": [  \r\n"
            		+ "    \"Catalogue.items.HLGA.73285516\",  \r\n"
            		+ "    \"Catalogue.items.IHOB.85266800\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"rights\": \"\",  \r\n"
            		+ "  \"catalogue\": [\r\n"
            		+ "      \"urn:ngsi-ld:Catalogue:items:LZMQ:44249979\",\r\n"
            		+ "  \"urn:ngsi-ld:Catalogue:items:PECX:02526105\"\r\n"
            		+ "  ],  \r\n"
            		+ "  \"creator\": \"Role fact sport shoulder blue direction probably order.\"  \r\n"
            		+ "}  "),
            
      })))
    public ResponseEntity<?> createCataloguedcatap(@RequestBody JsonNode agent) {
    	CatalogueDcatAp catalogueNgsi = new CatalogueDcatAp();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(agent.toString());
			String entityString = catalogueNgsi.convertToNgsi(node);
			System.out.println(entityString);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			
			
			
			
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	@RequestMapping(value = "/cataloguerecorddcatap", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new catalogueRecord into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
    content = @io.swagger.v3.oas.annotations.media.Content (examples = {
            @ExampleObject(value="{  \r\n"
            		+ "  \"id\": \"KFTL:88140679\",    \r\n"
            		+ "  \"dateCreated\": \"2020-11-02T21:25:54Z\",  \r\n"
            		+ "  \"dateModified\": \"2021-07-02T18:37:55Z\",  \r\n"
            		+ "  \"source\": \"\",  \r\n"
            		+ "  \"name\": \"\",  \r\n"
            		+ "  \"alternateName\": \"\",  \r\n"
            		+ "  \"description\": \"Catalogue record of the solar system open data portal\",  \r\n"
            		+ "  \"dataProvider\": \"european open data portal\",  \r\n"
            		+ "  \"owner\": [  \r\n"
            		+ "    \"urn:ngsi-ld:CatalogueRecordDCAT-AP:items:ISXP:07320625\",  \r\n"
            		+ "    \"urn:ngsi-ld:CatalogueRecordDCAT-AP:items:BQMW:23610768\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"seeAlso\": [  \r\n"
            		+ "    \"urn:ngsi-ld:CatalogueRecordDCAT-AP:items:FVCU:03753474\",  \r\n"
            		+ "    \"urn:ngsi-ld:CatalogueRecordDCAT-AP:items:AIEC:73224831\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"location\": {  \r\n"
            		+ "    \"type\": \"Point\",  \r\n"
            		+ "    \"coordinates\": [  \r\n"
            		+ "      36.633152,  \r\n"
            		+ "      -85.183315  \r\n"
            		+ "    ]  \r\n"
            		+ "  },  \r\n"
            		+ "  \"address\": {  \r\n"
            		+ "    \"streetAddress\": \"2, rue Mercier\",  \r\n"
            		+ "    \"addressLocality\": \"Luxembourg\",  \r\n"
            		+ "    \"addressRegion\": \"Luxembourg\",  \r\n"
            		+ "    \"addressCountry\": \"Luxembourg\",  \r\n"
            		+ "    \"postalCode\": \"2985\",  \r\n"
            		+ "    \"postOfficeBoxNumber\": \"\"  \r\n"
            		+ "  },  \r\n"
            		+ "  \"areaServed\": \"European Union and beyond\",  \r\n"
            		+ "  \"primaryTopic\": \"Public administration\",  \r\n"
            		+ "  \"modificationDate\": \"2021-07-02T18:37:55Z\",  \r\n"
            		+ "  \"applicationProfile\": \"DCAT Application profile for data portals in Europe\",  \r\n"
            		+ "  \"changeType\": \"First version\",  \r\n"
            		+ "  \"listingDate\": \"2021-07-02T18:37:55Z\",  \r\n"
            		+ "  \"language\": [  \r\n"
            		+ "    \"EN\",  \r\n"
            		+ "    \"ES\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"sourceMetadata\": \"\",  \r\n"
            		+ "  \"title\": [  \r\n"
            		+ "    \"Example of catalogue record\",  \r\n"
            		+ "    \"Ejemplo de registro de catálogo\"  \r\n"
            		+ "  ]  \r\n"
            		+ "}  "),
            
      })))
    public ResponseEntity<?> createCatalogueRecordDcatap(@RequestBody JsonNode agent) {
		CatalogueRecordDcatAp catalogueNgsi = new CatalogueRecordDcatAp();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(agent.toString());
			String entityString = catalogueNgsi.convertToNgsi(node);
			System.out.println(entityString);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	@RequestMapping(value = "/dataservicedcatap", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new DataService into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
    content = @io.swagger.v3.oas.annotations.media.Content (examples = {
            @ExampleObject(value="{  \r\n"
            		+ "  \"id\": \"u56257192\",  \r\n"
            		+ "   \r\n"
            		+ "  \"accessRights\": \"No restrictions to access the data but APi requests limit, 5000 requests per hour\",  \r\n"
            		+ "  \"address\": {  \r\n"
            		+ "    \"addressCountry\": \"Luxembourg\",  \r\n"
            		+ "    \"addressLocality\": \"Luxembourg\",  \r\n"
            		+ "    \"addressRegion\": \"Luxembourg\",  \r\n"
            		+ "    \"postOfficeBoxNumber\": \"\",  \r\n"
            		+ "    \"postalCode\": \"2985\",  \r\n"
            		+ "    \"streetAddress\": \"2, rue Mercier\"  \r\n"
            		+ "  },  \r\n"
            		+ "  \"alternateName\": \"\",  \r\n"
            		+ "  \"areaServed\": \"European union and beyond\",  \r\n"
            		+ "  \"dataProvider\": \"European open data portal\",  \r\n"
            		+ "  \"dataServiceDescription\": [  \r\n"
            		+ "    \"Digital resources for accessing to the end points of the EU open data portal for solar system.\",  \r\n"
            		+ "    \"Recursos digitales para el acceso a los puntos de interaccion del portal europeo de datos abiertos del sistema solar.\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"dateCreated\": \"2020-10-28T04:19:29Z\",  \r\n"
            		+ "  \"dateModified\": \"2021-10-06T16:31:26Z\",  \r\n"
            		+ "  \"description\": \"Data service for the solar system open data portal.\",  \r\n"
            		+ "  \"endPointDescription\": [  \r\n"
            		+ "    \"SPARQL end point without authentication\",  \r\n"
            		+ "    \"API compliant with CKAN specification\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"endPointURL\": [  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:AFGI:79071729\",  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:JAZP:97999812\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"license\": \"EUPL.\",  \r\n"
            		+ "  \"location\": {  \r\n"
            		+ "    \"coordinates\": [  \r\n"
            		+ "      72.564509,  \r\n"
            		+ "      11.125289  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"type\": \"Point\"  \r\n"
            		+ "  },  \r\n"
            		+ "  \"name\": \"\",  \r\n"
            		+ "  \"owner\": [  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:HGSY:92686457\",  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:JCJR:29622597\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"seeAlso\": [  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:JDKD:53476147\",  \r\n"
            		+ "    \"urn:ngsi-ld:DataServiceDCAT-AP:items:XVJQ:09725114\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"servesDataset\": [  \r\n"
            		+ "    \"EU geographic map\",  \r\n"
            		+ "    \"EU physical map\"  \r\n"
            		+ "  ],  \r\n"
            		+ "  \"source\": \"\",  \r\n"
            		+ "  \"title\": [  \r\n"
            		+ "    \"Data service of the european open data portal\",  \r\n"
            		+ "    \"Data service del portal europeo de datos abiertos\"  \r\n"
            		+ "  ]  \r\n"
            		+ "}  "),
            
      })))
    public ResponseEntity<?> createDataServiceDcatAp(@RequestBody JsonNode agent) {
		DataServiceDcatAp dataserviceNgsi = new DataServiceDcatAp();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(agent.toString());
			String entityString = dataserviceNgsi.convertToNgsi(node);
			System.out.println(entityString);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
	@RequestMapping(value = "/distributiondcatap", method = RequestMethod.POST, consumes="application/json")
	@Operation(summary = "Create a new distribution into CB", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request examples",
    content = @io.swagger.v3.oas.annotations.media.Content (examples = {
            @ExampleObject(value="\r\n"
            		+ "{  \r\n"
            		+ "    \"id\": \"KJVK:30944451\",   \r\n"
            		+ "    \"accessService\": [  \r\n"
            		+ "        \"\"  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"accessUrl\": [  \r\n"
            		+ "        \"\"  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"address\": {  \r\n"
            		+ "        \"addressCountry\": \"Luxembourg\",  \r\n"
            		+ "        \"addressLocality\": \"Luxembourg\",  \r\n"
            		+ "        \"addressRegion\": \"Luxembourg\",  \r\n"
            		+ "        \"postOfficeBoxNumber\": \"\",  \r\n"
            		+ "        \"postalCode\": \"24004\",  \r\n"
            		+ "        \"streetAddress\": \"Luxembourg platz 2\"  \r\n"
            		+ "    },  \r\n"
            		+ "    \"alternateName\": \"csv\",  \r\n"
            		+ "    \"areaServed\": \"European Union.\",  \r\n"
            		+ "    \"availability\": \"yes\",  \r\n"
            		+ "    \"byteSize\": 43503,  \r\n"
            		+ "    \"checksum\": \"H3FR.\",  \r\n"
            		+ "    \"compressionFormat\": \"\",  \r\n"
            		+ "    \"dataProvider\": \"Meloda.org\",  \r\n"
            		+ "    \"dateCreated\": \"1993-08-16T05:35:56Z\",  \r\n"
            		+ "    \"dateModified\": \"1970-07-14T10:48:19Z\",  \r\n"
            		+ "    \"description\": \"Distribution of open data portals in csv\",  \r\n"
            		+ "    \"documentation\": [],  \r\n"
            		+ "    \"downloadURL\": \"urn:ngsi-ld:DistributionDCAT-AP:items:ICPI:96947751\",  \r\n"
            		+ "    \"format\": \" text/csv\",  \r\n"
            		+ "    \"hasPolicy\": \"Open data policy.\",  \r\n"
            		+ "    \"language\": [  \r\n"
            		+ "        \"EN\",  \r\n"
            		+ "        \"ES\"  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"license\": \"CC-BY\",  \r\n"
            		+ "    \"linkedSchemas\": [],  \r\n"
            		+ "    \"location\": {  \r\n"
            		+ "        \"coordinates\": [  \r\n"
            		+ "            -67.057831,  \r\n"
            		+ "            67.968509  \r\n"
            		+ "        ],  \r\n"
            		+ "        \"type\": \"Point\"  \r\n"
            		+ "    },  \r\n"
            		+ "    \"mediaType\": \"\",  \r\n"
            		+ "    \"modifiedDate\": \"1986-03-28T19:56:43Z\",  \r\n"
            		+ "    \"name\": \"csv portals distribution\",  \r\n"
            		+ "    \"owner\": [  \r\n"
            		+ "        \"urn:ngsi-ld:DistributionDCAT-AP:items:HZAC:24935175\",  \r\n"
            		+ "        \"urn:ngsi-ld:DistributionDCAT-AP:items:AQGQ:50019342\"  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"packagingFormat\": \"zip\",  \r\n"
            		+ "    \"releaseDate\": \"1997-05-06T05:04:10Z\",  \r\n"
            		+ "    \"rights\": \"copyleft\",  \r\n"
            		+ "    \"seeAlso\": [  \r\n"
            		+ "        \"urn:ngsi-ld:DistributionDCAT-AP:items:TYQY:03354957\",  \r\n"
            		+ "        \"urn:ngsi-ld:DistributionDCAT-AP:items:VZQW:12690544\"  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"source\": \"\",  \r\n"
            		+ "    \"spatialResolution\": [  \r\n"
            		+ "        0.5,  \r\n"
            		+ "        0.5  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"status\": \"Withdrawn\",  \r\n"
            		+ "    \"temporalResolution\": [  \r\n"
            		+ "        2,  \r\n"
            		+ "        10  \r\n"
            		+ "    ],  \r\n"
            		+ "    \"title\":\"Dataset base\",\r\n"
            		+ "      \r\n"
            		+ "    \"@context\": [  \r\n"
            		+ "        \"https://raw.githubusercontent.com/smart-data-models/dataModel.DCAT-AP/master/context.jsonld\"  \r\n"
            		+ "    ]  \r\n"
            		+ "}  "),
            
      })))
    public ResponseEntity<?> createDistributionDcatap(@RequestBody JsonNode agent) {
		DistributionDcatAp distributionNgsi = new DistributionDcatAp();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(agent.toString());
			String entityString = distributionNgsi.convertToNgsi(node);
			System.out.println(entityString);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		
			final HttpEntity<String> entity = new HttpEntity<String>(entityString, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(contexBrokerEndpoint + "/", entity, String.class);
		     return new ResponseEntity<String> (response.toString(), null, response.getStatusCode());
		} catch (HttpClientErrorException e) {
			System.out.println(e.getMessage());
			// handle exception here
			return new ResponseEntity<String> (e.getMessage(), null, e.getStatusCode());
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //@PostMapping("/agentdcatap")
     //public ResponseEntity<AgentDcatAp> createDcatAp(@RequestBody AgentDcatAp agentDcatAp) {
    	//AgentDcatAp convertedDcatAp = converter.convertAgentDcatAp(agentDcatAp);
       // orionClient.sendAgentDcatApToOrion(convertedDcatAp);
    // return ResponseEntity.ok(agentDcatAp);
    // }
      //@PostMapping("/cataloguedcatap")
   // public ResponseEntity<CatalogueDcatAp> createDcatAp(@RequestBody CatalogueDcatAp catalogueDcatAp) {
    	//CatalogueDcatAp convertedDcatAp = converter.convertCatalogueDcatAp(catalogueDcatAp);
    	// orionClient.sendCatalogueDcatApToOrion(convertedDcatAp);
    	// return ResponseEntity.ok(catalogueDcatAp);
    // }
        // @PostMapping("/cataloguerecorddcatap")
    //public ResponseEntity<CatalogueRecordDcatAp> createDcatAp(@RequestBody CatalogueRecordDcatAp catalogueRecordDcatAp) {
    	//CatalogueRecordDcatAp convertedDcatAp = converter.convertCatalogueRecordDcatAp(catalogueRecordDcatAp);
    	//orionClient.sendCatalogueRecordDcatApToOrion(convertedDcatAp);
    	//  return ResponseEntity.ok(catalogueRecordDcatAp);
        // }
    // @PostMapping("/dataservicedcatap")
    // public ResponseEntity<DataServiceDcatAp> createDcatAp(@RequestBody DataServiceDcatAp dataServiceDcatAp) {
    	//DataServiceDcatAp convertedDcatAp = converter.convertDataServiceDcatAp(dataServiceDcatAp);
    	//orionClient.sendDataServiceDcatApToOrion(convertedDcatAp);
    	//  return ResponseEntity.ok(dataServiceDcatAp);
        // }
    // @PostMapping("/distributiondcatap")
     //public ResponseEntity<DistributionDcatAp> createDcatAp(@RequestBody DistributionDcatAp distributionDcatAp) {
    	//DistributionDcatAp convertedDcatAp = converter.convertDistributionDcatAp(distributionDcatAp);
    	//orionClient.sendDistributionDcatApToOrion(convertedDcatAp);
     // return ResponseEntity.ok(distributionDcatAp);
     // }
    
@GetMapping("/dataset")
public List<Object> getAllDataset() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			System.out.println("hostContextBroker: " + hostContextBroker);
			System.out.println("contexBrokerEndpoint: " + contexBrokerEndpoint);
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=Dataset&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		System.out.println(dataset);
		return dataset;
    }
@GetMapping("/cataloguedcatap")
public List<Object> getAllCataloguedcatap() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			System.out.println("hostContextBroker: " + hostContextBroker);
			System.out.println("contexBrokerEndpoint: " + contexBrokerEndpoint);
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=CatalogueDCAT-AP&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		System.out.println(dataset);
		return dataset;
    }
@GetMapping("/cataloguerecorddcatap")
public List<Object> getAllCatalogueRecordDcatap() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			System.out.println("hostContextBroker: " + hostContextBroker);
			System.out.println("contexBrokerEndpoint: " + contexBrokerEndpoint);
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=CatalogueRecordDCAT-AP&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		System.out.println(dataset);
		return dataset;
    }

@GetMapping("/agentdcatap")
public List<Object> getAllAgentdcatap() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=AgentDCAT-AP&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		return dataset;
    }
@GetMapping("/dataservicedcatap")
public List<Object> getAllDataservicedcatap() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=DataServiceDCAT-AP&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		return dataset;
    }

@GetMapping("/distributiondcatap")
public List<Object> getAllDistributiondcatap() {
	  int limit = 20;
		int offset = 0;
		List<Object> dataset= new ArrayList();
	
		String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		ResponseEntity<Object[]> response;
		do {
			response = restTemplate.getForEntity(contexBrokerEndpoint + "?type=DistributionDCAT-AP&options=keyValues&limit=" + limit + "&offset=" + offset , Object[].class);
			List<Object> responseArr = Arrays.asList(response.getBody());
			dataset.addAll(responseArr);
			offset = offset + limit; 
		} while (!Arrays.asList(response.getBody()).isEmpty());
		return dataset;
    }


    

    
    
    
   
}