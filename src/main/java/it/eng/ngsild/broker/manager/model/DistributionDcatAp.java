package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionDcatAp {
	   public String id;
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
	   public String[] downloadURL;
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
	   public String modifiedDate;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String name; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String[] owner; 
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String packagingFormat;
	   @JsonInclude(JsonInclude.Include.NON_NULL)
	   public String releaseDate;
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
	   public String[] title;
	public DistributionDcatAp(String id, String type, String[] accessService, AddressVerified address,
			String[] accessUrl, String alternateName, String areaServed, String availability, String byteSize,
			String checksum, String compressionFormat, String dataProvider, String dateCreated, String dateModified,
			String description, String[] documentation, String[] downloadURL, String format, String hasPolicy,
			String[] language, String license, String[] linkedSchemas, Point location, String mediaType,
			String modifiedDate, String name, String[] owner, String packagingFormat, String releaseDate, String rights,
			String seeAlso, String source, Double[] spatialResolution, Status status, Integer[] temporalResolution,
			String[] title) {
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
		
		String entity = null;
		try {
			String idDataset = distributionDcatAp.get("id") != null ? distributionDcatAp.get("id").textValue()
					: UUID.randomUUID().toString();
			String id = "urn:ngsi-ld:DistributionDCAT-AP:id:" + idDataset;
			String type = "DistributionDCAT-AP";

		
			String[] accessService = null;
			if (distributionDcatAp.get("title") != null && distributionDcatAp.get("accessService").isArray()) {
				accessService = new String[(distributionDcatAp.get("accessService").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("accessService")) {
					accessService[i] = c.toString();
					i++;
				}
			}
			AddressVerified addressVer = null;
			Address address = null;
			if (distributionDcatAp.get("address") != null) {
				String addressCountry = distributionDcatAp.get("address").get("addressCountry") != null
						? distributionDcatAp.get("address").get("addressCountry").textValue()
						: null;
				String addressLocality = distributionDcatAp.get("address").get("addressLocality") != null
						? distributionDcatAp.get("address").get("addressLocality").textValue()
						: null;
				String addressRegion = distributionDcatAp.get("address").get("addressRegion") != null
						? distributionDcatAp.get("address").get("addressRegion").textValue()
						: null;
				String district = distributionDcatAp.get("address").get("district") != null
						? distributionDcatAp.get("address").get("district").textValue()
						: null;
				String postOfficeBoxNumber = distributionDcatAp.get("address").get("postOfficeBoxNumber") != null
						? distributionDcatAp.get("address").get("postOfficeBoxNumber").textValue()
						: null;
				String postalCode = distributionDcatAp.get("address").get("postalCode") != null
						? distributionDcatAp.get("address").get("postalCode").textValue()
						: null;
				String streetAddress = distributionDcatAp.get("address").get("streetAddress") != null
						? distributionDcatAp.get("address").get("streetAddress").textValue()
						: null;
				String streetNr = distributionDcatAp.get("address").get("streetNr") != null
						? distributionDcatAp.get("address").get("streetNr").textValue()
						: null;
				address = new Address(addressCountry, addressLocality, addressRegion, district, postOfficeBoxNumber,
						postalCode, streetAddress, streetNr);
				addressVer = new AddressVerified(address, true);
			}
		
			String[] accessUrl = null;
			if (distributionDcatAp.get("accessUrl") != null && distributionDcatAp.get("accessUrl").isArray()) {
				accessUrl = new String[(distributionDcatAp.get("accessUrl").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("accessUrl")) {
					accessUrl[i] = c.toString();
					i++;
				}
			}
			String alternateName = distributionDcatAp.get("alternateName") != null
					? distributionDcatAp.get("alternateName").textValue()
					: null;
			String areaServed = distributionDcatAp.get("areaServed") != null
					? distributionDcatAp.get("areaServed").textValue()
					: null;
			String availability = distributionDcatAp.get("availability") != null
					? distributionDcatAp.get("availability").textValue()
					: null;
			String byteSize = distributionDcatAp.get("byteSize") != null
					? distributionDcatAp.get("byteSize").textValue()
					: null;
			String checksum = distributionDcatAp.get("checksum") != null
					? distributionDcatAp.get("checksum").textValue()
					: null;
			String compressionFormat = distributionDcatAp.get("compressionFormat") != null
					? distributionDcatAp.get("compressionFormat").textValue()
					: null;
			String dataProvider = distributionDcatAp.get("dataProvider") != null
					? distributionDcatAp.get("dataProvider").textValue()
					: null;
			String dateCreated = distributionDcatAp.get("dateCreated") != null
					? distributionDcatAp.get("dateCreated").textValue()
					: null;
			String dateModified = distributionDcatAp.get("dateModified") != null
					? distributionDcatAp.get("dateModified").textValue()
					: null;
			String description = distributionDcatAp.get("description") != null
					? distributionDcatAp.get("description").textValue()
					: null;
		 
			String[] documentation = null;
			if (distributionDcatAp.get("documentation") != null && distributionDcatAp.get("documentation").isArray()) {
				documentation = new String[(distributionDcatAp.get("documentation").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("documentation")) {
					documentation[i] = c.toString();
					i++;
				}
			}
			
			
			String[] downloadURL = null;
			if (distributionDcatAp.get("downloadURL") != null && distributionDcatAp.get("downloadURL").isArray()) {
				downloadURL = new String[(distributionDcatAp.get("downloadURL").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("downloadURL")) {
					downloadURL[i] = c.toString();
					i++;
				}
			}
			String format = distributionDcatAp.get("format") != null
					? distributionDcatAp.get("format").textValue()
					: null;
			String hasPolicy = distributionDcatAp.get("hasPolicy") != null
					? distributionDcatAp.get("hasPolicy").textValue()
					: null;
		
			String[] language = null;
			if (distributionDcatAp.get("language") != null && distributionDcatAp.get("language").isArray()) {
				language = new String[(distributionDcatAp.get("language").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("language")) {
					language[i] = c.toString();
					i++;
				}
			}
			String license = distributionDcatAp.get("license") != null
					? distributionDcatAp.get("license").textValue()
					: null;
			
			String[] linkedSchemas = null;
			if (distributionDcatAp.get("linkedSchemas") != null && distributionDcatAp.get("linkedSchemas").isArray()) {
				linkedSchemas = new String[(distributionDcatAp.get("linkedSchemas").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("linkedSchemas")) {
					linkedSchemas[i] = c.toString();
					i++;
				}
			}
			
			Point location = null;
			if (distributionDcatAp.get("location") != null) {
				location = new Point();

				Double coordinate[] = new Double[2];
				int j = 0;
				for (JsonNode coord : distributionDcatAp.get("location").get("coordinates")) {
					coordinate[j] = coord.asDouble();
					j++;
				}

				location = new Point("Point", coordinate);

			}
			String mediaType = distributionDcatAp.get("mediaType") != null
					? distributionDcatAp.get("mediaType").textValue()
					: null;
			String modifiedDate = distributionDcatAp.get("modifiedDate") != null
					? distributionDcatAp.get("modifiedDate").textValue()
					: null;
			String name = distributionDcatAp.get("name") != null
					? distributionDcatAp.get("name").textValue()
					: null;
		
			String[] owner = null;
			if (distributionDcatAp.get("owner") != null && distributionDcatAp.get("owner").isArray()) {
				owner = new String[(distributionDcatAp.get("owner").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("owner")) {
					owner[i] = c.toString();
					i++;
				}
			}
			String packagingFormat = distributionDcatAp.get("packagingFormat") != null
					? distributionDcatAp.get("packagingFormat").textValue()
					: null;
			String releaseDate = distributionDcatAp.get("releaseDate") != null
					? distributionDcatAp.get("releaseDate").textValue()
					: null;
			String rights = distributionDcatAp.get("rights") != null
					? distributionDcatAp.get("rights").textValue()
					: null;
			String seeAlso = distributionDcatAp.get("seeAlso") != null
					? distributionDcatAp.get("seeAlso").textValue()
					: null;
			String source = distributionDcatAp.get("source") != null
					? distributionDcatAp.get("source").textValue()
					: null;
			
			Double[] spatialResolution = null;
			if (distributionDcatAp.get("spatialResolution") != null && distributionDcatAp.get("spatialResolution").isArray()) {
				spatialResolution = new Double[(distributionDcatAp.get("spatialResolution").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("spatialResolution")) {
					spatialResolution[i] = c.asDouble();
					i++;
				}
			}
			Status status = null;
			if (distributionDcatAp.get("status") != null) {
				 status = this.findByNameStatus(name);
			} 
				
			
			Integer[] temporalResolution = null;
			if (distributionDcatAp.get("temporalResolution") != null && distributionDcatAp.get("temporalResolution").isArray()) {
				temporalResolution = new Integer[(distributionDcatAp.get("temporalResolution").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("temporalResolution")) {
					temporalResolution[i] = c.asInt();
					i++;
				}
			}
			
			String[] title = null;
			if (distributionDcatAp.get("title") != null && distributionDcatAp.get("title").isArray()) {
				title = new String[(distributionDcatAp.get("title").size())];
				int i = 0;
				for (JsonNode c : distributionDcatAp.get("title")) {
					title[i] = c.toString();
					i++;
				}
			}
			DistributionDcatAp distrngsi = new DistributionDcatAp (id,type,accessService,addressVer,
			accessUrl,alternateName,areaServed,availability,byteSize,
			  checksum,compressionFormat,dataProvider,dateCreated,dateModified,
			  description,documentation,downloadURL,format,hasPolicy,
			 language,license,linkedSchemas, location,mediaType,
			  modifiedDate,name,owner,packagingFormat,releaseDate,rights,
			  seeAlso,source,spatialResolution,status,temporalResolution,
			 title);
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			entity = mapper.writeValueAsString(distrngsi);
			
		} catch(Exception ex) {
			
		}
		return entity;
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
