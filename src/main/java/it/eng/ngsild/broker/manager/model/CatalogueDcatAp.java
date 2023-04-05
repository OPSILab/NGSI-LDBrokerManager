package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueDcatAp {
	private String id;
    private String type;
    private String description;
    private String modificationDate;
    private String title;
    private Address address;
    private String alternateName;
    private String areaServed;
    private String[] catalogue;
    private String creator;
    private String dataProvider;
    private Dataset[] dataset;
    private String dateCreated;
    private String dateModified;
    private String[] hasPart;
    private String homepage;
    private String[] language;
    private String licence;
    private Object location;
    private String name;
    private String[] owner;
    private AgentDcatAp publisher;
    private String[] record;
    private String releaseDate;
    private String rights;
    private String[] seeAlso;
    private String[] service;
    private String source;
    private String[] spatial_geographic;
    private String[] themes;
    
}