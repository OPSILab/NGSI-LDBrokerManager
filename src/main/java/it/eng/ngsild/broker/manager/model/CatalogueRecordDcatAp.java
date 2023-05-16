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
public class CatalogueRecordDcatAp {
	public String id;
	public String modificationDate;
	public String primaryTopic;
	public String type;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified address;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String applicationProfile;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String changeType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] language;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String listingDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String sourceMetadata;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] title;

	public CatalogueRecordDcatAp(String id, String modificationDate, String primaryTopic, String type,
			AddressVerified address, String alternateName, String applicationProfile, String areaServed,
			String changeType, String dataProvider, String dateCreated, String dateModified, String description,
			String[] language, String listingDate, Point location, String name, String[] owner, String seeAlso,
			String source, String sourceMetadata, String[] title) {
		super();
		this.id = id;
		this.modificationDate = modificationDate;
		this.primaryTopic = primaryTopic;
		this.type = type;
		this.address = address;
		this.alternateName = alternateName;
		this.applicationProfile = applicationProfile;
		this.areaServed = areaServed;
		this.changeType = changeType;
		this.dataProvider = dataProvider;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.language = language;
		this.listingDate = listingDate;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.seeAlso = seeAlso;
		this.source = source;
		this.sourceMetadata = sourceMetadata;
		this.title = title;
	}
	public CatalogueRecordDcatAp() {
		
	}

	public String convertToNgsi(JsonNode catalogueRecordDcatAp) {
		String entity = null;
		try {
			String idDataset = catalogueRecordDcatAp.get("id") != null ? catalogueRecordDcatAp.get("id").textValue()
					: UUID.randomUUID().toString();
			String id = "urn:ngsi-ld:CatalogueRecordDCAT-AP:id:" + idDataset;
			String type = "CatalogueRecordDCAT-AP";
			String modificationDate= catalogueRecordDcatAp.get("modificationDate") != null ? catalogueRecordDcatAp.get("modificationDate").textValue():null;
			String primaryTopic=catalogueRecordDcatAp.get("primaryTopic") != null ? catalogueRecordDcatAp.get("primaryTopic").textValue():null;
			
		
			AddressVerified addressVer = null;
			Address address = null;
			if (catalogueRecordDcatAp.get("address") != null) {
				String addressCountry = catalogueRecordDcatAp.get("address").get("addressCountry") != null
						? catalogueRecordDcatAp.get("address").get("addressCountry").textValue()
						: null;
				String addressLocality = catalogueRecordDcatAp.get("address").get("addressLocality") != null
						? catalogueRecordDcatAp.get("address").get("addressLocality").textValue()
						: null;
				String addressRegion = catalogueRecordDcatAp.get("address").get("addressRegion") != null
						? catalogueRecordDcatAp.get("address").get("addressRegion").textValue()
						: null;
				String district = catalogueRecordDcatAp.get("address").get("district") != null
						? catalogueRecordDcatAp.get("address").get("district").textValue()
						: null;
				String postOfficeBoxNumber = catalogueRecordDcatAp.get("address").get("postOfficeBoxNumber") != null
						? catalogueRecordDcatAp.get("address").get("postOfficeBoxNumber").textValue()
						: null;
				String postalCode = catalogueRecordDcatAp.get("address").get("postalCode") != null
						? catalogueRecordDcatAp.get("address").get("postalCode").textValue()
						: null;
				String streetAddress = catalogueRecordDcatAp.get("address").get("streetAddress") != null
						? catalogueRecordDcatAp.get("address").get("streetAddress").textValue()
						: null;
				String streetNr = catalogueRecordDcatAp.get("address").get("streetNr") != null
						? catalogueRecordDcatAp.get("address").get("streetNr").textValue()
						: null;
				address = new Address(addressCountry, addressLocality, addressRegion, district, postOfficeBoxNumber,
						postalCode, streetAddress, streetNr);
				addressVer = new AddressVerified(address, true);
			}
			String alternateName=catalogueRecordDcatAp.get("alternateName") != null ? catalogueRecordDcatAp.get("alternateName").textValue():null;
			
			String applicationProfile=catalogueRecordDcatAp.get("applicationProfile") != null ? catalogueRecordDcatAp.get("applicationProfile").textValue():null;
			
			String areaServed=catalogueRecordDcatAp.get("areaServed") != null ? catalogueRecordDcatAp.get("areaServed").textValue():null;
			
			String changeType=catalogueRecordDcatAp.get("changeType") != null ? catalogueRecordDcatAp.get("changeType").textValue():null;
			 
			String dataProvider=catalogueRecordDcatAp.get("dataProvider") != null ? catalogueRecordDcatAp.get("dataProvider").textValue():null;
			
			String dateCreated=catalogueRecordDcatAp.get("dateCreated") != null ? catalogueRecordDcatAp.get("dateCreated").textValue():null;
			
			String dateModified=catalogueRecordDcatAp.get("dateModified") != null ? catalogueRecordDcatAp.get("dateModified").textValue():null;
			
			String description=catalogueRecordDcatAp.get("description") != null ? catalogueRecordDcatAp.get("description").textValue():null;
			
			
			String[] language = null;
			if (catalogueRecordDcatAp.get("language") != null && catalogueRecordDcatAp.get("language").isArray()) {
				language = new String[(catalogueRecordDcatAp.get("language").size())];
				int i = 0;
				for (JsonNode c : catalogueRecordDcatAp.get("language")) {
					language[i] = c.toString();
					i++;
				}

			}
			String listingDate=catalogueRecordDcatAp.get("listingDate") != null ? catalogueRecordDcatAp.get("listingDate").textValue():null;
			
			Point location = null;
			if (catalogueRecordDcatAp.get("location") != null) {
				location = new Point();

				Double coordinate[] = new Double[2];
				int j = 0;
				for (JsonNode coord : catalogueRecordDcatAp.get("location").get("coordinates")) {
					coordinate[j] = coord.asDouble();
					j++;
				}

				location = new Point("Point", coordinate);

			}
			String name=catalogueRecordDcatAp.get("name") != null ? catalogueRecordDcatAp.get("name").textValue():null;
			
	
			String[] owner = null;
			if (catalogueRecordDcatAp.get("owner") != null && catalogueRecordDcatAp.get("owner").isArray()) {
				owner = new String[(catalogueRecordDcatAp.get("owner").size())];
				int i = 0;
				for (JsonNode c : catalogueRecordDcatAp.get("owner")) {
					owner[i] = c.toString();
					i++;
				}

			}
			String seeAlso=catalogueRecordDcatAp.get("seeAlso") != null ? catalogueRecordDcatAp.get("seeAlso").textValue():null;
			
			String source=catalogueRecordDcatAp.get("source") != null ? catalogueRecordDcatAp.get("source").textValue():null;
			
			String sourceMetadata=catalogueRecordDcatAp.get("sourceMetadata") != null ? catalogueRecordDcatAp.get("sourceMetadata").textValue():null;
			
		
			String[] title = null;
			if (catalogueRecordDcatAp.get("title") != null && catalogueRecordDcatAp.get("title").isArray()) {
				title = new String[(catalogueRecordDcatAp.get("title").size())];
				int i = 0;
				for (JsonNode c : catalogueRecordDcatAp.get("title")) {
					title[i] = c.toString();
					i++;
				}

			}
			
			
			CatalogueRecordDcatAp catalogueRecordNgsi = new CatalogueRecordDcatAp(id,modificationDate,primaryTopic,type,addressVer,alternateName,applicationProfile,areaServed,
					changeType,dataProvider,dateCreated,dateModified,description,language,listingDate,location,name,owner,seeAlso,source,sourceMetadata,title);
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			entity = mapper.writeValueAsString(catalogueRecordNgsi);
		
		} catch (Exception ex) {

		}
		return entity;
	}

}