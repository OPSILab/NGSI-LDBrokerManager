/*
 * package it.eng.ngsild.broker.manager.service;
 * 
 * 
 * 
 * import org.springframework.stereotype.Component;
 * 
 * import it.eng.ngsild.broker.manager.model.AgentDcatAp; import
 * it.eng.ngsild.broker.manager.model.CatalogueDcatAp; import
 * it.eng.ngsild.broker.manager.model.CatalogueRecordDcatAp; import
 * it.eng.ngsild.broker.manager.model.DataServiceDcatAp; import
 * it.eng.ngsild.broker.manager.model.Dataset; import
 * it.eng.ngsild.broker.manager.model.DistributionDcatAp;
 * 
 * 
 * 
 * 
 * //classe che esegue la conversione di un oggetto DCAT-AP per NGSI-LD:
 * 
 * @Component public class DcatApToNgsiLdConverter {
 * 
 * public Dataset convertDataset(Dataset dataset) {
 * 
 * Dataset ngsiDataset = new Dataset();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD Dataset (credo)
 * ngsiDataset.setId(dataset.getId()); ngsiDataset.setType(dataset.getType());
 * ngsiDataset.setTitle(dataset.getTitle());
 * ngsiDataset.setDatasetDescription(dataset.getDatasetDescription());
 * ngsiDataset.setDatasetDistribution(dataset.getDatasetDistribution());
 * ngsiDataset.setDatasetSource(dataset.getDatasetSource());
 * ngsiDataset.setDatasetType(dataset.getDatasetType());
 * ngsiDataset.setDateCreated(dataset.getDateCreated());
 * ngsiDataset.setDateModified(dataset.getDateModified());
 * ngsiDataset.setDescription(dataset.getDescription());
 * ngsiDataset.setFrequency(dataset.getFrequency());
 * ngsiDataset.setIdentifier(dataset.getIdentifier());
 * ngsiDataset.setKeyword(dataset.getKeyword());
 * ngsiDataset.setLandingPage(dataset.getLandingPage());
 * ngsiDataset.setLanguage(dataset.getLanguage());
 * ngsiDataset.setName(dataset.getName());
 * ngsiDataset.setOtherIdentifier(dataset.getOtherIdentifier());
 * ngsiDataset.setOwner(dataset.getOwner());
 * ngsiDataset.setPublisher(dataset.getPublisher());
 * ngsiDataset.setRelatedResource(dataset.getRelatedResource());
 * ngsiDataset.setReleaseDate(dataset.getReleaseDate());
 * ngsiDataset.setSeeAlso(dataset.getSeeAlso());
 * ngsiDataset.setSource(dataset.getSource());
 * ngsiDataset.setSpatial(dataset.getSpatial());
 * ngsiDataset.setSpatialResolution(dataset.getSpatialResolution());
 * ngsiDataset.setTemporal(dataset.getTemporal());
 * ngsiDataset.setTemporalResolution(dataset.getTemporalResolution());
 * ngsiDataset.setTheme(dataset.getTheme());
 * ngsiDataset.setTitleArray(dataset.getTitleArray());
 * ngsiDataset.setUpdateDate(dataset.getUpdateDate());
 * ngsiDataset.setVersion(dataset.getVersion());
 * ngsiDataset.setVersionNotes(dataset.getVersionNotes());
 * ngsiDataset.setWasGeneratedBy(dataset.getWasGeneratedBy()); return
 * ngsiDataset;
 * 
 * 
 * // il metodo "convertDataset" accetta un oggetto "Dataset" e restituisce
 * un'entità NGSI-LD corrispondente. //Questo processo di mappatura delle
 * proprietà di un dataset di tipo DCAT-AP a un dataset di tipo NGSI-LD
 * //consente di convertire il formato di un dataset in modo che possa essere
 * utilizzato da altri sistemi che //supportano il formato NGSI-LD. }
 * 
 * 
 * public AgentDcatAp convertAgentDcatAp(AgentDcatAp agentDcatAp) {
 * 
 * AgentDcatAp ngsiAgentDcatAp = new AgentDcatAp();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD AgentDcatAp(credo)
 * 
 * ngsiAgentDcatAp.setId(agentDcatAp.getId());
 * ngsiAgentDcatAp.setType(agentDcatAp.getType());
 * ngsiAgentDcatAp.setAgentName(agentDcatAp.getAgentName());
 * ngsiAgentDcatAp.setDescription(agentDcatAp.getDescription());
 * ngsiAgentDcatAp.setAgentType(agentDcatAp.getAgentType());
 * ngsiAgentDcatAp.setAlternateName(agentDcatAp.getAlternateName());
 * ngsiAgentDcatAp.setAreaServed(agentDcatAp.getAreaServed());
 * ngsiAgentDcatAp.setDataProvider(agentDcatAp.getDataProvider());
 * ngsiAgentDcatAp.setDateCreated(agentDcatAp.getDateCreated());
 * ngsiAgentDcatAp.setDateModified(agentDcatAp.getDateModified());
 * ngsiAgentDcatAp.setLocation(agentDcatAp.getLocation());
 * ngsiAgentDcatAp.setName(agentDcatAp.getName());
 * ngsiAgentDcatAp.setOwner(agentDcatAp.getOwner());
 * ngsiAgentDcatAp.setSeeAlso(agentDcatAp.getSeeAlso());
 * ngsiAgentDcatAp.setSource(agentDcatAp.getSource()); //ci sarebbe un dato di
 * nome : typeNGSI ma non so come impostarlo. return ngsiAgentDcatAp; }
 * 
 * 
 * public CatalogueDcatAp convertCatalogueDcatAp(CatalogueDcatAp
 * catalogueDcatAp) {
 * 
 * CatalogueDcatAp ngsiCatalogueDcatAp = new CatalogueDcatAp();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD CatalogueDcatAp (credo)
 * ngsiCatalogueDcatAp.setId(catalogueDcatAp.getId());
 * ngsiCatalogueDcatAp.setType(catalogueDcatAp.getType());
 * ngsiCatalogueDcatAp.setDescription(catalogueDcatAp.getDescription());
 * ngsiCatalogueDcatAp.setModificationDate(catalogueDcatAp.getModificationDate()
 * ); ngsiCatalogueDcatAp.setTitle(catalogueDcatAp.getTitle());
 * ngsiCatalogueDcatAp.setAddress(catalogueDcatAp.getAddress());
 * ngsiCatalogueDcatAp.setAlternateName(catalogueDcatAp.getAlternateName());
 * ngsiCatalogueDcatAp.setAreaServed(catalogueDcatAp.getAreaServed());
 * ngsiCatalogueDcatAp.setCatalogue(catalogueDcatAp.getCatalogue());
 * ngsiCatalogueDcatAp.setCreator(catalogueDcatAp.getCreator());
 * ngsiCatalogueDcatAp.setDataProvider(catalogueDcatAp.getDataProvider());
 * ngsiCatalogueDcatAp.setDataset(catalogueDcatAp.getDataset());
 * ngsiCatalogueDcatAp.setDateCreated(catalogueDcatAp.getDateCreated());
 * ngsiCatalogueDcatAp.setDateModified(catalogueDcatAp.getDateModified());
 * ngsiCatalogueDcatAp.setHasPart(catalogueDcatAp.getHasPart());
 * ngsiCatalogueDcatAp.setHomepage(catalogueDcatAp.getHomepage());
 * ngsiCatalogueDcatAp.setLanguage(catalogueDcatAp.getLanguage());
 * ngsiCatalogueDcatAp.setLicence(catalogueDcatAp.getLicence());
 * ngsiCatalogueDcatAp.setLocation(catalogueDcatAp.getLocation());
 * ngsiCatalogueDcatAp.setName(catalogueDcatAp.getName());
 * ngsiCatalogueDcatAp.setOwner(catalogueDcatAp.getOwner());
 * ngsiCatalogueDcatAp.setPublisher(catalogueDcatAp.getPublisher());
 * ngsiCatalogueDcatAp.setRecord(catalogueDcatAp.getRecord());
 * ngsiCatalogueDcatAp.setReleaseDate(catalogueDcatAp.getReleaseDate());
 * ngsiCatalogueDcatAp.setRights(catalogueDcatAp.getRights());
 * ngsiCatalogueDcatAp.setSeeAlso(catalogueDcatAp.getSeeAlso());
 * ngsiCatalogueDcatAp.setService(catalogueDcatAp.getService());
 * ngsiCatalogueDcatAp.setSource(catalogueDcatAp.getSource());
 * ngsiCatalogueDcatAp.setSpatial_geographic(catalogueDcatAp.
 * getSpatial_geographic());
 * ngsiCatalogueDcatAp.setThemes(catalogueDcatAp.getThemes());
 * 
 * return ngsiCatalogueDcatAp; }
 * 
 * 
 * public CatalogueRecordDcatAp
 * convertCatalogueRecordDcatAp(CatalogueRecordDcatAp catalogueRecordDcatAp) {
 * 
 * CatalogueRecordDcatAp ngsiCatalogueRecordDcatAp = new
 * CatalogueRecordDcatAp();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD CatalogueRecordDcatAp
 * (credo) ngsiCatalogueRecordDcatAp.setId(catalogueRecordDcatAp.getId());
 * ngsiCatalogueRecordDcatAp.setType(catalogueRecordDcatAp.getType());
 * ngsiCatalogueRecordDcatAp.setModificationDate(catalogueRecordDcatAp.
 * getModificationDate());
 * ngsiCatalogueRecordDcatAp.setPrimaryTopic(catalogueRecordDcatAp.
 * getPrimaryTopic());
 * ngsiCatalogueRecordDcatAp.setAddress(catalogueRecordDcatAp.getAddress());
 * ngsiCatalogueRecordDcatAp.setAlternateName(catalogueRecordDcatAp.
 * getAlternateName());
 * ngsiCatalogueRecordDcatAp.setApplicationProfile(catalogueRecordDcatAp.
 * getApplicationProfile());
 * ngsiCatalogueRecordDcatAp.setAreaServed(catalogueRecordDcatAp.getAreaServed()
 * );
 * ngsiCatalogueRecordDcatAp.setChangeType(catalogueRecordDcatAp.getChangeType()
 * ); ngsiCatalogueRecordDcatAp.setDataProvider(catalogueRecordDcatAp.
 * getDataProvider());
 * ngsiCatalogueRecordDcatAp.setDateCreated(catalogueRecordDcatAp.getDateCreated
 * ()); ngsiCatalogueRecordDcatAp.setDateModified(catalogueRecordDcatAp.
 * getDateModified());
 * ngsiCatalogueRecordDcatAp.setDescription(catalogueRecordDcatAp.getDescription
 * ());
 * ngsiCatalogueRecordDcatAp.setLanguage(catalogueRecordDcatAp.getLanguage());
 * ngsiCatalogueRecordDcatAp.setListingDate(catalogueRecordDcatAp.getListingDate
 * ());
 * ngsiCatalogueRecordDcatAp.setLocation(catalogueRecordDcatAp.getLocation());
 * ngsiCatalogueRecordDcatAp.setName(catalogueRecordDcatAp.getName());
 * ngsiCatalogueRecordDcatAp.setOwner(catalogueRecordDcatAp.getOwner());
 * ngsiCatalogueRecordDcatAp.setSeeAlso(catalogueRecordDcatAp.getSeeAlso());
 * ngsiCatalogueRecordDcatAp.setSource(catalogueRecordDcatAp.getSource());
 * ngsiCatalogueRecordDcatAp.setSourceMetadata(catalogueRecordDcatAp.
 * getSourceMetadata());
 * ngsiCatalogueRecordDcatAp.setTitle(catalogueRecordDcatAp.getTitle());
 * 
 * return ngsiCatalogueRecordDcatAp; }
 * 
 * 
 * 
 * 
 * public DistributionDcatAp convertDistributionDcatAp(DistributionDcatAp
 * distributionDcatAp) {
 * 
 * DistributionDcatAp ngsiDistributionDcatAp = new DistributionDcatAp();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD DistributionDcatAp (credo)
 * ngsiDistributionDcatAp.setId(distributionDcatAp.getId());
 * ngsiDistributionDcatAp.setType(distributionDcatAp.getType());
 * ngsiDistributionDcatAp.setAccessService(distributionDcatAp.getAccessService()
 * ); ngsiDistributionDcatAp.setAccessUrl(distributionDcatAp.getAccessUrl());
 * ngsiDistributionDcatAp.setAddress(distributionDcatAp.getAddress());
 * ngsiDistributionDcatAp.setAlternateName(distributionDcatAp.getAlternateName()
 * ); ngsiDistributionDcatAp.setAreaServed(distributionDcatAp.getAreaServed());
 * ngsiDistributionDcatAp.setAvailability(distributionDcatAp.getAvailability());
 * ngsiDistributionDcatAp.setByteSize(distributionDcatAp.getByteSize());
 * ngsiDistributionDcatAp.setChecksum(distributionDcatAp.getChecksum());
 * ngsiDistributionDcatAp.setCompressionFormat(distributionDcatAp.
 * getCompressionFormat());
 * ngsiDistributionDcatAp.setDataProvider(distributionDcatAp.getDataProvider());
 * ngsiDistributionDcatAp.setDateCreated(distributionDcatAp.getDateCreated());
 * ngsiDistributionDcatAp.setDateModified(distributionDcatAp.getDateModified());
 * ngsiDistributionDcatAp.setDescription(distributionDcatAp.getDescription());
 * ngsiDistributionDcatAp.setDocumentation(distributionDcatAp.getDocumentation()
 * );
 * ngsiDistributionDcatAp.setDownloadURL(distributionDcatAp.getDownloadURL());
 * ngsiDistributionDcatAp.setFormat(distributionDcatAp.getFormat());
 * ngsiDistributionDcatAp.setHasPolicy(distributionDcatAp.getHasPolicy());
 * ngsiDistributionDcatAp.setLanguage(distributionDcatAp.getLanguage());
 * ngsiDistributionDcatAp.setLicense(distributionDcatAp.getLicense());
 * ngsiDistributionDcatAp.setLinkedSchemas(distributionDcatAp.getLinkedSchemas()
 * ); ngsiDistributionDcatAp.setLocation(distributionDcatAp.getLocation());
 * ngsiDistributionDcatAp.setMediaType(distributionDcatAp.getMediaType());
 * ngsiDistributionDcatAp.setModifiedDate(distributionDcatAp.getModifiedDate());
 * ngsiDistributionDcatAp.setName(distributionDcatAp.getName());
 * ngsiDistributionDcatAp.setOwner(distributionDcatAp.getOwner());
 * ngsiDistributionDcatAp.setPackagingFormat(distributionDcatAp.
 * getPackagingFormat());
 * ngsiDistributionDcatAp.setReleaseDate(distributionDcatAp.getReleaseDate());
 * ngsiDistributionDcatAp.setRights(distributionDcatAp.getRights());
 * ngsiDistributionDcatAp.setSeeAlso(distributionDcatAp.getSeeAlso());
 * ngsiDistributionDcatAp.setSource(distributionDcatAp.getSource());
 * ngsiDistributionDcatAp.setSpatialResolution(distributionDcatAp.
 * getSpatialResolution());
 * ngsiDistributionDcatAp.setStatus(distributionDcatAp.getStatus());
 * ngsiDistributionDcatAp.setTemporalResolution(distributionDcatAp.
 * getTemporalResolution());
 * ngsiDistributionDcatAp.setTitle(distributionDcatAp.getTitle());
 * 
 * return ngsiDistributionDcatAp; }
 * 
 * 
 * 
 * 
 * public DataServiceDcatAp convertDataServiceDcatAp(DataServiceDcatAp
 * dataServiceDcatAp) {
 * 
 * DataServiceDcatAp ngsiDataServiceDcatAp = new DataServiceDcatAp();
 * 
 * // Cosi mi mappo le proprietà di DCAT-AP a NGSI-LD DataServiceDcatAp (credo)
 * ngsiDataServiceDcatAp.setAccessRights(dataServiceDcatAp.getAccessRights());
 * ngsiDataServiceDcatAp.setAddress(dataServiceDcatAp.getAddress());
 * ngsiDataServiceDcatAp.setAlternateName(dataServiceDcatAp.getAlternateName());
 * ngsiDataServiceDcatAp.setAreaServed(dataServiceDcatAp.getAreaServed());
 * ngsiDataServiceDcatAp.setDataProvider(dataServiceDcatAp.getDataProvider());
 * ngsiDataServiceDcatAp.setDataServiceDescription(dataServiceDcatAp.
 * getDataServiceDescription());
 * ngsiDataServiceDcatAp.setDateCreated(dataServiceDcatAp.getDateCreated());
 * ngsiDataServiceDcatAp.setDateModified(dataServiceDcatAp.getDateModified());
 * ngsiDataServiceDcatAp.setDescription(dataServiceDcatAp.getDescription());
 * ngsiDataServiceDcatAp.setEndPointDescription(dataServiceDcatAp.
 * getEndPointDescription());
 * ngsiDataServiceDcatAp.setEndPointURL(dataServiceDcatAp.getEndPointURL());
 * ngsiDataServiceDcatAp.setId(dataServiceDcatAp.getId());
 * ngsiDataServiceDcatAp.setLicense(dataServiceDcatAp.getLicense());
 * ngsiDataServiceDcatAp.setLocation(dataServiceDcatAp.getLocation());
 * ngsiDataServiceDcatAp.setName(dataServiceDcatAp.getName());
 * ngsiDataServiceDcatAp.setOwner(dataServiceDcatAp.getOwner());
 * ngsiDataServiceDcatAp.setSeeAlso(dataServiceDcatAp.getSeeAlso());
 * ngsiDataServiceDcatAp.setServesDataset(dataServiceDcatAp.getServesDataset());
 * ngsiDataServiceDcatAp.setSource(dataServiceDcatAp.getSource());
 * ngsiDataServiceDcatAp.setTitle(dataServiceDcatAp.getTitle());
 * ngsiDataServiceDcatAp.setType(dataServiceDcatAp.getType());
 * 
 * // ...
 * 
 * return ngsiDataServiceDcatAp; }
 * 
 * 
 * 
 * }
 */