package it.eng.ngsild.broker.manager.model;

import java.util.Arrays;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
public class AgentDcatAp {
	public String[] agentName;
	public String id;
	public String type = "AgentDCAT-AP";
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified addressVerified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String agentType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point[]location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;

	

	public AgentDcatAp() {

	}
	



	public AgentDcatAp(String[] agentName, String id, String type, AddressVerified address, String agentType,
			String alternateName, String areaServed, String dataProvider, String dateCreated, String dateModified,
			String description, Point[] location, String name, String[] owner, String[] seeAlso, String source) {
		super();
		this.agentName = agentName;
		this.id = id;
		this.type = type;
		this.addressVerified = address;
		this.agentType = agentType;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.dataProvider = dataProvider;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.seeAlso = seeAlso;
		this.source = source;
	}




	@Override
	public String toString() {
		return "AgentDcatAp [agentName=" + Arrays.toString(agentName) + ", id=" + id + ", type=" + type + ", address="
				+ addressVerified + ", agentType=" + agentType + ", alternateName=" + alternateName + ", areaServed="
				+ areaServed + ", dataProvider=" + dataProvider + ", dateCreated=" + dateCreated + ", dateModified="
				+ dateModified + ", description=" + description + ", location=" + Arrays.toString(location) + ", name="
				+ name + ", owner=" + Arrays.toString(owner) + ", seeAlso=" + Arrays.toString(seeAlso) + ", source="
				+ source + "]";
	}
	

	public String convertToNgsi(JsonNode agent) {
    org.json.JSONObject input = new org.json.JSONObject(agent.toString());
    String identifier = input.optString("id", java.util.UUID.randomUUID().toString());
    String id = identifier.contains(":") ? identifier : "urn:ngsi-ld:AgentDCAT-AP:id:" + identifier;
    return it.eng.ngsild.broker.manager.service.NgsiDcatMapper.fromJson(input,id,"AgentDCAT-AP").toString();
  }

}
