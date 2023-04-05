package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dataset {
    private AccessRight accessRight;
    private String alternateName;
    private String[] contactPoint;
    private String creator;
    private String dataProvider;
    private String[] datasetDescription;
    private DistributionDcatAp[] datasetDistribution;
    private String[] datasetSource;
    private String datasetType;
    private String dateCreated;
    private String dateModified;
    private String description;
    private String[] documentation;
    private String frequency;
    private String[] hasVersion;
    private String id;
    private String[] identifier;
    private String[] isReferencedBy;
    private String[] isVersionOf;
    private String[] keyword;
    private String[] landingPage;
    private String[] language;
    private String name;
    private String[] otherIdentifier;
    private String[] owner;
    private String[] provenance;
    private AgentDcatAp publisher;
    private String[] qualifiedAttribution;
    private String[] qualifiedRelation;
    private String[] relatedResource;
    private String releaseDate;
    private String[] sample;
    private String seeAlso;
    private String source;
    private String[] spatial;
    private String[] temporalResolution;
    private String[] theme;
    private String[] title;
    private String type;
    private String updateDate;
    private String version;
    private String[] versionNotes;
    private String[] wasGeneratedBy;
      
    
}
