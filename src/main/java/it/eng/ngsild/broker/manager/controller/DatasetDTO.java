package it.eng.ngsild.broker.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import it.eng.ngsild.broker.manager.model.Dataset;

@Component

public class DatasetDTO {
	
    @Autowired
    private final RestTemplate restTemplate;
   
    private final String orionUrl;

    public DatasetDTO(@Value("localhost:1026/ngsi-ld/v1/entities") String orionUrl) {
        this.restTemplate = new RestTemplate();
        this.orionUrl = orionUrl;
    }
    public Dataset toDatasetOrion() {
	      return null;
    	
    }

    public void sendDatasetToOrion(Dataset dataset) {
    	
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.APPLICATION_JSON);
       
        HttpEntity<Dataset> entity = new HttpEntity<Dataset>(dataset, headers);
        
        ResponseEntity<Dataset> response = restTemplate.postForEntity(orionUrl, entity, Dataset.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Dataset successfully sent to Orion Fiware.");
        } else {
            System.out.println("Failed to send dataset to Orion Fiware. Error code: " + response.getStatusCodeValue());
        }
    }
}


