package it.eng.ngsild.broker.manager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.json.simple.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dataset {
	public String id;
	public String type = "Dataset";
	public AccessRight accessRights;
	public String alternateName;
	public String[] contactPoint;
	public String creator;
	public String dataProvider;
	public String[] datasetDescription;
	public String[] datasetDistribution;
	public String[] datasetSource;
	public String datasetType;
	public String dateCreated;
	public String dateModified;
	public String description;
	public String[] documentation;
	public String frequency;
	public String[] hasVersion;
	public String[] identifier;
	public String[] isReferencedBy;
	public String[] isVersionOf;
	public String[] keyword;
	public String[] landingPage;
	public String[] language;
	public String name;
	public String[] otherIdentifier;
	public String[] owner;
	public String[] provenance;
	public AgentDcatAp publisher;
	public String[] qualifiedAttribution;
	public String[] qualifiedRelation;
	public String[] relatedResource;
	public String releaseDate;
	public String[] sample;
	public String seeAlso;
	public String source;
	public String[] spatial;
	public String[] temporalResolution;
	public String[] theme;
	public String[] title;
	public String updateDate;
	public String version;
	public String[] versionNotes;
	public String[] wasGeneratedBy;

	public Dataset() {
		
	}
	public Dataset(String id, String type, AccessRight accessRights, String alternateName, String[] contactPoint,
			String creator, String dataProvider, String[] datasetDescription, String[] datasetDistribution,
			String[] datasetSource, String datasetType, String dateCreated, String dateModified, String description,
			String[] documentation, String frequency, String[] hasVersion, String[] identifier, String[] isReferencedBy,
			String[] isVersionOf, String[] keyword, String[] landingPage, String[] language, String name,
			String[] otherIdentifier, String[] owner, String[] provenance, AgentDcatAp publisher,
			String[] qualifiedAttribution, String[] qualifiedRelation, String[] relatedResource, String releaseDate,
			String[] sample, String seeAlso, String source, String[] spatial, String[] temporalResolution,
			String[] theme, String[] title, String updateDate, String version, String[] versionNotes,
			String[] wasGeneratedBy) {
		super();
		this.id = id;
		this.type = type;
		this.accessRights = accessRights;
		this.alternateName = alternateName;
		this.contactPoint = contactPoint;
		this.creator = creator;
		this.dataProvider = dataProvider;
		this.datasetDescription = datasetDescription;
		this.datasetDistribution = datasetDistribution;
		this.datasetSource = datasetSource;
		this.datasetType = datasetType;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.documentation = documentation;
		this.frequency = frequency;
		this.hasVersion = hasVersion;
		this.identifier = identifier;
		this.isReferencedBy = isReferencedBy;
		this.isVersionOf = isVersionOf;
		this.keyword = keyword;
		this.landingPage = landingPage;
		this.language = language;
		this.name = name;
		this.otherIdentifier = otherIdentifier;
		this.owner = owner;
		this.provenance = provenance;
		this.publisher = publisher;
		this.qualifiedAttribution = qualifiedAttribution;
		this.qualifiedRelation = qualifiedRelation;
		this.relatedResource = relatedResource;
		this.releaseDate = releaseDate;
		this.sample = sample;
		this.seeAlso = seeAlso;
		this.source = source;
		this.spatial = spatial;
		this.temporalResolution = temporalResolution;
		this.theme = theme;
		this.title = title;
		this.updateDate = updateDate;
		this.version = version;
		this.versionNotes = versionNotes;
		this.wasGeneratedBy = wasGeneratedBy;
	}

	
	
	@Override
	public String toString() {
		return "Dataset [id=" + id + ", type=" + type + ", accessRights=" + accessRights + ", alternateName="
				+ alternateName + ", contactPoint=" + Arrays.toString(contactPoint) + ", creator=" + creator
				+ ", dataProvider=" + dataProvider + ", datasetDescription=" + Arrays.toString(datasetDescription)
				+ ", datasetDistribution=" + Arrays.toString(datasetDistribution) + ", datasetSource="
				+ Arrays.toString(datasetSource) + ", datasetType=" + datasetType + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + ", description=" + description + ", documentation="
				+ Arrays.toString(documentation) + ", frequency=" + frequency + ", hasVersion="
				+ Arrays.toString(hasVersion) + ", identifier=" + Arrays.toString(identifier) + ", isReferencedBy="
				+ Arrays.toString(isReferencedBy) + ", isVersionOf=" + Arrays.toString(isVersionOf) + ", keyword="
				+ Arrays.toString(keyword) + ", landingPage=" + Arrays.toString(landingPage) + ", language="
				+ Arrays.toString(language) + ", name=" + name + ", otherIdentifier=" + Arrays.toString(otherIdentifier)
				+ ", owner=" + Arrays.toString(owner) + ", provenance=" + Arrays.toString(provenance) + ", publisher="
				+ publisher + ", qualifiedAttribution=" + Arrays.toString(qualifiedAttribution) + ", qualifiedRelation="
				+ Arrays.toString(qualifiedRelation) + ", relatedResource=" + Arrays.toString(relatedResource)
				+ ", releaseDate=" + releaseDate + ", sample=" + Arrays.toString(sample) + ", seeAlso=" + seeAlso
				+ ", source=" + source + ", spatial=" + Arrays.toString(spatial) + ", temporalResolution="
				+ Arrays.toString(temporalResolution) + ", theme=" + Arrays.toString(theme) + ", title="
				+ Arrays.toString(title) + ", updateDate=" + updateDate + ", version=" + version + ", versionNotes="
				+ Arrays.toString(versionNotes) + ", wasGeneratedBy=" + Arrays.toString(wasGeneratedBy) + "]";
	}
	public static AccessRight findByNameAccessRight(String name) {
		AccessRight result = null;
		for (AccessRight accessRight : AccessRight.values()) {
			if(name.equalsIgnoreCase("non-public")) {
				result = AccessRight.NON_PUBLIC;
				break;
			}
			else if (accessRight.name().toLowerCase().equalsIgnoreCase(name)) {
				result = accessRight;
				break;
			}
		}
		return result;
	}

	public String convertToNgsi(JsonNode dataset) {
		String entity = null;
		try {
			// Mapping fields
			String idDataset = dataset.get("id") != null ? dataset.get("id").textValue() : UUID.randomUUID().toString();
			String id = "urn:ngsi-ld:Dataset:id:" + idDataset;
			String type = "Dataset";
			AccessRight accessRight = this.findByNameAccessRight(dataset.get("accessRights").textValue());
			String alternateName = dataset.get("alternateName") != null ? dataset.get("alternateName").toString()
					: null;
			String[] contactPoint;
			if(dataset.get("contactPoint")!= null && dataset.get("contactPoint").isArray()) {
				System.out.println("contactPoint");
				 contactPoint = new String[(dataset.get("contactPoint").size())];
				int i=0;
				for(JsonNode c: dataset.get("contactPoint")){
					contactPoint[i] = c.toString();
					System.out.println(contactPoint[i]);
					i++;	
				}
				
			} else {
				 contactPoint = null;
			}
			String creator = dataset.get("creator") != null ? dataset.get("creator").toString() : null;
			String dataProvider = dataset.get("dataProvider") != null ? dataset.get("dataProvider").toString() : null;
			String[] datasetDescription;
			if(dataset.get("datasetDescription")!= null &&  dataset.get("datasetDescription").isArray()) {
				System.out.println("datasetDescription");
				System.out.println(dataset.get("datasetDescription").size());
				 datasetDescription = new String[(dataset.get("datasetDescription").size())];
				int i=0;
				for(JsonNode c: dataset.get("datasetDescription")){
					datasetDescription[i] = c.toString();
					i++;	
				}
				
			} else {
				datasetDescription = null;
			}
			String[] datasetDistribution = null;
			System.out.println("datasetSource");
			String[] datasetSource;
			if(dataset.get("datasetSource")!= null && dataset.get("datasetSource").isArray()) {
				System.out.println("datasetSource 2");
				datasetSource = new String[(dataset.get("datasetSource").size())];
				int i=0;
				for(JsonNode c: dataset.get("datasetSource")){
					datasetSource[i] = c.toString();
					i++;	
				}
				
			} else {
				datasetSource = null;
			}
			String datasetType = dataset.get("datasetType") != null ? dataset.get("datasetType").toString() : null;
			String dateCreated = dataset.get("dateCreated") != null ? dataset.get("dateCreated").toString() : null;
			String dateModified = dataset.get("dateModified") != null ? dataset.get("dateModified").toString() : null;
			String description = dataset.get("description") != null ? dataset.get("description").toString() : null;
			System.out.println("194");
			String[] documentation;
			if(dataset.get("documentation")!= null && dataset.get("documentation").isArray()) {
				 documentation = new String[(dataset.get("documentation").size())];
				int i=0;
				for(JsonNode c: dataset.get("documentation")){
					documentation[i] = c.toString();
					i++;	
				}
				
			} else {
				documentation = null;
			}
			String frequency = dataset.get("frequency") != null ? dataset.get("frequency").toString() : null;
			String[] hasVersion;
			if(dataset.get("hasVersion")!= null && dataset.get("hasVersion").isArray()) {
				hasVersion = new String[(dataset.get("hasVersion").size())];
				int i=0;
				for(JsonNode c: dataset.get("hasVersion")){
					hasVersion[i] = c.toString();
					i++;	
				}
				
			} else {
				hasVersion = null;
			}
			String[] identifier;
			if(dataset.get("identifier")!= null && dataset.get("identifier").isArray()) {
				identifier = new String[(dataset.get("identifier").size())];
				int i=0;
				for(JsonNode c: dataset.get("identifier")){
					identifier[i] = c.toString();
					i++;	
				}
				
			} else {
				identifier = null;
			}
			String[] isReferencedBy;
			if(dataset.get("isReferencedBy")!= null && dataset.get("isReferencedBy").isArray()) {
			isReferencedBy = new String[(dataset.get("isReferencedBy").size())];
				int i=0;
				for(JsonNode c: dataset.get("isReferencedBy")){
					isReferencedBy[i] = c.toString();
					i++;	
				}
				
			} else {
				isReferencedBy = null;
			}
			
			String[] isVersionOf;
			if(dataset.get("isVersionOf")!= null && dataset.get("isVersionOf").isArray()) {
				 isVersionOf = new String[(dataset.get("isVersionOf").size())];
				int i=0;
				for(JsonNode c: dataset.get("isVersionOf")){
					isVersionOf[i] = c.toString();
					i++;	
				}
				
			} else {
				 isVersionOf = null;
			}
			
			String[] keyword;
			if(dataset.get("keyword")!= null && dataset.get("keyword").isArray()) {
				 keyword = new String[(dataset.get("keyword").size())];
				int i=0;
				for(JsonNode c: dataset.get("keyword")){
					keyword[i] = c.toString();
					i++;	
				}
				
			} else {
				 keyword = null;
			}
			
			String[] landingPage;
			if(dataset.get("landingPage")!= null && dataset.get("landingPage").isArray()) {
				landingPage = new String[(dataset.get("landingPage").size())];
				int i=0;
				for(JsonNode c: dataset.get("landingPage")){
					landingPage[i] = c.toString();
					i++;	
				}
				
			} else {
				 landingPage = null;
			}
			String[] language;
			if(dataset.get("language")!= null && dataset.get("language").isArray()) {
				 language = new String[(dataset.get("language").size())];
				int i=0;
				for(JsonNode c: dataset.get("language")){
					language[i] = c.toString();
					i++;	
				}
				
			} else {
				language = null;
			}
			
			String name = dataset.get("name") != null ? dataset.get("name").toString() : null;
			
			String[] otherIdentifier;
			if(dataset.get("otherIdentifier")!= null && dataset.get("otherIdentifier").isArray()) {
				 otherIdentifier = new String[(dataset.get("otherIdentifier").size())];
				int i=0;
				for(JsonNode c: dataset.get("otherIdentifier")){
					otherIdentifier[i] = c.toString();
					i++;	
				}
				
			} else {
				 otherIdentifier = null;
			}
			String[] owner;
			if(dataset.get("owner")!= null && dataset.get("owner").isArray()) {
				 owner = new String[(dataset.get("owner").size())];
				int i=0;
				for(JsonNode c: dataset.get("owner")){
					owner[i] = c.toString();
					i++;	
				}
				
			} else {
				 owner = null;
			}
			
			String[] provenance;
			if(dataset.get("provenance")!= null && dataset.get("provenance").isArray()) {
				provenance = new String[(dataset.get("provenance").size())];
				int i=0;
				for(JsonNode c: dataset.get("provenance")){
					provenance[i] = c.toString();
					i++;	
				}
				
			} else {
				 provenance = null;
			}
			
			AgentDcatAp publisher= null;
			String[] qualifiedAttribution;
			if(dataset.get("qualifiedAttribution")!= null && dataset.get("qualifiedAttribution").isArray()) {
				 qualifiedAttribution = new String[(dataset.get("qualifiedAttribution").size())];
				int i=0;
				for(JsonNode c: dataset.get("qualifiedAttribution")){
					qualifiedAttribution[i] = c.toString();
					i++;	
				}
				
			} else {
				 qualifiedAttribution = null;
			}
			String[] qualifiedRelation;
			if(dataset.get("qualifiedRelation")!= null && dataset.get("qualifiedRelation").isArray()) {
				 qualifiedRelation = new String[(dataset.get("qualifiedRelation").size())];
				int i=0;
				for(JsonNode c: dataset.get("qualifiedRelation")){
					qualifiedRelation[i] = c.toString();
					i++;	
				}
				
			} else {
				 qualifiedRelation = null;
			}
			
			String[] relatedResource;
			if(dataset.get("relatedResource")!= null && dataset.get("relatedResource").isArray()) {
				relatedResource = new String[(dataset.get("relatedResource").size())];
				int i=0;
				for(JsonNode c: dataset.get("relatedResource")){
					relatedResource[i] = c.toString();
					i++;	
				}
				
			} else {
				relatedResource = null;
			}
			
			String releaseDate = dataset.get("releaseDate") != null ? dataset.get("releaseDate").toString() : null;
			
			String[] sample;
			if(dataset.get("sample")!= null && dataset.get("sample").isArray()) {
				 sample = new String[(dataset.get("sample").size())];
				int i=0;
				for(JsonNode c: dataset.get("sample")){
					sample[i] = c.toString();
					i++;	
				}
				
			} else {
				 sample = null;
			}
			
			String seeAlso = dataset.get("seeAlso") != null ? dataset.get("seeAlso").toString() : null;
			String source = dataset.get("source") != null ? dataset.get("source").toString() : null;
			String[] spatial;
			if(dataset.get("spatial")!= null && dataset.get("spatial").isArray()) {
				 spatial = new String[(dataset.get("spatial").size())];
				int i=0;
				for(JsonNode c: dataset.get("spatial")){
					spatial[i] = c.toString();
					i++;	
				}
				
			} else {
				spatial = null;
			}
			String[] temporalResolution;
			if(dataset.get("temporalResolution")!= null && dataset.get("temporalResolution").isArray()) {
				temporalResolution = new String[(dataset.get("temporalResolution").size())];
				int i=0;
				for(JsonNode c: dataset.get("temporalResolution")){
					temporalResolution[i] = c.toString();
					i++;	
				}
				
			} else {
				temporalResolution = null;
			}
			
			String[] theme;
			if(dataset.get("theme")!= null && dataset.get("theme").isArray()) {
				theme = new String[(dataset.get("theme").size())];
				int i=0;
				for(JsonNode c: dataset.get("theme")){
					theme[i] = c.toString();
					i++;	
				}
				
			} else {
				 theme = null;
			}
			String[] title;
			if(dataset.get("title")!= null && dataset.get("title").isArray()) {
			 title = new String[(dataset.get("title").size())];
				int i=0;
				for(JsonNode c: dataset.get("title")){
					title[i] = c.toString();
					i++;	
				}
				
			} else {
				title = null;
			}
			
			String updateDate = dataset.get("updateDate") != null ? dataset.get("updateDate").toString() : null;
			String version = dataset.get("version") != null ? dataset.get("version").toString() : null;
			String[] versionNotes;
			if(dataset.get("versionNotes")!= null && dataset.get("versionNotes").isArray()) {
				 versionNotes = new String[(dataset.get("versionNotes").size())];
				int i=0;
				for(JsonNode c: dataset.get("versionNotes")){
					versionNotes[i] = c.toString();
					i++;	
				}
				
			} else {
				versionNotes = null;
			}
			
			String[] wasGeneratedBy;
			if(dataset.get("wasGeneratedBy")!= null &&  dataset.get("wasGeneratedBy").isArray()) {
				 wasGeneratedBy = new String[(dataset.get("wasGeneratedBy").size())];
				int i=0;
				for(JsonNode c: dataset.get("wasGeneratedBy")){
					wasGeneratedBy[i] = c.toString();
					i++;	
				}
				
			} else {
				wasGeneratedBy = null;
			}
			

			// create dataset Object
			Dataset datasetNgsi = new Dataset(id, type, accessRights,alternateName, contactPoint,
					creator, dataProvider,datasetDescription, datasetDistribution,
					 datasetSource, datasetType, dateCreated, dateModified, description,
					documentation,  frequency, hasVersion, identifier,  isReferencedBy,
					 isVersionOf,  keyword, landingPage,  language,  name,
					otherIdentifier,  owner,  provenance,  publisher,
					 qualifiedAttribution,  qualifiedRelation, relatedResource,  releaseDate,
					 sample, seeAlso,  source,  spatial,  temporalResolution,
					theme,  title,  updateDate,  version,  versionNotes,
					 wasGeneratedBy);

			
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			 entity = mapper.writeValueAsString(datasetNgsi);
			
			System.out.println(entity);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return entity;

		// return entity;
	}

}
