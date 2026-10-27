package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class CatalogueDcatAp {
	public String id;
	public String type;
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String modificationDate;
	public String[] title;
	public String[] dataset;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified address;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] catalogue;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String creator;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] hasPart;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String homepage;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] language;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String licence;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	public String publisher;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] record;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String releaseDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String rights;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] service;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point[] spatial_geographic;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] themes;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String isPartOf;

	public CatalogueDcatAp(String id, String type, String description, String modificationDate, String[] title,
			String[] dataset, AddressVerified address, String alternateName, String areaServed, String[] catalogue,
			String creator, String dataProvider, String dateCreated, String dateModified, String[] hasPart,
			String homepage, String[] language, String licence, Point location, String name, String[] owner,
			String publisher, String[] record, String releaseDate, String rights, String[] seeAlso, String[] service,
			String source, Point[] spatial_geographic, String[] themes, String isPartOf) {
		super();
		this.id = id;
		this.type = type;
		this.description = description;
		this.modificationDate = modificationDate;
		this.title = title;
		this.dataset = dataset;
		this.address = address;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.catalogue = catalogue;
		this.creator = creator;
		this.dataProvider = dataProvider;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.hasPart = hasPart;
		this.homepage = homepage;
		this.language = language;
		this.licence = licence;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.publisher = publisher;
		this.record = record;
		this.releaseDate = releaseDate;
		this.rights = rights;
		this.seeAlso = seeAlso;
		this.service = service;
		this.source = source;
		this.spatial_geographic = spatial_geographic;
		this.themes = themes;
		this.isPartOf = isPartOf;
	}

	public CatalogueDcatAp() {

	}

	public String convertToNgsi(JsonNode catalogueDcatAp) {
    org.json.JSONObject input = new org.json.JSONObject(catalogueDcatAp.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String id = identifier.contains(":") ? identifier : "urn:ngsi-ld:CatalogueDCAT-AP:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input,id,"CatalogueDCAT-AP").toString();
  }

}