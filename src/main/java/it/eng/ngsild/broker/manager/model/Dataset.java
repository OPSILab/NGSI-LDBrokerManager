package it.eng.ngsild.broker.manager.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class Dataset {
	public String id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String type = "Dataset";
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AccessRight accessRights;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] contactPoint;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String creator;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	public String[] datasetDescription;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] datasetDistribution;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] datasetSource;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String datasetType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public DateTime dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public DateTime dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] documentation;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String frequency;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] hasVersion;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String identifier;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] isReferencedBy;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] isVersionOf;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] keyword;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String landingPage;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] language;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] otherIdentifier;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] provenance;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String publisher;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] qualifiedAttribution;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] qualifiedRelation;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] relatedResource;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public DateTime releaseDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] sample;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point[] spatial;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] temporalResolution;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] theme;
	public String title;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public DateTime updateDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String version;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] versionNotes;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] wasGeneratedBy;

	public Dataset() {

	}

	public Dataset(String id, String type, AccessRight accessRights, String alternateName, String[] contactPoint,
			String creator, String dataProvider, String[] datasetDescription, String[] datasetDistribution,
			String[] datasetSource, String datasetType, DateTime dateCreated, DateTime dateModified, String description,
			String[] documentation, String frequency, String[] hasVersion, String identifier, String[] isReferencedBy,
			String[] isVersionOf, String[] keyword, String landingPage, String[] language, String name,
			String[] otherIdentifier, String[] owner, String[] provenance, String publisher,
			String[] qualifiedAttribution, String[] qualifiedRelation, String[] relatedResource, DateTime releaseDate,
			String[] sample, String seeAlso, String source, Point[] spatial, String[] temporalResolution,
			String[] theme, String title, DateTime updateDate, String version, String[] versionNotes,
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
				+ Arrays.toString(hasVersion) + ", identifier=" + identifier + ", isReferencedBy="
				+ Arrays.toString(isReferencedBy) + ", isVersionOf=" + Arrays.toString(isVersionOf) + ", keyword="
				+ Arrays.toString(keyword) + ", landingPage=" + landingPage + ", language="
				+ Arrays.toString(language) + ", name=" + name + ", otherIdentifier=" + Arrays.toString(otherIdentifier)
				+ ", owner=" + Arrays.toString(owner) + ", provenance=" + Arrays.toString(provenance) + ", publisher="
				+ publisher + ", qualifiedAttribution=" + Arrays.toString(qualifiedAttribution) + ", qualifiedRelation="
				+ Arrays.toString(qualifiedRelation) + ", relatedResource=" + Arrays.toString(relatedResource)
				+ ", releaseDate=" + releaseDate.toString() + ", sample=" + Arrays.toString(sample) + ", seeAlso=" + seeAlso
				+ ", source=" + source + ", spatial=" + Arrays.toString(spatial) + ", temporalResolution="
				+ Arrays.toString(temporalResolution) + ", theme=" + Arrays.toString(theme) + ", title="
				+ title + ", updateDate=" + updateDate + ", version=" + version + ", versionNotes="
				+ Arrays.toString(versionNotes) + ", wasGeneratedBy=" + Arrays.toString(wasGeneratedBy) + "]";
	}

	public AccessRight findByNameAccessRight(String name) {
		AccessRight result = null;
		for (AccessRight accessRight : AccessRight.values()) {
			if (name.equalsIgnoreCase("non-public")) {

				result = AccessRight.NON_PUBLIC;

				break;
			} else if (accessRight.name().toLowerCase().equalsIgnoreCase(name)) {
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
			if (dataset.get("accessRights") != null) {
				AccessRight accessRights = this.findByNameAccessRight(dataset.get("accessRights").textValue());
			}

			System.out.println(accessRights);
			String alternateName = dataset.get("alternateName") != null ? dataset.get("alternateName").textValue()
					: null;
			String[] contactPoint = null;
			if (dataset.get("contactPoint") != null && dataset.get("contactPoint").isArray()) {
				System.out.println("contactPoint");
				contactPoint = new String[(dataset.get("contactPoint").size())];
				int i = 0;
				for (JsonNode c : dataset.get("contactPoint")) {
					contactPoint[i] = c.textValue();
					System.out.println(contactPoint[i]);
					i++;
				}

			}
			String creator = dataset.get("creator") != null ? dataset.get("creator").textValue() : null;
			String dataProvider = dataset.get("dataProvider") != null ? dataset.get("dataProvider").textValue() : null;
			String[] datasetDescription = null;
			if (dataset.get("datasetDescription") != null && dataset.get("datasetDescription").isArray()) {
				System.out.println("datasetDescription");
				System.out.println(dataset.get("datasetDescription").size());
				datasetDescription = new String[(dataset.get("datasetDescription").size())];
				int i = 0;
				for (JsonNode c : dataset.get("datasetDescription")) {
					datasetDescription[i] = c.textValue();
					i++;
				}

			}
			String[] datasetDistribution = null;

			if (dataset.get("datasetDistribution") != null && dataset.get("datasetDistribution").isArray()) {
				datasetDistribution = new String[(dataset.get("datasetDistribution").size())];
				int i = 0;
				for (JsonNode c : dataset.get("datasetDistribution")) {
					datasetDistribution[i] = "urn:ngsi-ld:Dataset:items:" + c.textValue();
					System.out.println("ciao");
					i++;
				}
			}
			System.out.println(datasetDistribution);
			String[] datasetSource = null;
			if (dataset.get("datasetSource") != null && dataset.get("datasetSource").isArray()) {
				System.out.println("datasetSource 2");
				datasetSource = new String[(dataset.get("datasetSource").size())];
				int i = 0;
				for (JsonNode c : dataset.get("datasetSource")) {
					datasetSource[i] = c.textValue();
					i++;
				}

			}
			String datasetType = dataset.get("datasetType") != null ? dataset.get("datasetType").textValue() : null;
			DateTime dateCreated = dataset.get("dateCreated") != null ? new DateTime(dataset.get("dateCreated").textValue()) : null;
			DateTime dateModified = dataset.get("dateModified") != null ? new DateTime(dataset.get("dateModified").textValue()) : null;
			String description = dataset.get("description") != null ? dataset.get("description").textValue() : null;
			System.out.println("194");
			String[] documentation = null;
			if (dataset.get("documentation") != null && dataset.get("documentation").isArray()) {
				documentation = new String[(dataset.get("documentation").size())];
				int i = 0;
				for (JsonNode c : dataset.get("documentation")) {
					documentation[i] = c.textValue();
					i++;
				}

			}
			String frequency = dataset.get("frequency") != null ? dataset.get("frequency").textValue() : null;
			String[] hasVersion = null;
			if (dataset.get("hasVersion") != null && dataset.get("hasVersion").isArray()) {
				hasVersion = new String[(dataset.get("hasVersion").size())];
				int i = 0;
				for (JsonNode c : dataset.get("hasVersion")) {
					hasVersion[i] = c.textValue();
					i++;
				}

			}
			String identifier = dataset.get("identifier") != null ? dataset.get("identifier").textValue() : null;
			/*if (dataset.get("identifier") != null && dataset.get("identifier").isArray()) {
				identifier = new String[(dataset.get("identifier").size())];
				int i = 0;
				for (JsonNode c : dataset.get("identifier")) {
					identifier[i] = c.textValue();
					i++;
				}

			}*/
			String[] isReferencedBy = null;
			if (dataset.get("isReferencedBy") != null && dataset.get("isReferencedBy").isArray()) {
				isReferencedBy = new String[(dataset.get("isReferencedBy").size())];
				int i = 0;
				for (JsonNode c : dataset.get("isReferencedBy")) {
					isReferencedBy[i] = c.textValue();
					i++;
				}

			}
			String[] isVersionOf = null;
			if (dataset.get("isVersionOf") != null && dataset.get("isVersionOf").isArray()) {
				isVersionOf = new String[(dataset.get("isVersionOf").size())];
				int i = 0;
				for (JsonNode c : dataset.get("isVersionOf")) {
					isVersionOf[i] = c.textValue();
					i++;
				}

			}
			String[] keyword = null;
			if (dataset.get("keyword") != null && dataset.get("keyword").isArray()) {
				keyword = new String[(dataset.get("keyword").size())];
				int i = 0;
				for (JsonNode c : dataset.get("keyword")) {
					keyword[i] = c.textValue();
					i++;
				}

			}
			String landingPage = dataset.get("landingPage") != null ? dataset.get("landingPage").textValue()
					: null;
			/*if (dataset.get("landingPage") != null && dataset.get("landingPage").isArray()) {
				landingPage = new String[(dataset.get("landingPage").size())];
				int i = 0;
				for (JsonNode c : dataset.get("landingPage")) {
					landingPage[i] = c.textValue();
					i++;
				}

			}*/
			String[] language = null;
			if (dataset.get("language") != null && dataset.get("language").isArray()) {
				language = new String[(dataset.get("language").size())];
				int i = 0;
				for (JsonNode c : dataset.get("language")) {
					language[i] = c.textValue();
					i++;
				}

			}

			String name = dataset.get("name") != null ? dataset.get("name").textValue() : null;

			String[] otherIdentifier = null;
			if (dataset.get("otherIdentifier") != null && dataset.get("otherIdentifier").isArray()) {
				otherIdentifier = new String[(dataset.get("otherIdentifier").size())];
				int i = 0;
				for (JsonNode c : dataset.get("otherIdentifier")) {
					otherIdentifier[i] = c.textValue();
					i++;
				}

			}
			String[] owner = null;
			if (dataset.get("owner") != null && dataset.get("owner").isArray()) {
				owner = new String[(dataset.get("owner").size())];
				int i = 0;
				for (JsonNode c : dataset.get("owner")) {
					owner[i] = c.textValue();
					i++;
				}

			}

			String[] provenance = null;
			if (dataset.get("provenance") != null && dataset.get("provenance").isArray()) {
				provenance = new String[(dataset.get("provenance").size())];
				int i = 0;
				for (JsonNode c : dataset.get("provenance")) {
					provenance[i] = c.textValue();
					i++;
				}

			}
			String publisher = dataset.get("publisher") != null ? dataset.get("publisher").textValue() : null;

			String[] qualifiedAttribution = null;
			if (dataset.get("qualifiedAttribution") != null && dataset.get("qualifiedAttribution").isArray()) {
				qualifiedAttribution = new String[(dataset.get("qualifiedAttribution").size())];
				int i = 0;
				for (JsonNode c : dataset.get("qualifiedAttribution")) {
					qualifiedAttribution[i] = c.textValue();
					i++;
				}

			}
			String[] qualifiedRelation = null;
			if (dataset.get("qualifiedRelation") != null && dataset.get("qualifiedRelation").isArray()) {
				qualifiedRelation = new String[(dataset.get("qualifiedRelation").size())];
				int i = 0;
				for (JsonNode c : dataset.get("qualifiedRelation")) {
					qualifiedRelation[i] = c.textValue();
					i++;
				}

			}

			String[] relatedResource = null;
			if (dataset.get("relatedResource") != null && dataset.get("relatedResource").isArray()) {
				relatedResource = new String[(dataset.get("relatedResource").size())];
				int i = 0;
				for (JsonNode c : dataset.get("relatedResource")) {
					relatedResource[i] = c.textValue();
					i++;
				}

			}

			DateTime releaseDate = dataset.get("releaseDate") != null ? new DateTime(dataset.get("releaseDate").textValue()) : null;

			String[] sample = null;
			if (dataset.get("sample") != null && dataset.get("sample").isArray()) {
				sample = new String[(dataset.get("sample").size())];
				int i = 0;
				for (JsonNode c : dataset.get("sample")) {
					sample[i] = c.textValue();
					i++;
				}

			}

			String seeAlso = dataset.get("seeAlso") != null ? dataset.get("seeAlso").textValue() : null;
			String source = dataset.get("source") != null ? dataset.get("source").textValue() : null;
			Point[] spatial = null;
			if (dataset.get("spatial") != null && dataset.get("spatial").isArray()) {
				System.out.println (dataset.get("spatial").size());
			spatial= new Point[dataset.get("spatial").size()];
				int i = 0;
				for (JsonNode c : dataset.get("spatial")) {
					
					System.out.println (c);
					Double coordinate[] = new Double[2];
					int j = 0;
					for (JsonNode coord : c.get("coordinates")) {
						System.out.println (coord);
						coordinate[j] = coord.asDouble();
						j++;
					}
					System.out.println(coordinate);
					spatial[i] = new Point("Point",coordinate);
					i++;
				}
				System.out.println(i);
			}
			System.out.println(spatial.toString());

			String[] temporalResolution = null;
			if (dataset.get("temporalResolution") != null && dataset.get("temporalResolution").isArray()) {
				temporalResolution = new String[(dataset.get("temporalResolution").size())];
				int i = 0;
				for (JsonNode c : dataset.get("temporalResolution")) {
					temporalResolution[i] = c.textValue();
					i++;
				}

			}

			String[] theme = null;
			if (dataset.get("theme") != null && dataset.get("theme").isArray()) {
				theme = new String[(dataset.get("theme").size())];
				int i = 0;
				for (JsonNode c : dataset.get("theme")) {
					theme[i] = c.textValue();
					i++;
				}

			}
			String title = dataset.get("title") != null ? dataset.get("title").textValue() : null;
			/*if (dataset.get("title") != null && dataset.get("title").isArray()) {
				title = new String[(dataset.get("title").size())];
				int i = 0;
				for (JsonNode c : dataset.get("title")) {
					title[i] = c.textValue();
					i++;
				}

			}*/
			DateTime updateDate = dataset.get("updateDate") != null ? new DateTime(dataset.get("updateDate").textValue()) : null;
			String version = dataset.get("version") != null ? dataset.get("version").textValue() : null;
			String[] versionNotes = null;
			if (dataset.get("versionNotes") != null && dataset.get("versionNotes").isArray()) {
				versionNotes = new String[(dataset.get("versionNotes").size())];
				int i = 0;
				for (JsonNode c : dataset.get("versionNotes")) {
					versionNotes[i] = c.textValue();
					i++;
				}

			}

			String[] wasGeneratedBy = null;
			if (dataset.get("wasGeneratedBy") != null && dataset.get("wasGeneratedBy").isArray()) {
				wasGeneratedBy = new String[(dataset.get("wasGeneratedBy").size())];
				int i = 0;
				for (JsonNode c : dataset.get("wasGeneratedBy")) {
					wasGeneratedBy[i] = c.textValue();
					i++;
				}

			}

			// create dataset Object
			Dataset datasetNgsi = new Dataset(id, type, accessRights, alternateName, contactPoint, creator,
					dataProvider, datasetDescription, datasetDistribution, datasetSource, datasetType, dateCreated,
					dateModified, description, documentation, frequency, hasVersion, identifier, isReferencedBy,
					isVersionOf, keyword, landingPage, language, name, otherIdentifier, owner, provenance, publisher,
					qualifiedAttribution, qualifiedRelation, relatedResource, releaseDate, sample, seeAlso, source,
					spatial, temporalResolution, theme, title, updateDate, version, versionNotes, wasGeneratedBy);

			System.out.println(datasetNgsi);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
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
