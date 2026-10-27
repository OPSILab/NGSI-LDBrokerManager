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
	@JsonInclude(JsonInclude.Include.NON_NULL)
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
	public GeoProperty spatial;
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
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] applicableLegislation;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] HVDCategory;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] inSeries;

	public Dataset() {

	}

	public Dataset(String id, String type, AccessRight accessRights, String alternateName, String[] contactPoint,
			String creator, String dataProvider, String[] datasetDescription, String[] datasetDistribution,
			String[] datasetSource, String datasetType, DateTime dateCreated, DateTime dateModified, String description,
			String[] documentation, String frequency, String[] hasVersion, String identifier, String[] isReferencedBy,
			String[] isVersionOf, String[] keyword, String landingPage, String[] language, String name,
			String[] otherIdentifier, String[] owner, String[] provenance, String publisher,
			String[] qualifiedAttribution, String[] qualifiedRelation, String[] relatedResource, DateTime releaseDate,
			String[] sample, String seeAlso, String source, GeoProperty spatial, String[] temporalResolution,
			String[] theme, String title, DateTime updateDate, String version, String[] versionNotes,
			String[] wasGeneratedBy, String[] applicableLegislation, String[] HVDCategory, String[] inSeries) {
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
		this.applicableLegislation = applicableLegislation;
		this.HVDCategory = HVDCategory;
		this.inSeries = inSeries;
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
				+ ", releaseDate=" + releaseDate + ", sample=" + Arrays.toString(sample) + ", seeAlso=" + seeAlso
				+ ", source=" + source + ", spatial=" + spatial + ", temporalResolution="
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
				System.out.println(result);
				break;
			}
		}

		return result;
	}



	public String convertToNgsi(JsonNode dataset) {
    org.json.JSONObject input = new org.json.JSONObject(dataset.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String entityId = identifier.startsWith("urn:") || identifier.startsWith("http:") || identifier.startsWith("https:")
        ? identifier : "urn:ngsi-ld:Dataset:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input, entityId, "Dataset").toString();
  }

}
