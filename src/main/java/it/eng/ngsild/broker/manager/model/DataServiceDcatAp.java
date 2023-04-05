package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataServiceDcatAp {
	   private AccessRight accessRights;
	   private Address address;
	   private String alternateName;
	   private String areaServed;
	   private String dataProvider;
	   private String[] dataServiceDescription;
	   private String dateCreated;
	   private String dateModified;
	   private String description;
	   private String[] endPointDescription;
	   private String[] endPointURL;
	   private String[] id;
	   private String license;
	   private Object location;
	   private String name;
	   private String[] owner;
	   private String[] seeAlso;
	   private String[] servesDataset;
	   private String source;
	   private String[] title;
	   private String type;

	   }