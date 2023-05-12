package it.eng.ngsild.broker.manager.controller;



import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;



//import io.swagger.v3.oas.annotations.responses.ApiResponses;

//import it.eng.ngsild.broker.manager.model.AgentDcatAp;
//import it.eng.ngsild.broker.manager.model.CatalogueDcatAp;
//import it.eng.ngsild.broker.manager.model.CatalogueRecordDcatAp;
//import it.eng.ngsild.broker.manager.model.DataServiceDcatAp;
import it.eng.ngsild.broker.manager.model.Dataset;
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
    public ResponseEntity<?> createDcatAp(@RequestBody String dataset) {
		System.out.println(dataset);
    	Dataset datasetNgsi = new Dataset();
    	ObjectMapper map = new ObjectMapper();  
    	JsonNode node = null;
    	
    	String contexBrokerEndpoint = "http://" + hostContextBroker + ":" + portContextBroker + "/ngsi-ld/v1/entities";
		try {
			node = map.readTree(dataset);
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
public List<Object> getAllEscalator() {
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


    

    
    
    
   
}