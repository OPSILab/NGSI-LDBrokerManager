package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DataServiceDcatAp {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String accessRights;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified address;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] dataServiceDescription;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] endPointDescription;
	public String[] endPointURL;
	public String id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String license;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] servesDataset;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	public String[] title;
	public String type;

	public DataServiceDcatAp(String accessRights, AddressVerified address, String alternateName, String areaServed,
			String dataProvider, String[] dataServiceDescription, String dateCreated, String dateModified,
			String description, String[] endPointDescription, String[] endPointURL, String id, String license,
			Point location, String name, String[] owner, String[] seeAlso, String[] servesDataset, String source,
			String[] title, String type) {
		super();
		this.accessRights = accessRights;
		this.address = address;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.dataProvider = dataProvider;
		this.dataServiceDescription = dataServiceDescription;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.endPointDescription = endPointDescription;
		this.endPointURL = endPointURL;
		this.id = id;
		this.license = license;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.seeAlso = seeAlso;
		this.servesDataset = servesDataset;
		this.source = source;
		this.title = title;
		this.type = type;
	}

	public DataServiceDcatAp() {

	}



	public String convertToNgsi(JsonNode dataServiceDcatAp) {
    org.json.JSONObject input = new org.json.JSONObject(dataServiceDcatAp.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String id = identifier.contains(":") ? identifier : "urn:ngsi-ld:DataServiceDCAT-AP:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input,id,"DataServiceDCAT-AP").toString();
  }
}