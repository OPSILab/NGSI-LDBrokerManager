package it.eng.ngsild.broker.manager.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataServiceDcatAp {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String accessRights;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified address;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] dataServiceDescription;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] endPointDescription;
	public String[] endPointURL;
	public String id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String license;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] servesDataset;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	public String[] title;
	public String type;

	public DataServiceDcatAp(String accessRights, AddressVerified address, String alternateName, String areaServed,
			String dataProvider, String[] dataServiceDescription, String dateCreated, String dateModified,
			String description, String[] endPointDescription, String[] endPointURL, String id, String license,
			Point location, String name, String[] owner, String[] seeAlso, String[] servesDataset, String source,
			String[] title, String type) {
		super();
		this.accessRights = accessRights;
		this.address = address;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.dataProvider = dataProvider;
		this.dataServiceDescription = dataServiceDescription;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.description = description;
		this.endPointDescription = endPointDescription;
		this.endPointURL = endPointURL;
		this.id = id;
		this.license = license;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.seeAlso = seeAlso;
		this.servesDataset = servesDataset;
		this.source = source;
		this.title = title;
		this.type = type;
	}

	public DataServiceDcatAp() {

	}



	public String convertToNgsi(JsonNode dataServiceDcatAp) {
		String entity = null;
		try {
			String idDataset = dataServiceDcatAp.get("id") != null ? dataServiceDcatAp.get("id").textValue()
					: UUID.randomUUID().toString();
			String id = "urn:ngsi-ld:Dataset:id:" + idDataset;
			String type = "DataServiceDCAT-AP";
			String accessRights = dataServiceDcatAp.get("accessRights") != null ? dataServiceDcatAp.get("accessRights").textValue()
					: null;
		
			
			AddressVerified addressVer = null;
			Address address = null;
			if (dataServiceDcatAp.get("address") != null) {
				String addressCountry = dataServiceDcatAp.get("address").get("addressCountry") != null
						? dataServiceDcatAp.get("address").get("addressCountry").textValue()
						: null;
				String addressLocality = dataServiceDcatAp.get("address").get("addressLocality") != null
						? dataServiceDcatAp.get("address").get("addressLocality").textValue()
						: null;
				String addressRegion = dataServiceDcatAp.get("address").get("addressRegion") != null
						? dataServiceDcatAp.get("address").get("addressRegion").textValue()
						: null;
				String district = dataServiceDcatAp.get("address").get("district") != null
						? dataServiceDcatAp.get("address").get("district").textValue()
						: null;
				String postOfficeBoxNumber = dataServiceDcatAp.get("address").get("postOfficeBoxNumber") != null
						? dataServiceDcatAp.get("address").get("postOfficeBoxNumber").textValue()
						: null;
				String postalCode = dataServiceDcatAp.get("address").get("postalCode") != null
						? dataServiceDcatAp.get("address").get("postalCode").textValue()
						: null;
				String streetAddress = dataServiceDcatAp.get("address").get("streetAddress") != null
						? dataServiceDcatAp.get("address").get("streetAddress").textValue()
						: null;
				String streetNr = dataServiceDcatAp.get("address").get("streetNr") != null
						? dataServiceDcatAp.get("address").get("streetNr").textValue()
						: null;
				address = new Address(addressCountry, addressLocality, addressRegion, district, postOfficeBoxNumber,
						postalCode, streetAddress, streetNr);
				addressVer = new AddressVerified(address, true);
			}
			String alternateName = dataServiceDcatAp.get("alternateName") != null ? dataServiceDcatAp.get("alternateName").textValue(): null;
			String areaServed= dataServiceDcatAp.get("areaServed") != null ? dataServiceDcatAp.get("areaServed").textValue(): null;
			String dataProvider= dataServiceDcatAp.get("dataProvider") != null ? dataServiceDcatAp.get("dataProvider").textValue(): null;
			
			String[] dataServiceDescription = null;
			if (dataServiceDcatAp.get("dataServiceDcatAp") != null && dataServiceDcatAp.get("dataServiceDcatAp").isArray()) {
				dataServiceDescription = new String[(dataServiceDcatAp.get("dataServiceDescription").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("dataServiceDescription")) {
					dataServiceDescription[i] = c.toString();
					i++;
				}

			}
			String dateCreated= dataServiceDcatAp.get("dateCreated") != null ? dataServiceDcatAp.get("dateCreated").textValue(): null;
			String dateModified= dataServiceDcatAp.get("dateModified") != null ? dataServiceDcatAp.get("dateModified").textValue(): null;
			String description= dataServiceDcatAp.get("description") != null ? dataServiceDcatAp.get("description").textValue(): null;
			
			String[] endPointDescription = null;
			if (dataServiceDcatAp.get("endPointDescription") != null && dataServiceDcatAp.get("endPointDescription").isArray()) {
				endPointDescription = new String[(dataServiceDcatAp.get("endPointDescription").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("endPointDescription")) {
					endPointDescription[i] = c.toString();
					i++;
				}

			}
			
			String[] endPointURL = null;
			if (dataServiceDcatAp.get("endPointURL") != null && dataServiceDcatAp.get("endPointURL").isArray()) {
				endPointURL = new String[(dataServiceDcatAp.get("endPointURL").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("endPointURL")) {
					endPointURL[i] = c.toString();
					i++;
				}

			}

			String license= dataServiceDcatAp.get("license") != null ? dataServiceDcatAp.get("license").textValue(): null;
			Point location= null;
			if (dataServiceDcatAp.get("location") != null) {
				location = new Point();

				Double coordinate[] = new Double[2];
				int j = 0;
				for (JsonNode coord : dataServiceDcatAp.get("location").get("coordinates")) {
					coordinate[j] = coord.asDouble();
					j++;
				}

				location = new Point("Point", coordinate);

			}
			String name= dataServiceDcatAp.get("name") != null ? dataServiceDcatAp.get("name").textValue(): null;
			
			String[] owner = null;
			if (dataServiceDcatAp.get("owner") != null && dataServiceDcatAp.get("owner").isArray()) {
				owner = new String[(dataServiceDcatAp.get("owner").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("owner")) {
					owner[i] = c.toString();
					i++;
				}

			}
			
			
			String[] seeAlso = null;
			if (dataServiceDcatAp.get("seeAlso") != null && dataServiceDcatAp.get("seeAlso").isArray()) {
				seeAlso = new String[(dataServiceDcatAp.get("seeAlso").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("seeAlso")) {
					seeAlso[i] = c.toString();
					i++;
				}

			}
			
			String[] servesDataset = null;
			if (dataServiceDcatAp.get("servesDataset") != null && dataServiceDcatAp.get("servesDataset").isArray()) {
				servesDataset = new String[(dataServiceDcatAp.get("servesDataset").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("servesDataset")) {
					servesDataset[i] = c.toString();
					i++;
				}

			}
			String source= dataServiceDcatAp.get("source") != null ? dataServiceDcatAp.get("source").textValue(): null;
			
			String[] title = null;
			if (dataServiceDcatAp.get("title") != null && dataServiceDcatAp.get("title").isArray()) {
				title = new String[(dataServiceDcatAp.get("title").size())];
				int i = 0;
				for (JsonNode c : dataServiceDcatAp.get("title")) {
					title[i] = c.toString();
					i++;
				}

			}
			
			DataServiceDcatAp datasetNgsi = new DataServiceDcatAp(accessRights,addressVer, alternateName,  areaServed,
					 dataProvider, dataServiceDescription,  dateCreated,  dateModified,
					 description, endPointDescription,endPointURL, id,  license,
					 location,  name,  owner,  seeAlso,  servesDataset,  source,
					 title,  type);
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			entity = mapper.writeValueAsString(datasetNgsi);

			System.out.println(entity);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return entity;
	}
}