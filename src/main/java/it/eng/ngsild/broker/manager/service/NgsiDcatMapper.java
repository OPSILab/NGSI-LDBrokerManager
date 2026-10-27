package it.eng.ngsild.broker.manager.service;

import com.google.gson.Gson;
import it.eng.idra.beans.dcat.*;
import it.eng.idra.beans.odms.OdmsCatalogue;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

/** Pure DCAT/NGSI-LD mapping shared by catalogue federation and direct APIs. */
public final class NgsiDcatMapper {
  private static final Gson GSON = new Gson();
  private static final Set<String> INTERNAL = new HashSet<>(Arrays.asList(
      "id", "nodeID", "nodeId", "nodeName", "datasetId", "dataset_id", "datasetSeriesId",
      "distributionId", "brokerId", "dataServiceId", "catalogueRecordId", "hasStoredRdf", "hasStoredRDF", "distributionAdditionalConfig"));
  private NgsiDcatMapper() { }

  public static String datasetId(DcatDataset dataset) { return "urn:ngsi-ld:Dataset:id:" + dataset.getId(); }
  public static String linkedId(String type, String owner, Object identity) {
    return "urn:ngsi-ld:" + type + ":" + owner + ":"
        + UUID.nameUUIDFromBytes((identity instanceof String ? (String)identity : GSON.toJson(identity)).getBytes(StandardCharsets.UTF_8));
  }
  private static JSONObject json(Object bean) { return new JSONObject(GSON.toJson(bean)); }
  private static String alias(String field) {
    switch (field) {
      case "HVDCategory": return "hvdCategory";
      case "keywords": return "keyword";
      case "languages": return "language";
      case "licence": return "license";
      case "compressionFormat": return "compressFormat";
      case "packagingFormat": return "packageFormat";
      case "spatialResolution": return "spatialResolutionInMeters";
      case "geographicalCoverage": case "spatialCoverage": return "spatial";
      case "temporalCoverage": return "temporal";
      case "accessUrl": return "accessURL";
      case "downloadUrl": return "downloadURL";
      case "releaseDate": return "dateCreated";
      case "updateDate": case "modificationDate": return "dateModified";
      case "endPointURL": return "endpointURL";
      case "endPointDescription": return "endpointDescription";
      case "agentName": return "name";
      case "applicationProfile": return "conformsTo";
      case "listingDate": return "dateCreated";
      case "changeType": return "status";
      default: return field;
    }
  }
  private static Object value(Object raw) {
    if (raw instanceof JSONObject) {
      JSONObject obj = (JSONObject) raw;
      if (obj.has("value")) return obj.opt("value");
      return obj;
    }
    if (raw instanceof JSONArray) {
      JSONArray input=(JSONArray) raw, result=new JSONArray();
      for (int i=0;i<input.length();i++) {
        Object v=value(input.opt(i));
        if (present(v)) result.put(v);
      }
      return result;
    }
    return raw;
  }
  private static boolean present(Object value) {
    return value != null && value != JSONObject.NULL
        && (!(value instanceof String) || !((String)value).trim().isEmpty())
        && (!(value instanceof JSONArray) || ((JSONArray)value).length()>0);
  }
  public static void property(JSONObject entity, String name, Object value) {
    if (value instanceof String && (name.equals("byteSize") || name.equals("spatialResolutionInMeters"))) {
      try { value = new java.math.BigDecimal((String)value); }
      catch(NumberFormatException invalid) { /* Preserve invalid source data for validation, without inventing a number. */ }
    }
    if (present(value)) entity.put(name,new JSONObject().put("type","Property").put("value",value));
  }
  public static void relationship(JSONObject entity, String name, Object ids) {
    if (present(ids)) entity.put(name,new JSONObject().put("type","Relationship").put("object",ids));
  }
  public static JSONObject fromJson(JSONObject input, String id, String type) {
    JSONObject entity=new JSONObject().put("id",id).put("type",type);
    try (java.io.InputStream stream = NgsiDcatMapper.class.getResourceAsStream("/static/dcat3-context.jsonld")) {
      if (stream == null) throw new IllegalStateException("Missing DCAT bridge context");
      entity.put("@context",new JSONObject(new String(stream.readAllBytes(),StandardCharsets.UTF_8)).get("@context"));
    } catch (java.io.IOException e) { throw new IllegalStateException("Cannot read DCAT context",e); }
    if(input.has("type")) property(entity,"resourceType",value(input.opt("type")));
    for (String key : input.keySet()) {
      if (INTERNAL.contains(key) || key.equals("@context") || key.equals("type") || key.endsWith("Details") || key.equals("nameLanguage")) continue;
      Object v=value(input.opt(key));
      // Native metadata is not itself an NGSI-LD wrapper.
      if ((key.equals("metadataEnvelope") || key.equals("sourceMetadata")) && v instanceof String && ((String)v).length()>4096) {
        // Some broker versions truncate long individual strings on retrieval.
        // Keep opaque evidence lossless in bounded ASCII chunks, with an explicit codec.
        byte[] compressed;
        try {
          java.io.ByteArrayOutputStream buffer=new java.io.ByteArrayOutputStream();
          try(java.util.zip.GZIPOutputStream gzip=new java.util.zip.GZIPOutputStream(buffer)) {
            gzip.write(((String)v).getBytes(StandardCharsets.UTF_8));
          }
          compressed=buffer.toByteArray();
        } catch(java.io.IOException error) {throw new IllegalStateException("Cannot encode metadata evidence",error);}
        String encoded=java.util.Base64.getEncoder().encodeToString(compressed);
        JSONArray chunks=new JSONArray();
        for(int start=0;start<encoded.length();start+=1024)chunks.put(encoded.substring(start,Math.min(start+1024,encoded.length())));
        property(entity,key,new JSONObject().put("encoding","gzip-base64-utf8-chunks-v1").put("chunks",chunks));
      } else if (input.optJSONObject(key)!=null && input.optJSONObject(key).has("type")
          && (input.optJSONObject(key).has("value") || input.optJSONObject(key).has("object"))) {
        entity.put(alias(key),input.opt(key));
      } else if (Arrays.asList("datasetDistribution","inSeries","accessService","hasCurrentVersion","previousVersion","prev","next","first","last","servesDataset").contains(alias(key)) && uriReferences(v))
        relationship(entity,alias(key),v);
      else property(entity,alias(key),v);
    }
    languageDetails(entity,input.optJSONArray("datasetDetails"));
    languageDetails(entity,input.optJSONArray("distributionDetails"));
    languageDetails(entity,input.optJSONArray("dataServiceDetails"));
    Object rawNames=input.opt("name");
    localizedNames(entity,rawNames instanceof String?new JSONArray().put(rawNames):input.optJSONArray("name"),input.optJSONArray("nameLanguage"));
    if(type.equals("CatalogueRecordDCAT-AP")) languageDetails(entity,input.optJSONArray("title"));
    localizedValues(entity,"keyword",input.optJSONArray("keywordDetails"));
    localizedValues(entity,"versionNotes",input.optJSONArray("versionNoteDetails"));
    return entity;
  }
  private static boolean uriReferences(Object value) {
    if(value instanceof String)return ((String)value).matches("^[a-zA-Z][a-zA-Z0-9+.-]*:.*");
    if(value instanceof JSONArray && ((JSONArray)value).length()>0) {
      JSONArray values=(JSONArray)value;
      for(int i=0;i<values.length();i++)if(!uriReferences(values.opt(i)))return false;
      return true;
    }
    return false;
  }
  private static void languageDetails(JSONObject entity, JSONArray details) {
    if (details==null) return;
    for (String field : Arrays.asList("title","description")) {
      JSONObject languages=new JSONObject();
      for (int i=0;i<details.length();i++) {
        JSONObject item=details.optJSONObject(i); if(item==null) continue;
        String text=item.optString(field,""); if(text.trim().isEmpty()) continue;
        String lang=item.optString("language",""); if(lang.isEmpty()) lang="@none";
        Object existing=languages.opt(lang);
        if(existing==null) languages.put(lang,text);
        else if(existing instanceof JSONArray) ((JSONArray)existing).put(text);
        else languages.put(lang,new JSONArray().put(existing).put(text));
      }
      if(languages.length()>0) property(entity,field,languages);
    }
  }
  /** Agent names with a language become a language map, like titles; untagged names stay as they are. */
  private static void localizedNames(JSONObject entity,JSONArray names,JSONArray languages) {
    if(names==null || languages==null || languages.length()!=names.length())return;
    boolean tagged=false;
    for(int i=0;i<languages.length();i++)if(!languages.optString(i,"").isEmpty())tagged=true;
    if(!tagged)return;
    JSONObject map=new JSONObject();
    for(int i=0;i<names.length();i++) {
      Object raw=value(names.opt(i));if(!(raw instanceof String) || ((String)raw).isEmpty())continue;
      String language=languages.optString(i,"");map.append(language.isEmpty()?"@none":language,raw);
    }
    if(map.length()==0)return;
    for(String language:map.keySet()) {JSONArray texts=map.getJSONArray(language);map.put(language,texts.length()==1?texts.get(0):texts);}
    property(entity,"name",map);
  }
  private static void localizedValues(JSONObject entity,String name,JSONArray values) {
    if(values==null || values.length()==0)return;
    JSONObject map=new JSONObject();
    for(int i=0;i<values.length();i++) {
      JSONObject item=values.optJSONObject(i);if(item==null)continue;
      String language=item.optString("language", "@none"),text=item.optString("value", "");
      if(language.isEmpty())language="@none";
      if(text.isEmpty())continue;
      JSONArray localized=map.optJSONArray(language);
      if(localized==null){localized=new JSONArray();map.put(language,localized);}
      localized.put(text);
    }
    if(map.length()>0)property(entity,name,map);
  }
  public static List<JSONObject> catalogue(OdmsCatalogue node, List<DcatDataset> datasets) {
    Map<String,JSONObject> entities=new LinkedHashMap<>();
    String owner=String.valueOf(node.getId()); JSONArray datasetIds=new JSONArray();
    for(DcatDataset dataset:datasets) {
      String id=datasetId(dataset); datasetIds.put(id);
      JSONObject entity=fromJson(json(dataset),id,"Dataset");
      if(!present(dataset.getResourceUri()))
        property(entity,"resourceUri","urn:idra:dataset:"+owner+":"+dataset.getId());
      entity.remove("distributions");
      JSONArray distributionIds=new JSONArray();
      if(dataset.getDistributions()!=null) for(DcatDistribution distribution:dataset.getDistributions()) {
        String did="urn:ngsi-ld:DistributionDCAT-AP:id:"+distribution.getId();
        distributionIds.put(did); JSONObject d=fromJson(json(distribution),did,"DistributionDCAT-AP");
        if(!present(distribution.getResourceUri()))
          property(d,"resourceUri","urn:idra:distribution:"+owner+":"+distribution.getId());
        JSONArray services=new JSONArray();
        if(distribution.getAccessService()!=null) for(DcatDataService service:distribution.getAccessService())
          services.put(addService(entities,service,owner));
        d.remove("accessService"); relationship(d,"accessService",services);
        entities.put(did,d);
      }
      relationship(entity,"datasetDistribution",distributionIds);
      JSONArray seriesIds=new JSONArray();
      if(dataset.getInSeries()!=null) for(DcatDatasetSeries series:dataset.getInSeries())
        seriesIds.put(addLinked(entities,json(series),"DatasetSeries",owner,series.getResourceUri()));
      entity.remove("inSeries"); relationship(entity,"inSeries",seriesIds);
      for(String field:Arrays.asList("publisher","creator","rightsHolder")) {
        JSONObject agent=json(dataset).optJSONObject(field);
        if(agent!=null && present(agent.opt("resourceUri"))) {
          String aid=addLinked(entities,agent,"AgentDCAT-AP",owner,agent.opt("resourceUri"));
          entity.remove(field);relationship(entity,field,aid);
        }
      }
      for(String field:Arrays.asList("hasCurrentVersion","previousVersion","prev","next")) {
        Object ids=json(dataset).opt(field);entity.remove(field);relationship(entity,field,ids);
      }
      entities.put(id,entity);
    }
    String cid="urn:ngsi-ld:Catalogue:id:"+owner;
    JSONObject cat=new JSONObject().put("id",cid).put("type","CatalogueDCAT-AP");
    // Avoid the NGSI core aliases for manually assembled catalogue metadata.
    // Some Orion builds retain their protected meanings despite the inline context.
    property(cat,"http://purl.org/dc/terms/title",node.getName());
    property(cat,"http://purl.org/dc/terms/description",node.getDescription());
    property(cat,"homepage",node.getHomepage()); property(cat,"source",node.getHost());
    if(present(node.getPublisherName()) || present(node.getPublisherUrl())) {
      JSONObject publisher=new JSONObject();
      if(present(node.getPublisherName()))publisher.put("name",node.getPublisherName());
      if(present(node.getPublisherUrl()))publisher.put("resourceUri",node.getPublisherUrl());
      if(present(node.getPublisherEmail()))publisher.put("mbox",node.getPublisherEmail());
      relationship(cat,"publisher",addLinked(entities,publisher,"AgentDCAT-AP",owner,
          present(node.getPublisherUrl())?node.getPublisherUrl():publisher.toString()));
    }
    if(present(node.getSourceMetadata())) sourceCatalogue(cat,node.getSourceMetadata(),entities,owner);
    relationship(cat,"http://www.w3.org/ns/dcat#dataset",datasetIds);
    if(present(node.getStandaloneRdf())) {
      JSONObject evidence=new JSONObject().put("version",1).put("sourceRdf",node.getStandaloneRdf());
      JSONObject envelope=fromJson(new JSONObject().put("metadataEnvelope",evidence.toString()),cid,"CatalogueDCAT-AP");
      cat.put("metadataEnvelope",envelope.get("metadataEnvelope"));
    }
    JSONArray seriesIds=new JSONArray();
    if(node.getDatasetSeries()!=null) for(DcatDatasetSeries series:node.getDatasetSeries()) {
      JSONObject metadata=json(series);
      // Both bean fields contain the merged title/description details; normalize once.
      if(metadata.optJSONArray("title")!=null) metadata.put("datasetDetails",metadata.optJSONArray("title"));
      seriesIds.put(addLinked(entities,metadata,"DatasetSeries",owner,series.getResourceUri()));
    }
    relationship(cat,"resource",seriesIds);
    if(node.getApplicableLegislation()!=null) property(cat,"applicableLegislation",value(new JSONArray(GSON.toJson(node.getApplicableLegislation()))));
    if(node.getGeographicalCoverage()!=null) property(cat,"spatial",new JSONArray(GSON.toJson(node.getGeographicalCoverage())));
    if(node.getTemporalCoverage()!=null) property(cat,"temporal",new JSONArray(GSON.toJson(node.getTemporalCoverage())));
    JSONArray services=new JSONArray(),records=new JSONArray();
    if(node.getService()!=null) for(DcatDataService service:node.getService())
      services.put(addService(entities,service,owner));
    if(node.getRecord()!=null) for(DcatCatalogueRecord record:node.getRecord())
      records.put(addLinked(entities,json(record),"CatalogueRecordDCAT-AP",owner,record.getResourceUri()!=null?record.getResourceUri():json(record).toString()));
    relationship(cat,"service",services);relationship(cat,"record",records);
    if(node.getCreator()!=null) relationship(cat,"creator",addLinked(entities,json(node.getCreator()),"AgentDCAT-AP",owner,node.getCreator().getResourceUri()));
    entities.put(cid,cat);
    for (JSONObject entity : entities.values()) {
      property(entity,"idraCatalogueId",owner);
      if(!entity.has("@context")) entity.put("@context",fromJson(new JSONObject(),"placeholder","Dataset").get("@context"));
    }
    return new ArrayList<>(entities.values());
  }
  private static final String SOURCE_ROOT="urn:idra:catalogue:source";
  private static final String DCT="http://purl.org/dc/terms/", FOAF="http://xmlns.com/foaf/0.1/";
  // Projected from Idra's typed fields (geometry, periods, legislation, creator entity); the source does not replace them here.
  // Plain IRIs: no Jena class may be touched during this class's static initialisation.
  private static final Set<String> TYPED=new HashSet<>(Arrays.asList(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",DCT+"spatial",DCT+"temporal",DCT+"creator","http://data.europa.eu/r5r/applicableLegislation"));

  /** Source catalogue values replace Idra's configured ones, predicate by predicate; the evidence is kept losslessly. */
  static void sourceCatalogue(JSONObject cat,String stored,Map<String,JSONObject> entities,String owner) {
    JSONObject envelope=new JSONObject(stored);
    // Jena vocabulary classes must not initialise before the Jena subsystems (null RDF terms in the Boot jar).
    org.apache.jena.sys.JenaSystem.init();
    org.apache.jena.rdf.model.Model model=org.apache.jena.rdf.model.ModelFactory.createDefaultModel();
    org.apache.jena.riot.RDFParser.fromString(envelope.getString("sourceRdf")).lang(org.apache.jena.riot.Lang.TURTLE).parse(model);
    org.apache.jena.rdf.model.Resource root=model.getResource(SOURCE_ROOT);
    Set<org.apache.jena.rdf.model.Property> predicates=new LinkedHashSet<>();
    root.listProperties().forEachRemaining(st->predicates.add(st.getPredicate()));
    for(org.apache.jena.rdf.model.Property p:predicates) {
      String uri=p.getURI();
      if(TYPED.contains(uri))continue;
      List<org.apache.jena.rdf.model.RDFNode> values=model.listObjectsOfProperty(root,p).toList();
      if(uri.equals(DCT+"publisher")) {
        JSONArray ids=new JSONArray();
        for(org.apache.jena.rdf.model.RDFNode v:values)if(v.isResource())ids.put(addLinked(entities,agent(v.asResource()),"AgentDCAT-AP",owner,
            v.isURIResource()?v.asResource().getURI():agent(v.asResource()).toString()));
        JSONObject configured=cat.optJSONObject("publisher");cat.remove("publisher");relationship(cat,"publisher",ids);
        // The configured publisher entity was created only for the catalogue; drop it unless a dataset shares it.
        String old=configured==null?null:configured.optString("object",null);
        if(old!=null && !ids.toList().contains(old)
            && entities.entrySet().stream().noneMatch(e->!e.getKey().equals(old) && e.getValue().toString().contains(old)))entities.remove(old);
        continue;
      }
      if(uri.equals(FOAF+"homepage"))cat.remove("homepage");
      cat.remove(uri);property(cat,uri,rdfValue(values,new HashSet<>()));
    }
    property(cat,"sourceResource",envelope.optString("sourceResource",null));
    JSONObject evidence=fromJson(new JSONObject().put("sourceMetadata",stored),"urn:ngsi-ld:CatalogueDCAT-AP:evidence","CatalogueDCAT-AP");
    cat.put("sourceMetadata",evidence.get("sourceMetadata"));
  }
  private static JSONObject agent(org.apache.jena.rdf.model.Resource r) {
    JSONObject agent=new JSONObject();
    if(r.isURIResource())agent.put("resourceUri",r.getURI());
    JSONArray names=new JSONArray(),languages=new JSONArray();
    r.listProperties(r.getModel().createProperty(FOAF+"name")).forEachRemaining(st->{
      names.put(st.getObject().isLiteral()?st.getLiteral().getLexicalForm():st.getObject().toString());
      languages.put(st.getObject().isLiteral()?st.getLiteral().getLanguage():"");
    });
    if(names.length()>0)agent.put("name",names.length()==1?names.get(0):names);
    if(names.length()>0)agent.put("nameLanguage",languages);
    for(String[] field:new String[][]{{"mbox",FOAF+"mbox"},{"homepage",FOAF+"homepage"},{"type",DCT+"type"},{"identifier",DCT+"identifier"}}) {
      Object v=rdfValue(r.getModel().listObjectsOfProperty(r,r.getModel().createProperty(field[1])).toList(),new HashSet<>());
      if(present(v))agent.put(field[0],v);
    }
    return agent;
  }
  /** IRIs and plain literals as text, language-tagged literals as a language map, blank nodes as nested objects. */
  private static Object rdfValue(List<org.apache.jena.rdf.model.RDFNode> values,Set<org.apache.jena.rdf.model.Resource> visiting) {
    JSONArray out=new JSONArray();JSONObject languages=new JSONObject();
    for(org.apache.jena.rdf.model.RDFNode v:values) {
      if(v.isURIResource())out.put(v.asResource().getURI());
      else if(v.isLiteral()) {
        String lang=v.asLiteral().getLanguage();
        if(lang==null||lang.isEmpty())out.put(v.asLiteral().getLexicalForm());
        else languages.append(lang,v.asLiteral().getLexicalForm());
      } else if(visiting.add(v.asResource())) {
        JSONObject nested=new JSONObject();
        Set<org.apache.jena.rdf.model.Property> ps=new LinkedHashSet<>();
        v.asResource().listProperties().forEachRemaining(st->ps.add(st.getPredicate()));
        for(org.apache.jena.rdf.model.Property p:ps)
          nested.put(p.getURI(),rdfValue(v.getModel().listObjectsOfProperty(v.asResource(),p).toList(),visiting));
        visiting.remove(v.asResource());out.put(nested);
      }
    }
    for(String lang:languages.keySet()) {
      JSONArray texts=languages.getJSONArray(lang);languages.put(lang,texts.length()==1?texts.get(0):texts);
    }
    if(languages.length()>0)out.put(languages);
    return out.length()==1?out.get(0):out;
  }
  /** A data service with its publisher as a linked agent entity, as for datasets. */
  private static String addService(Map<String,JSONObject> entities,DcatDataService service,String owner) {
    String id=addLinked(entities,json(service),"DataServiceDCAT-AP",owner,service.getResourceUri()!=null?service.getResourceUri():service.getEndpointURL());
    JSONObject agent=json(service).optJSONObject("publisher");
    if(agent!=null && present(agent.opt("resourceUri"))) {
      JSONObject entity=entities.get(id);entity.remove("publisher");
      relationship(entity,"publisher",addLinked(entities,agent,"AgentDCAT-AP",owner,agent.opt("resourceUri")));
    }
    return id;
  }
  private static String addLinked(Map<String,JSONObject> entities,JSONObject metadata,String type,String owner,Object identity) {
    String id=linkedId(type,owner,identity!=null?identity:metadata.toString());
    JSONObject entity=fromJson(metadata,id,type),existing=entities.get(id);
    // One resource reached twice (e.g. a catalogue service that is also a distribution's access service):
    // keep the first, richer projection and only add what it lacks, instead of overwriting it.
    if(existing==null) entities.put(id,entity);
    else for(String key:entity.keySet()) if(!existing.has(key)) existing.put(key,entity.get(key));
    return id;
  }
}
