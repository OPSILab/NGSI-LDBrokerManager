package it.eng.ngsild.broker.manager.service;

import com.google.gson.Gson;
import it.eng.idra.beans.dcat.DcatDataset;
import it.eng.idra.beans.odms.OdmsCatalogue;
import java.nio.file.*;
import java.util.*;
import org.json.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DcatMetadataEnvelopeBridgeTest {
  @Test void longEvidenceUsesBoundedLosslessTransportAndSmallEvidenceRemainsCompatible() throws Exception {
    String metadata=new JSONObject().put("version",1).put("sourceRdf","Città 東京 ".repeat(10000)).toString();
    JSONObject mapped=NgsiDcatMapper.fromJson(new JSONObject().put("metadataEnvelope",metadata),"urn:test:dataset","Dataset");
    JSONObject transport=mapped.getJSONObject("metadataEnvelope").getJSONObject("value");
    assertEquals("gzip-base64-utf8-chunks-v1",transport.getString("encoding"));
    StringBuilder encoded=new StringBuilder();
    for(Object chunk:transport.getJSONArray("chunks")) {assertTrue(((String)chunk).length()<=1024);encoded.append(chunk);}
    try(java.util.zip.GZIPInputStream gzip=new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(java.util.Base64.getDecoder().decode(encoded.toString())))) {
      assertEquals(metadata,new String(gzip.readAllBytes(),java.nio.charset.StandardCharsets.UTF_8));
    }
  }
  @Test void preservesVersionedEvidenceAcrossTheActualMapper() throws Exception {
    String input=System.getProperty("plan48.input");
    if(input==null) {
      JSONObject metadata=new JSONObject().put("version",1).put("source","fixture").put("sourceRdf","@prefix ex:<urn:test:>. ex:a ex:p \"Citoyenneté\"@fr.");
      JSONObject entity=NgsiDcatMapper.fromJson(new JSONObject().put("metadataEnvelope",metadata.toString()),"urn:test:dataset","Dataset");
      assertEquals(metadata.toString(),entity.getJSONObject("metadataEnvelope").getString("value"));return;
    }
    DcatDataset dataset=new Gson().fromJson(Files.readString(Path.of(input)),DcatDataset.class);
    OdmsCatalogue node=new OdmsCatalogue();node.setId(999999);node.setName("Plan 48 isolated fixture");
    List<JSONObject> entities=NgsiDcatMapper.catalogue(node,Arrays.asList(dataset));
    JSONObject mapped=entities.stream().filter(e->e.getString("type").equals("Dataset")).findFirst().orElseThrow();
    assertEquals(dataset.getMetadataEnvelope(),mapped.getJSONObject("metadataEnvelope").getString("value"));
    assertTrue(mapped.get("@context").toString().contains("metadataEnvelope"));
    Files.writeString(Path.of("target/plan48-entities.json"),new JSONArray(entities).toString());
  }
}
