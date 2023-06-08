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
		String entity = null;
		try {
			String type = "AgentDCAT-AP";
		String idDataset = agent.get("id") != null ? agent.get("id").textValue() : UUID.randomUUID().toString();
		String id = "urn:ngsi-ld:AgentDCAT-AP:id:" + idDataset;
		 String[] agentName= null;
			if (agent.get("agentName") != null && agent.get("agentName").isArray()) {
				agentName = new String[(agent.get("agentName").size())];
				int i = 0;
				for (JsonNode c : agent.get("agentName")) {
					agentName[i] = c.toString();
					i++;
				}

			}
			AddressVerified addressVer =null;
			Address address= null;
		if(agent.get("address")!=null) {
			System.out.println(agent.get("address"));
			System.out.println(agent.get("address").get("addressCountry"));
			 String addressCountry=agent.get("address").get("addressCountry") != null ? agent.get("address").get("addressCountry").textValue() : null;
			    String addressLocality=agent.get("address").get("addressLocality") != null ? agent.get("address").get("addressLocality").textValue() : null;
			    String addressRegion=agent.get("address").get("addressRegion") != null ? agent.get("address").get("addressRegion").textValue() : null;
			     String district=agent.get("address").get("district") != null ? agent.get("address").get("district").textValue() : null;
			    String postOfficeBoxNumber=agent.get("address").get("postOfficeBoxNumber") != null ? agent.get("address").get("postOfficeBoxNumber").textValue() : null;
			    String postalCode=agent.get("address").get("postalCode") != null ? agent.get("address").get("postalCode").textValue() : null;
			    String streetAddress=agent.get("address").get("streetAddress") != null ? agent.get("address").get("streetAddress").textValue() : null;
			    String streetNr=agent.get("address").get("streetNr") != null ? agent.get("address").get("streetNr").textValue() : null;
				 address = new Address(addressCountry,addressLocality,addressRegion,district,postOfficeBoxNumber,postalCode,streetAddress,streetNr);
				addressVer = new AddressVerified(address, true);
		}
	String[] agentNameArray = null;
		if (agent.get("agentNameArray") != null && agent.get("agentNameArray").isArray()) {
			agentNameArray = new String[(agent.get("agentNameArray").size())];
			int i = 0;
			for (JsonNode c : agent.get("agentNameArray")) {
				agentNameArray[i] = c.toString();
				i++;
			}

		}
		String agentType = agent.get("agentType") != null ? agent.get("agentType").toString() : null;
		String alternateName = agent.get("alternateName") != null ? agent.get("alternateName").toString() : null;
		String areaServed = agent.get("areaServed") != null ? agent.get("areaServed").toString() : null;
		String dataProvider = agent.get("dataProvider") != null ? agent.get("dataProvider").toString() : null;
		String dateCreated = agent.get("dateCreated") != null ? agent.get("dateCreated").toString() : null;
		String dateModified = agent.get("dateModified") != null ? agent.get("dateModified").toString() : null;
		String description = agent.get("description") != null ? agent.get("description").toString() : null;
		
		Point[] location = null;
		if (agent.get("location") != null && agent.get("location").isArray()) {
			location= new Point[agent.get("location").size()];
			int i = 0;
			for (JsonNode c : agent.get("location")) {
				Double coordinate[] = new Double[2];
				int j = 0;
				for (JsonNode coord : c.get("coordinates")) {
					coordinate[j] = coord.asDouble();
					j++;
				}
				
				location[i] = new Point("Point",coordinate);
				i++;
			}
		}
		String name = agent.get("name") != null ? agent.get("name").toString() : null;
		String[] owner = null;
		if (agent.get("owner") != null && agent.get("owner").isArray()) {
			owner = new String[(agent.get("owner").size())];
			int i = 0;
			for (JsonNode c : agent.get("owner")) {
				owner[i] = c.toString();
				i++;
			}

		}
		
		String[] seeAlso = null;
		if (agent.get("seeAlso") != null && agent.get("seeAlso").isArray()) {
			seeAlso = new String[(agent.get("seeAlso").size())];
			int i = 0;
			for (JsonNode c : agent.get("seeAlso")) {
				seeAlso[i] = c.toString();
				i++;
			}

		}
		String source = agent.get("source") != null ? agent.get("source").toString() : null;
		String typeNGSI = agent.get("typeNGSI") != null ? agent.get("typeNGSI").toString() : null;

	   AgentDcatAp agentNgsiLd = new AgentDcatAp(agentName, id, type, addressVer,
				 agentType, alternateName,  areaServed, dataProvider, dateCreated,
				dateModified,  description, location, name, owner, seeAlso,
				source);
	   System.out.println(agentNgsiLd);
		ObjectMapper mapper = new ObjectMapper();

		
		entity = mapper.writeValueAsString(agentNgsiLd);
		   System.out.println(entity);
	
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			System.out.println(ex.getMessage());
		}

		return entity;

	}

}
