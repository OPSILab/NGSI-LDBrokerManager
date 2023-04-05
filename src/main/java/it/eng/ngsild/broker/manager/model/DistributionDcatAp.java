package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionDcatAp {
	   private String id;
	   private String type;
	   private String[] accessService;
	   private Address address;
	   private String[] accessUrl;
	   private String alternateName;
	   private String areaServed; 
	   private String availability; 
	   private String byteSize;
	   private String checksum;
	   private String compressionFormat; 
	   private String dataProvider;
	   private String dateCreated; 
	   private String dateModified;
	   private String description; 
	   private String[] documentation;
	   private String[] downloadURL;
	   private String format;
	   private String hasPolicy;
	   private String[] language;
	   private String license;
	   private String[] linkedSchemas;
	   private Object location;
	   private String mediaType;
	   private String modifiedDate;
	   private String name; 
	   private String[] owner; 
	   private String packagingFormat;
	   private String releaseDate;
	   private String rights;
	   private String seeAlso;
	   private String source;
	   private String[] spatialResolution;
	   private Status status;
	   private String[] temporalResolution ;
	   private String[] title;
	  
	
}
