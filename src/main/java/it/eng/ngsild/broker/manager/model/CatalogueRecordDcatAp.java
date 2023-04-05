package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueRecordDcatAp {
	private String id;
	private String modificationDate;
	private String primaryTopic;
	private String type;
	private Address address;
	private String alternateName;
	private String applicationProfile;
	private String areaServed;
	private String changeType;
	private String dataProvider;
	private String dateCreated;
	private String dateModified;
	private String description;
	private String[] language;
	private String listingDate;
	private Object location;
	private String name;
	private String[] owner;
	private String seeAlso;
	private String source;
	private String sourceMetadata;
	private String[] title;
	
}