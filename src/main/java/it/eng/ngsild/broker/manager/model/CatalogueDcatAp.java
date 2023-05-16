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
public class CatalogueDcatAp {
	public String id;
	public String type;
	public String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String modificationDate;
	public String[] title;
	public String[] dataset;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public AddressVerified address;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String alternateName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String areaServed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] catalogue;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String creator;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dataProvider;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateCreated;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String dateModified;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] hasPart;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String homepage;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] language;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String licence;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point location;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] owner;
	public String publisher;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] record;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String releaseDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String rights;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] seeAlso;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] service;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String source;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Point[] spatial_geographic;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] themes;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String isPartOf;

	public CatalogueDcatAp(String id, String type, String description, String modificationDate, String[] title,
			String[] dataset, AddressVerified address, String alternateName, String areaServed, String[] catalogue,
			String creator, String dataProvider, String dateCreated, String dateModified, String[] hasPart,
			String homepage, String[] language, String licence, Point location, String name, String[] owner,
			String publisher, String[] record, String releaseDate, String rights, String[] seeAlso, String[] service,
			String source, Point[] spatial_geographic, String[] themes, String isPartOf) {
		super();
		this.id = id;
		this.type = type;
		this.description = description;
		this.modificationDate = modificationDate;
		this.title = title;
		this.dataset = dataset;
		this.address = address;
		this.alternateName = alternateName;
		this.areaServed = areaServed;
		this.catalogue = catalogue;
		this.creator = creator;
		this.dataProvider = dataProvider;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.hasPart = hasPart;
		this.homepage = homepage;
		this.language = language;
		this.licence = licence;
		this.location = location;
		this.name = name;
		this.owner = owner;
		this.publisher = publisher;
		this.record = record;
		this.releaseDate = releaseDate;
		this.rights = rights;
		this.seeAlso = seeAlso;
		this.service = service;
		this.source = source;
		this.spatial_geographic = spatial_geographic;
		this.themes = themes;
		this.isPartOf = isPartOf;
	}

	public CatalogueDcatAp() {

	}

	public String convertToNgsi(JsonNode catalogueDcatAp) {
		String entity = null;
		try {
			String idDataset = catalogueDcatAp.get("id") != null ? catalogueDcatAp.get("id").textValue()
					: UUID.randomUUID().toString();
			String id = "urn:ngsi-ld:CatalogueDCAT-AP:id:" + idDataset;
			String type = "CatalogueDCAT-AP";

			String description = catalogueDcatAp.get("description") != null
					? catalogueDcatAp.get("description").textValue()
					: null;
			String modificationDate = catalogueDcatAp.get("modificationDate") != null
					? catalogueDcatAp.get("modificationDate").textValue()
					: null;
			String[] title = null;
			if (catalogueDcatAp.get("title") != null && catalogueDcatAp.get("title").isArray()) {
				title = new String[(catalogueDcatAp.get("title").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("title")) {
					title[i] = c.toString();
					System.out.println(title[i]);
					i++;
				}

			}

			String[] dataset = null;
			if (catalogueDcatAp.get("dataset") != null && catalogueDcatAp.get("dataset").isArray()) {
				dataset = new String[(catalogueDcatAp.get("dataset").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("dataset")) {
					dataset[i] = c.toString();
					i++;
				}

			}

			AddressVerified addressVer = null;
			Address address = null;
			if (catalogueDcatAp.get("address") != null) {
				String addressCountry = catalogueDcatAp.get("address").get("addressCountry") != null
						? catalogueDcatAp.get("address").get("addressCountry").textValue()
						: null;
				String addressLocality = catalogueDcatAp.get("address").get("addressLocality") != null
						? catalogueDcatAp.get("address").get("addressLocality").textValue()
						: null;
				String addressRegion = catalogueDcatAp.get("address").get("addressRegion") != null
						? catalogueDcatAp.get("address").get("addressRegion").textValue()
						: null;
				String district = catalogueDcatAp.get("address").get("district") != null
						? catalogueDcatAp.get("address").get("district").textValue()
						: null;
				String postOfficeBoxNumber = catalogueDcatAp.get("address").get("postOfficeBoxNumber") != null
						? catalogueDcatAp.get("address").get("postOfficeBoxNumber").textValue()
						: null;
				String postalCode = catalogueDcatAp.get("address").get("postalCode") != null
						? catalogueDcatAp.get("address").get("postalCode").textValue()
						: null;
				String streetAddress = catalogueDcatAp.get("address").get("streetAddress") != null
						? catalogueDcatAp.get("address").get("streetAddress").textValue()
						: null;
				String streetNr = catalogueDcatAp.get("address").get("streetNr") != null
						? catalogueDcatAp.get("address").get("streetNr").textValue()
						: null;
				address = new Address(addressCountry, addressLocality, addressRegion, district, postOfficeBoxNumber,
						postalCode, streetAddress, streetNr);
				addressVer = new AddressVerified(address, true);
			}
			String alternateName = catalogueDcatAp.get("alternateName") != null
					? catalogueDcatAp.get("alternateName").textValue()
					: null;
			String areaServed = catalogueDcatAp.get("areaServed") != null
					? catalogueDcatAp.get("areaServed").textValue()
					: null;

			String[] catalogue = null;
			if (catalogueDcatAp.get("catalogue") != null && catalogueDcatAp.get("catalogue").isArray()) {
				catalogue = new String[(catalogueDcatAp.get("catalogue").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("catalogue")) {
					catalogue[i] = c.toString();
					i++;
				}

			}
			String creator = catalogueDcatAp.get("creator") != null ? catalogueDcatAp.get("creator").textValue() : null;
			String dataProvider = catalogueDcatAp.get("dataProvider") != null
					? catalogueDcatAp.get("dataProvider").textValue()
					: null;
			String dateCreated = catalogueDcatAp.get("dateCreated") != null
					? catalogueDcatAp.get("dateCreated").textValue()
					: null;
			String dateModified = catalogueDcatAp.get("dateModified") != null
					? catalogueDcatAp.get("dateModified").textValue()
					: null;

			String[] hasPart = null;
			if (catalogueDcatAp.get("hasPart") != null && catalogueDcatAp.get("hasPart").isArray()) {
				hasPart = new String[(catalogueDcatAp.get("hasPart").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("hasPart")) {
					hasPart[i] = c.toString();
					i++;
				}

			}
			String homepage = catalogueDcatAp.get("homepage") != null ? catalogueDcatAp.get("homepage").textValue()
					: null;

			String[] language = null;
			if (catalogueDcatAp.get("language") != null && catalogueDcatAp.get("language").isArray()) {
				language = new String[(catalogueDcatAp.get("language").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("language")) {
					language[i] = c.toString();
					i++;
				}

			}
			String licence = catalogueDcatAp.get("licence") != null ? catalogueDcatAp.get("licence").textValue() : null;

			Point location = null;
			if (catalogueDcatAp.get("location") != null) {
				location = new Point();

				Double coordinate[] = new Double[2];
				int j = 0;
				for (JsonNode coord : catalogueDcatAp.get("location").get("coordinates")) {
					coordinate[j] = coord.asDouble();
					j++;
				}

				location = new Point("Point", coordinate);

			}
			String name = catalogueDcatAp.get("name") != null ? catalogueDcatAp.get("name").textValue() : null;

			String[] owner = null;
			if (catalogueDcatAp.get("owner") != null && catalogueDcatAp.get("owner").isArray()) {
				owner = new String[(catalogueDcatAp.get("owner").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("owner")) {
					owner[i] = c.toString();
					i++;
				}

			}
			String publisher = catalogueDcatAp.get("publisher") != null ? catalogueDcatAp.get("publisher").textValue()
					: null;

			String[] record = null;
			if (catalogueDcatAp.get("record") != null && catalogueDcatAp.get("record").isArray()) {
				record = new String[(catalogueDcatAp.get("record").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("record")) {
					record[i] = c.toString();
					i++;
				}

			}
			String releaseDate = catalogueDcatAp.get("releaseDate") != null
					? catalogueDcatAp.get("releaseDate").textValue()
					: null;
			String rights = catalogueDcatAp.get("rights") != null ? catalogueDcatAp.get("rights").textValue() : null;

			String[] seeAlso = null;
			if (catalogueDcatAp.get("seeAlso") != null && catalogueDcatAp.get("seeAlso").isArray()) {
				seeAlso = new String[(catalogueDcatAp.get("seeAlso").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("seeAlso")) {
					seeAlso[i] = c.toString();
					i++;
				}

			}

			String[] service = null;
			if (catalogueDcatAp.get("service") != null && catalogueDcatAp.get("service").isArray()) {
				service = new String[(catalogueDcatAp.get("service").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("service")) {
					service[i] = c.toString();
					i++;
				}

			}
			String source = catalogueDcatAp.get("source") != null ? catalogueDcatAp.get("source").textValue() : null;

			Point[] spatial_geographic = null;
			if (catalogueDcatAp.get("location") != null && catalogueDcatAp.get("spatial_geographic").isArray()) {
				spatial_geographic = new Point[catalogueDcatAp.get("spatial_geographic").size()];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("spatial_geographic")) {
					Double coordinate[] = new Double[2];
					int j = 0;
					for (JsonNode coord : c.get("coordinates")) {
						coordinate[j] = coord.asDouble();
						j++;
					}

					spatial_geographic[i] = new Point("Point", coordinate);
					i++;
				}
			}

			String[] themes = null;
			if (catalogueDcatAp.get("themes") != null && catalogueDcatAp.get("themes").isArray()) {
				themes = new String[(catalogueDcatAp.get("themes").size())];
				int i = 0;
				for (JsonNode c : catalogueDcatAp.get("themes")) {
					themes[i] = c.toString();
					i++;
				}

			}
			String isPartOf = catalogueDcatAp.get("isPartOf") != null ? catalogueDcatAp.get("isPartOf").textValue()
					: null;

			CatalogueDcatAp catalogueNgsi = new CatalogueDcatAp(id, type, description, modificationDate, title, dataset,
					addressVer, alternateName, areaServed, catalogue, creator, dataProvider, dateCreated, dateModified,
					hasPart, homepage, language, licence, location, name, owner, publisher, record, releaseDate, rights,
					seeAlso, service, source, spatial_geographic, themes, isPartOf);
			ObjectMapper mapper = new ObjectMapper();
			// Converting the Object to JSONString
			entity = mapper.writeValueAsString(catalogueNgsi);
		} catch (Exception ex) {

		}
		return entity;
	}

}