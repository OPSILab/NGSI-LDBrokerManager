package it.eng.ngsild.broker.manager.service;

import com.google.gson.Gson;
import it.eng.idra.beans.dcat.*;
import it.eng.idra.beans.odms.OdmsCatalogue;
import java.nio.file.*;
import java.util.*;
import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NgsiDcatMapperTest {
  @Test void catalogueUsesExplicitDcatPredicatesInsteadOfProtectedNgsiCoreAliases() {
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987654);node.setName("Catalogue title");node.setDescription("Catalogue description");
    DcatDataset dataset=new Gson().fromJson("{\"id\":\"namespace-fixture\"}",DcatDataset.class);
    JSONObject catalogue=NgsiDcatMapper.catalogue(node,List.of(dataset)).stream().filter(e->e.getString("type").equals("CatalogueDCAT-AP")).findFirst().get();
    assertEquals("Catalogue title",catalogue.getJSONObject("http://purl.org/dc/terms/title").getString("value"));
    assertEquals("Catalogue description",catalogue.getJSONObject("http://purl.org/dc/terms/description").getString("value"));
    assertEquals("urn:ngsi-ld:Dataset:id:namespace-fixture",catalogue.getJSONObject("http://www.w3.org/ns/dcat#dataset").getJSONArray("object").getString(0));
    assertFalse(catalogue.has("title"));assertFalse(catalogue.has("description"));assertFalse(catalogue.has("dataset"));
  }
  @Test void catalogueUsesSourceMetadataBeforeIdraConfiguration() {
    // Plan50 step 2: the source dcat:Catalog description captured by Idra replaces the configured values in the broker.
    String rdf="@prefix dct: <http://purl.org/dc/terms/> . @prefix foaf: <http://xmlns.com/foaf/0.1/> . @prefix dcat: <http://www.w3.org/ns/dcat#> .\n"
        +"<urn:idra:catalogue:source> a dcat:Catalog ; dct:title \"Titolo\"@it , \"Title\"@en ; dct:description \"Description\"@en ;"
        +" dct:publisher <https://example.org/agent> ; dct:issued \"2024-01-15\"^^<http://www.w3.org/2001/XMLSchema#date> ;"
        +" dct:language <http://publications.europa.eu/resource/authority/language/ITA> ; foaf:homepage <https://example.org/home> ;"
        +" dct:rights [ a dct:RightsStatement ; <http://www.w3.org/2000/01/rdf-schema#label> \"Open\" ] ;"
        +" dct:spatial <https://example.org/location> .\n"
        +"<https://example.org/agent> a foaf:Agent ; foaf:name \"Source publisher\" ; foaf:mbox <mailto:info@example.org> .";
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987655);node.setName("Idra name");node.setDescription("Idra description");
    node.setHomepage("https://idra.example/home");node.setPublisherName("Idra publisher");node.setPublisherUrl("https://idra.example/publisher");
    node.setSourceMetadata(new JSONObject().put("version",1).put("sourceResource","https://example.org/catalog").put("sourceRdf",rdf).toString());
    List<JSONObject> entities=NgsiDcatMapper.catalogue(node,Collections.emptyList());
    Map<String,JSONObject> byId=new HashMap<>();for(JSONObject e:entities)byId.put(e.getString("id"),e);
    JSONObject cat=byId.get("urn:ngsi-ld:Catalogue:id:987655");
    JSONObject title=cat.getJSONObject("http://purl.org/dc/terms/title").getJSONObject("value");
    assertEquals("Titolo",title.getString("it"));assertEquals("Title",title.getString("en"));
    assertEquals("Description",cat.getJSONObject("http://purl.org/dc/terms/description").getJSONObject("value").getString("en"));
    assertEquals("2024-01-15",cat.getJSONObject("http://purl.org/dc/terms/issued").getString("value"));
    assertEquals("http://publications.europa.eu/resource/authority/language/ITA",cat.getJSONObject("http://purl.org/dc/terms/language").getString("value"));
    assertEquals("https://example.org/home",cat.getJSONObject("http://xmlns.com/foaf/0.1/homepage").getString("value"));
    assertFalse(cat.has("homepage"),"the configured homepage is replaced, not duplicated");
    assertEquals("Open",cat.getJSONObject("http://purl.org/dc/terms/rights").getJSONObject("value").getString("http://www.w3.org/2000/01/rdf-schema#label"));
    assertFalse(cat.has("http://purl.org/dc/terms/spatial"),"spatial stays the typed projection");
    assertEquals("https://example.org/catalog",cat.getJSONObject("sourceResource").getString("value"));
    assertTrue(cat.getJSONObject("sourceMetadata").getString("value").contains("Source publisher"),"evidence is kept");
    String publisher=cat.getJSONObject("publisher").getJSONArray("object").getString(0);
    assertEquals("Source publisher",byId.get(publisher).getJSONObject("name").getString("value"));
    assertEquals("mailto:info@example.org",byId.get(publisher).getJSONObject("mbox").getString("value"));
    assertFalse(entities.toString().contains("Idra publisher"),"the configured publisher is not projected beside the source one");
    assertFalse(entities.toString().contains("Idra name"));
    // Without source metadata the configured values are used as before.
    node.setSourceMetadata(null);
    JSONObject configured=NgsiDcatMapper.catalogue(node,Collections.emptyList()).stream().filter(e->e.getString("type").equals("CatalogueDCAT-AP")).findFirst().get();
    assertEquals("Idra name",configured.getJSONObject("http://purl.org/dc/terms/title").getString("value"));
  }
  @Test void dataServiceKeepsDcatAp3StructuredFields() {
    // Plan50 step 3: multilingual title, keywords, themes, profiles and publisher of a data service reach the broker.
    String json="{\"resourceUri\":\"https://example.org/service\",\"title\":{\"value\":\"Servizio\"},"
        +"\"dataServiceDetails\":[{\"title\":\"Servizio\",\"language\":\"it\"},{\"title\":\"Service\",\"description\":\"Description\",\"language\":\"en\"}],"
        +"\"keywordDetails\":[{\"value\":\"api\",\"language\":\"en\"}],\"theme\":[{\"value\":\"https://example.org/theme\"}],"
        +"\"conformsTo\":[{\"value\":\"https://example.org/standard\"}],"
        +"\"publisher\":{\"resourceUri\":\"https://example.org/agent\",\"name\":[{\"value\":\"Service publisher\"}]}}";
    DcatDataService service=new Gson().fromJson(json,DcatDataService.class);
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987657);node.setName("Catalogue");node.setService(List.of(service));
    Map<String,JSONObject> byId=new HashMap<>();for(JSONObject e:NgsiDcatMapper.catalogue(node,Collections.emptyList()))byId.put(e.getString("id"),e);
    JSONObject entity=byId.values().stream().filter(e->e.getString("type").equals("DataServiceDCAT-AP")).findFirst().get();
    JSONObject title=entity.getJSONObject("title").getJSONObject("value");
    assertEquals("Servizio",title.getString("it"));assertEquals("Service",title.getString("en"));
    assertEquals("Description",entity.getJSONObject("description").getJSONObject("value").getString("en"));
    assertEquals("api",entity.getJSONObject("keyword").getJSONObject("value").getJSONArray("en").getString(0));
    assertTrue(entity.getJSONObject("theme").toString().contains("https://example.org/theme"));
    assertTrue(entity.getJSONObject("conformsTo").toString().contains("https://example.org/standard"));
    String agent=entity.getJSONObject("publisher").getString("object");
    assertEquals("Relationship",entity.getJSONObject("publisher").getString("type"));
    assertTrue(byId.get(agent).toString().contains("Service publisher"));
  }
  @Test void serviceReachedTwiceKeepsTheRicherProjection() {
    // The access service of a distribution and the catalogue service share the source IRI: one entity, not overwritten.
    DcatDataService access=new Gson().fromJson("{\"resourceUri\":\"https://example.org/service\",\"title\":{\"value\":\"Service\"},\"conformsTo\":[{\"value\":\"https://example.org/standard\"}]}",DcatDataService.class);
    DcatDataService listed=new Gson().fromJson("{\"resourceUri\":\"https://example.org/service\",\"title\":{\"value\":\"Service\"}}",DcatDataService.class);
    DcatDataset dataset=new Gson().fromJson("{\"id\":\"ds1\",\"distributions\":[{\"id\":\"d1\"}]}",DcatDataset.class);
    dataset.getDistributions().get(0).setAccessService(List.of(access));
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987658);node.setName("Catalogue");node.setService(List.of(listed));
    List<JSONObject> services=new ArrayList<>();
    for(JSONObject e:NgsiDcatMapper.catalogue(node,List.of(dataset)))if(e.getString("type").equals("DataServiceDCAT-AP"))services.add(e);
    assertEquals(1,services.size());
    assertTrue(services.get(0).has("conformsTo"),"the catalogue listing does not overwrite the access service fields");
  }
  @Test void agentNamesKeepTheirLanguage() {
    // Plan50: Idra sends names as strings plus the parallel nameLanguage list; the broker gets a language map.
    JSONObject agent=new JSONObject().put("resourceUri","https://example.org/agent").put("name",new JSONArray().put("Ente").put("Agency"))
        .put("nameLanguage",new JSONArray().put("it").put("en"));
    JSONObject entity=NgsiDcatMapper.fromJson(agent,"urn:test:agent","AgentDCAT-AP");
    JSONObject names=entity.getJSONObject("name").getJSONObject("value");
    assertEquals("Ente",names.getString("it"));assertEquals("Agency",names.getString("en"));
    assertFalse(entity.has("nameLanguage"));
    JSONObject plain=NgsiDcatMapper.fromJson(new JSONObject().put("name",new JSONArray().put("Plain")),"urn:test:plain","AgentDCAT-AP");
    assertEquals("Plain",plain.getJSONObject("name").getJSONArray("value").getString(0),"untagged names are unchanged");
  }
  @Test void licenceWithoutIriHasNoPlaceholder() {
    // Plan50: a licence without IRI must not reach the broker as the dct:license property IRI.
    DctLicenseDocument licence=new DctLicenseDocument(null,"cc-by","","","1");
    assertEquals("",licence.getUri());
    licence.setUri("http://purl.org/dc/terms/license");
    assertEquals("",licence.getUri(),"stored placeholder is cleared");
    JSONObject entity=NgsiDcatMapper.fromJson(new JSONObject(new Gson().toJson(licence)),"urn:test:licence","LicenseDocument");
    // The @context legitimately maps the "license" term to dct:license: only the uri value matters.
    assertFalse(entity.has("uri") && entity.getJSONObject("uri").toString().contains("http://purl.org/dc/terms/license"));
    assertEquals("cc-by",entity.getJSONObject("name").getString("value"));
  }
  @Test void inventedDateDoesNotReachTheBroker() {
    // Plan50: older Idra versions wrote 1970-01-01T00:00:00Z when the source had no date.
    DcatDistribution distribution=new DcatDistribution();
    distribution.setUpdateDate(new DcatProperty(DcatProperty.INVENTED_DATE));
    assertEquals("",distribution.getUpdateDate().getValue());
    JSONObject entity=NgsiDcatMapper.fromJson(new JSONObject(new Gson().toJson(distribution)),"urn:test:distribution","DistributionDCAT-AP");
    assertFalse(entity.toString().contains(DcatProperty.INVENTED_DATE));
  }
  @Test void catalogueRecordKeepsItsSourceRecord() {
    // Plan50 step 3: dct:source of the catalogue record reaches the broker under the dct:source term.
    DcatCatalogueRecord record=new DcatCatalogueRecord();record.setResourceUri("https://example.org/record");
    DcatProperty source=new DcatProperty();source.setValue("https://origin.example/record/7");record.setSource(source);
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987656);node.setName("Catalogue");node.setRecord(List.of(record));
    JSONObject entity=NgsiDcatMapper.catalogue(node,Collections.emptyList()).stream().filter(e->e.getString("type").equals("CatalogueRecordDCAT-AP")).findFirst().get();
    assertEquals("https://origin.example/record/7",entity.getJSONObject("source").getString("value"));
  }
  @Test void catalogueRecordUsesDcatPredicatesAndLocalizedText() {
    JSONObject input=new JSONObject().put("title",new JSONArray().put(new JSONObject().put("title","Primo record").put("description","Descrizione").put("language","it")))
        .put("applicationProfile",new JSONArray().put(new JSONObject().put("uri","https://example.org/profile")))
        .put("listingDate",new JSONObject().put("value","2026-10-22"))
        .put("primaryTopic",new JSONObject().put("value","https://example.org/dataset"))
        .put("changeType",new JSONObject().put("value","https://example.org/status"));
    JSONObject entity=NgsiDcatMapper.fromJson(input,"urn:test:record","CatalogueRecordDCAT-AP");
    assertEquals("Primo record",entity.getJSONObject("title").getJSONObject("value").getString("it"));
    assertEquals("Descrizione",entity.getJSONObject("description").getJSONObject("value").getString("it"));
    assertEquals("https://example.org/profile",entity.getJSONObject("conformsTo").getJSONArray("value").getJSONObject(0).getString("uri"));
    assertEquals("2026-10-22",entity.getJSONObject("dateCreated").getString("value"));
    assertFalse(entity.has("applicationProfile"));assertFalse(entity.has("listingDate"));assertFalse(entity.has("changeType"));
    JSONObject context=entity.getJSONArray("@context").getJSONObject(1);
    assertEquals("http://xmlns.com/foaf/0.1/primaryTopic",context.getString("primaryTopic"));
    assertEquals("http://www.w3.org/ns/adms#status",context.getString("status"));
  }
  @Test void catalogueHttpClientAcceptsIdraScalarPropertiesAndLegacyValueObjects() throws Exception {
    com.fasterxml.jackson.databind.ObjectMapper jackson=org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json().build();
    String payload="{\"id\":987654,\"service\":[{\"resourceUri\":\"https://example.org/api\",\"title\":\"API\",\"endpointURL\":[\"https://example.org/endpoint\"],\"rights\":[\"\"]}],\"datasetSeries\":[{\"resourceUri\":\"https://example.org/series\",\"title\":[{\"title\":\"Serie autonoma\",\"language\":\"it\"}]}]}";
    OdmsCatalogue node=jackson.readValue(payload,OdmsCatalogue.class);
    assertEquals("API",node.getService().get(0).getTitle().getValue());
    assertEquals("https://example.org/endpoint",node.getService().get(0).getEndpointURL().get(0).getValue());
    assertEquals("Serie autonoma",node.getDatasetSeries().get(0).getTitle().get(0).getTitle());
    assertEquals("legacy",jackson.readValue("{\"value\":\"legacy\"}",DcatProperty.class).getValue());
  }
  @Test void standaloneResourcesPublishWithoutInventingDatasetEntitiesOrReferences() {
    OdmsCatalogue node=new OdmsCatalogue();node.setId(987654);node.setName("Standalone");
    node.setStandaloneRdf("<https://example.org/series> a <http://www.w3.org/ns/dcat#DatasetSeries> .");
    DcatDatasetSeries series=new Gson().fromJson("{\"resourceUri\":\"https://example.org/series\",\"title\":[{\"title\":\"Serie autonoma\",\"description\":\"Descrizione\",\"language\":\"it\"}]}",DcatDatasetSeries.class);
    node.setDatasetSeries(Arrays.asList(series));
    DcatDataService service=new Gson().fromJson("{\"resourceUri\":\"https://example.org/api\",\"title\":{\"value\":\"API\"},\"endpointURL\":[{\"value\":\"https://example.org/endpoint\"}]}",DcatDataService.class);
    node.setService(Arrays.asList(service));
    List<JSONObject> entities=NgsiDcatMapper.catalogue(node,Collections.emptyList());
    assertEquals(3,entities.size());
    JSONObject cat=entities.stream().filter(e->e.getString("type").equals("CatalogueDCAT-AP")).findFirst().orElseThrow();
    assertFalse(cat.has("dataset"));
    JSONObject evidence=new JSONObject(cat.getJSONObject("metadataEnvelope").getString("value"));
    assertEquals(node.getStandaloneRdf(),evidence.getString("sourceRdf"));
    String seriesId=cat.getJSONObject("resource").getJSONArray("object").getString(0);
    JSONObject mappedSeries=entities.stream().filter(e->e.getString("id").equals(seriesId)).findFirst().orElseThrow();
    assertEquals("Serie autonoma",mappedSeries.getJSONObject("title").getJSONObject("value").getString("it"));
    assertEquals("Descrizione",mappedSeries.getJSONObject("description").getJSONObject("value").getString("it"));
    assertEquals(entities.toString(),NgsiDcatMapper.catalogue(node,Collections.emptyList()).toString());
    assertTrue(entities.stream().noneMatch(e->e.getString("type").equals("Dataset")));
  }
  @Test void fallbackDcatUrisMatchIdraExportsWithoutChangingBrokerIds() {
    DcatDataset dataset=new Gson().fromJson("{\"id\":\"dataset-1\",\"distributions\":[{\"id\":\"distribution-1\"}]}",DcatDataset.class);
    OdmsCatalogue catalogue=new OdmsCatalogue();catalogue.setId(987654);
    List<JSONObject> entities=NgsiDcatMapper.catalogue(catalogue,Arrays.asList(dataset));
    Map<String,JSONObject> byId=new HashMap<>();for(JSONObject e:entities)byId.put(e.getString("id"),e);
    assertEquals("urn:idra:dataset:987654:dataset-1",byId.get("urn:ngsi-ld:Dataset:id:dataset-1").getJSONObject("resourceUri").getString("value"));
    assertEquals("urn:idra:distribution:987654:distribution-1",byId.get("urn:ngsi-ld:DistributionDCAT-AP:id:distribution-1").getJSONObject("resourceUri").getString("value"));
    assertNull(dataset.getResourceUri());assertNull(dataset.getDistributions().get(0).getResourceUri());
    dataset.setResourceUri("https://example.org/source");dataset.getDistributions().get(0).setResourceUri("https://example.org/file");
    entities=NgsiDcatMapper.catalogue(catalogue,Arrays.asList(dataset));byId.clear();for(JSONObject e:entities)byId.put(e.getString("id"),e);
    assertEquals("https://example.org/source",byId.get("urn:ngsi-ld:Dataset:id:dataset-1").getJSONObject("resourceUri").getString("value"));
    assertEquals("https://example.org/file",byId.get("urn:ngsi-ld:DistributionDCAT-AP:id:distribution-1").getJSONObject("resourceUri").getString("value"));
  }
  @Test void internalFlagsAreExcludedAndTechnicalTermsAreExplicit() {
    JSONObject entity=NgsiDcatMapper.fromJson(new JSONObject().put("hasStoredRDF",true).put("resourceUri","https://example.org/data"),"urn:test:1","Dataset");
    assertFalse(entity.has("hasStoredRDF"));
    JSONObject context=entity.getJSONArray("@context").getJSONObject(1);
    assertEquals("https://smartera.eu/ns/dcat-bridge/idraCatalogueId",context.getString("idraCatalogueId"));
    assertEquals("https://smartera.eu/ns/dcat-bridge/resourceUri",context.getString("resourceUri"));
  }
  @Test void catalogueReferencesCreatedEntitiesAndRetainsExtendedMetadata() throws Exception {
    DcatDataset dataset=new Gson().fromJson("{\"id\":\"fixture-dataset\",\"title\":{\"value\":\"Budget\"},\"description\":{\"value\":\"Annual budget\"},\"resourceUri\":\"https://example.org/dataset\",\"datasetDetails\":[{\"title\":\"Bilancio\",\"description\":\"Dati annuali\",\"language\":\"it\"},{\"title\":\"Budget\",\"description\":\"Annual data\",\"language\":\"en\"}],\"temporalResolution\":{\"value\":\"P1Y\"},\"previousVersion\":[\"https://example.org/v1\"],\"HVDCategory\":[{\"value\":\"https://example.org/hvd\"}],\"inSeries\":[{\"resourceUri\":\"https://example.org/series\",\"title\":[{\"title\":\"Budgets\",\"language\":\"en\"}],\"first\":[\"https://example.org/v1\"]}],\"distributions\":[{\"id\":\"fixture-dist\",\"title\":{\"value\":\"CSV\"},\"accessUrl\":{\"value\":\"https://example.org/data\"},\"byteSize\":{\"value\":\"0\"},\"compressionFormat\":{\"value\":\"application/gzip\"},\"accessService\":[{\"title\":{\"value\":\"API\"},\"endpointURL\":[{\"value\":\"https://example.org/api\"}]}]}]}",DcatDataset.class);
    OdmsCatalogue catalogue=new OdmsCatalogue(); catalogue.setId(987654); catalogue.setName("Fixture");
    List<JSONObject> entities=NgsiDcatMapper.catalogue(catalogue,Arrays.asList(dataset));
    Map<String,JSONObject> byId=new HashMap<>();for(JSONObject entity:entities)byId.put(entity.getString("id"),entity);
    JSONObject cat=byId.get("urn:ngsi-ld:Catalogue:id:987654");
    String reference=cat.getJSONObject("http://www.w3.org/ns/dcat#dataset").getJSONArray("object").getString(0);
    assertTrue(byId.containsKey(reference));
    JSONObject data=byId.get(reference);
    assertEquals("P1Y",data.getJSONObject("temporalResolution").getString("value"));
    assertEquals("https://example.org/v1",data.getJSONObject("previousVersion").getJSONArray("object").getString(0));
    assertEquals("Bilancio",data.getJSONObject("title").getJSONObject("value").getString("it"));
    String series=data.getJSONObject("inSeries").getJSONArray("object").getString(0);
    assertEquals("https://example.org/series",byId.get(series).getJSONObject("resourceUri").getString("value"));
    String distribution=data.getJSONObject("datasetDistribution").getJSONArray("object").getString(0);
    assertEquals("application/gzip",byId.get(distribution).getJSONObject("compressFormat").getString("value"));
    assertEquals(entities.toString(),NgsiDcatMapper.catalogue(catalogue,Arrays.asList(dataset)).toString());
    Files.writeString(Path.of("target/dcat3-entities.json"),new JSONArray(entities).toString(2));
  }
  @Test void scopedIdsPreservePunctuationAndAreDeterministic() {
    assertNotEquals(NgsiDcatMapper.linkedId("Agent","1","https://example.org/a-b"),NgsiDcatMapper.linkedId("Agent","1","https://example.org/ab"));
    assertNotEquals(NgsiDcatMapper.linkedId("Agent","1","same"),NgsiDcatMapper.linkedId("Agent","2","same"));
  }
  @Test void directApisUseCanonicalEndpointsAndRelationships() throws Exception {
    com.fasterxml.jackson.databind.ObjectMapper jackson=new com.fasterxml.jackson.databind.ObjectMapper();
    JSONObject service=new JSONObject(new it.eng.ngsild.broker.manager.model.DataServiceDcatAp().convertToNgsi(
        jackson.readTree("{\"id\":\"api\",\"endPointURL\":[\"https://example.org/api\"],\"servesDataset\":[\"urn:example:dataset\"]}")));
    assertEquals("https://example.org/api",service.getJSONObject("endpointURL").getJSONArray("value").getString(0));
    assertEquals("Relationship",service.getJSONObject("servesDataset").getString("type"));
    JSONObject distribution=new JSONObject(new it.eng.ngsild.broker.manager.model.DistributionDcatAp().convertToNgsi(
        jackson.readTree("{\"id\":\"download\",\"accessService\":[\"urn:example:service\"],\"compressionFormat\":\"application/gzip\"}")));
    assertEquals("Relationship",distribution.getJSONObject("accessService").getString("type"));
    assertEquals("application/gzip",distribution.getJSONObject("compressFormat").getString("value"));
  }
}
