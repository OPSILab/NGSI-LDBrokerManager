package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentDcatAp {
	private String agentName;
	private String id;
	private String type;
	private Address address;
	private String[] agentNameArray;
	private String agentType;
	private String alternateName;
	private String areaServed;
	private String dataProvider;
	private String dateCreated;
	private String dateModified;
	private String description;
	private String[] location;
	private String name;
	private String[] owner;
	private String[] seeAlso;
	private String source;
	private String typeNGSI;

	

	
}
