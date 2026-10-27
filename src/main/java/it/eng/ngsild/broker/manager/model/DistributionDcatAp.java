package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DistributionDcatAp {
	   public String id;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String type;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] accessService;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public AddressVerified address;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] accessUrl;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String alternateName;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String areaServed; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String availability; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String byteSize;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String checksum;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String compressionFormat; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String dataProvider;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String dateCreated; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String dateModified;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String description; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] documentation;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String downloadURL;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String format;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String hasPolicy;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] language;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String license;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] linkedSchemas;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public Point location;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String mediaType;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public DateTime modifiedDate;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String name; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] owner; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String packagingFormat;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public DateTime releaseDate;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String rights;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String seeAlso;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String source;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public Double[] spatialResolution;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public Status status;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public Integer[] temporalResolution ;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String title;
	public DistributionDcatAp(String id, String type, String[] accessService, AddressVerified address,
			String[] accessUrl, String alternateName, String areaServed, String availability, String byteSize,
			String checksum, String compressionFormat, String dataProvider, String dateCreated, String dateModified,
			String description, String[] documentation, String downloadURL, String format, String hasPolicy,
			String[] language, String license, String[] linkedSchemas, Point location, String mediaType,
			DateTime modifiedDate, String name, String[] owner, String packagingFormat, DateTime releaseDate, String rights,
			String seeAlso, String source, Double[] spatialResolution, Status status, Integer[] temporalResolution,
			String title) {
		super();
		this.id = id;
		this.type = type;
		this.accessService = accessService;
		this.address = address;
		this.accessUrl = accessUrl;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.availability = availability;
		this.byteSize = byteSize;
		this.checksum = checksum;
		this.compressionFormat = compressionFormat;
		this.dataProvider = dataProvider;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.documentation = documentation;
		this.downloadURL = downloadURL;
		this.format = format;
		this.hasPolicy = hasPolicy;
		this.language = language;
		this.license = license;
		this.linkedSchemas = linkedSchemas;
		this.location = location;
		this.mediaType = mediaType;
		this.modifiedDate = modifiedDate;
		this.name = name;
		this.owner = owner;
		this.packagingFormat = packagingFormat;
		this.releaseDate = releaseDate;
		this.rights = rights;
		this.seeAlso = seeAlso;
		this.source = source;
		this.spatialResolution = spatialResolution;
		this.status = status;
		this.temporalResolution = temporalResolution;
		this.title = title;
	}
	   
	public DistributionDcatAp() {
		
	}
	
	
	public String convertToNgsi(JsonNode distributionDcatAp) {
    org.json.JSONObject input = new org.json.JSONObject(distributionDcatAp.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String id = identifier.contains(":") ? identifier : "urn:ngsi-ld:DistributionDCAT-AP:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input,id,"DistributionDCAT-AP").toString();
  }
	
	
	public Status findByNameStatus(String name) {
		Status result = null;
		for (Status accessRight : Status.values()) {
			 if (accessRight.name().toLowerCase().equalsIgnoreCase(name)) {
				result = accessRight;
				break;
			}
		}

		return result;
	}
	   
	  
	
}
