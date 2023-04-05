package it.eng.ngsild.broker.manager.controller;



import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
	
	
    //API POST
    @PostMapping("/dataset")
    public ResponseEntity<Dataset> createDcatAp(@RequestBody Dataset dataset) {
   	//Dataset convertedDcatAp = converter.convertDatasetDcatAp(dataset);
    // orionClient.sendDatasetDcatApToOrion(convertedDcatAp);
    return ResponseEntity.ok(dataset);
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
    
    

    //API DELETE
    // @DeleteMapping("/dataset/{id}")
    //public ResponseEntity<Void> deleteDcatAp(@PathVariable String id) {
        //orionClient.deleteDatasetFromOrion(id);
    //   return ResponseEntity.noContent().build();
    //  }
    // @DeleteMapping("/agentdcatap/{id}")
    //  public ResponseEntity<Void> deleteAgentDcatAp(@PathVariable String id) {
    	//orionClient.deleteAgentDcatApFromOrion(id);
    // return ResponseEntity.noContent().build();
    //   }
    // @DeleteMapping("/cataloguedcatap/{id}")
    // public ResponseEntity<Void> deleteCatalogueDcatAp(@PathVariable String id) {
    	//orionClient.deleteCatalogueDcatApFromOrion(id);
    //   return ResponseEntity.noContent().build();
    // }
    // @DeleteMapping("/cataloguerecorddcatap/{id}")
    // public ResponseEntity<Void> deleteCatalogueRecordDcatAp(@PathVariable String id) {
    	//orionClient.deleteCatalogueRecordDcatApFromOrion(id);
    //   return ResponseEntity.noContent().build();
    // }
    // @DeleteMapping("/dataservicedcatap/{id}")
    // public ResponseEntity<Void> deleteDataServiceDcatAp(@PathVariable String id) {
    	// orionClient.deleteDataServiceDcatApFromOrion(id);
    //    return ResponseEntity.noContent().build();
    //  }
    // @DeleteMapping("/distributiondcatap/{id}")
    // public ResponseEntity<Void> deleteDistributionDcatAp(@PathVariable String id) {
    	// orionClient.deleteDistributionDcatApFromOrion(id);
    //      return ResponseEntity.noContent().build();
    // }
    

    
    
    
    // //API PUT
    //@PutMapping("/dataset/{id}")
    // public ResponseEntity<Dataset> updateDcatAp(@PathVariable String id, @RequestBody Dataset dataset) {
    	// Dataset convertedDcatAp = converter.convertDataset(dataset);
        //orionClient.updateDatasetInOrion(id, convertedDcatAp);
    //   return ResponseEntity.ok(dataset);
    //}
    // @PutMapping("/agentdcatap/{id}")
    // public ResponseEntity<AgentDcatAp> updateDcatAp(@PathVariable String id, @RequestBody AgentDcatAp agentDcatAp) {
    	//AgentDcatAp convertedDcatAp = converter.convertAgentDcatAp(agentDcatAp);
    	//orionClient.updateAgentDcatApInOrion(id, convertedDcatAp);
    //   return ResponseEntity.ok(agentDcatAp);
        // }
// @PutMapping("/cataloguedcatap/{id}")
    // public ResponseEntity<CatalogueDcatAp> updateDcatAp(@PathVariable String id, @RequestBody CatalogueDcatAp catalogueDcatAp) {
    	//CatalogueDcatAp convertedDcatAp = converter.convertCatalogueDcatAp(catalogueDcatAp);
    	//orionClient.updateCatalogueDcatApInOrion(id, convertedDcatAp);
    	//   return ResponseEntity.ok(catalogueDcatAp);
        // }
    //@PutMapping("/cataloguerecorddcatap/{id}")
    //  public ResponseEntity<CatalogueRecordDcatAp> updateDcatAp(@PathVariable String id, @RequestBody CatalogueRecordDcatAp catalogueRecordDcatAp) {
    	//CatalogueRecordDcatAp convertedDcatAp = converter.convertCatalogueRecordDcatAp(catalogueRecordDcatAp);
    	//orionClient.updateCatalogueRecordDcatApInOrion(id, convertedDcatAp);
    //   return ResponseEntity.ok(catalogueRecordDcatAp);
        //}
        // @PutMapping("/dataservicedcatap/{id}")
    // public ResponseEntity<DataServiceDcatAp> updateDcatAp(@PathVariable String id, @RequestBody DataServiceDcatAp dataServiceDcatAp) {
    	//DataServiceDcatAp convertedDcatAp = converter.convertDataServiceDcatAp(dataServiceDcatAp);
    	//orionClient.updateDataServiceDcatApInOrion(id, convertedDcatAp);
    //   return ResponseEntity.ok(dataServiceDcatAp);
        //}
    // @PutMapping("/distributiondcatap/{id}")
    //public ResponseEntity<DistributionDcatAp> updateDcatAp(@PathVariable String id, @RequestBody DistributionDcatAp distributionDcatAp) {
    	//DistributionDcatAp convertedDcatAp = converter.convertDistributionDcatAp(distributionDcatAp);
    	//orionClient.updateDistributionDcatApInOrion(id, convertedDcatAp);
    //  return ResponseEntity.ok(distributionDcatAp);
    // }
}