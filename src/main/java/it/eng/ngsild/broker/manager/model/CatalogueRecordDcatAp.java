package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    org.json.JSONObject input = new org.json.JSONObject(catalogueRecordDcatAp.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String id = identifier.contains(":") ? identifier : "urn:ngsi-ld:CatalogueRecordDCAT-AP:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input,id,"CatalogueRecordDCAT-AP").toString();
  }

}