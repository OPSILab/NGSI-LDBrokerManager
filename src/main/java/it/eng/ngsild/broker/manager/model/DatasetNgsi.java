package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatasetNgsi {
    private AccessRight accessRight;
    private Property alternateName;
    private Property[] contactPoint;
    private Property creator;
    private Property dataProvider;
    private Property[] datasetDescription;
    private DistributionDcatAp[] datasetDistribution;
    private Property[] datasetSource;
    private Property datasetType;
    private Property dateCreated;
    private Property dateModified;
    private Property description;
    private Property[] documentation;
    private Property frequency;
    private Property[] hasVersion;
    private Property id;
    private Property[] identifier;
    private Property[] isReferencedBy;
    private Property[] isVersionOf;
    private Property[] keyword;
    private Property[] landingPage;
    private Property[] language;
    private Property name;
    private Property[] otherIdentifier;
    private Property[] owner;
    private Property[] provenance;
    private AgentDcatAp publisher;
    private Property[] qualifiedAttribution;
    private Property[] qualifiedRelation;
    private Property[] relatedResource;
    private Property releaseDate;
    private Property[] sample;
    private Property seeAlso;
    private Property source;
    private Property[] spatial;
    private Property[] temporalResolution;
    private Property[] theme;
    private Property[] title;
    private Property type;
    private Property updateDate;
    private Property version;
    private Property[] versionNotes;
    private Property[] wasGeneratedBy;
     
}
