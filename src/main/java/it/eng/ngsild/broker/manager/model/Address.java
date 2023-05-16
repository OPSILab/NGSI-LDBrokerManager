package it.eng.ngsild.broker.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String addressCountry;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String addressLocality;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String addressRegion;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String district;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String postOfficeBoxNumber;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String postalCode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String streetAddress;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    public String streetNr;

	
	
	
	



	public Address(String addressCountry, String addressLocality, String addressRegion, String district,
			String postOfficeBoxNumber, String postalCode, String streetAddress, String streetNr) {
		super();
		this.addressCountry = addressCountry;
		this.addressLocality = addressLocality;
		this.addressRegion = addressRegion;
		this.district = district;
		this.postOfficeBoxNumber = postOfficeBoxNumber;
		this.postalCode = postalCode;
		this.streetAddress = streetAddress;
		this.streetNr = streetNr;
	}



	public String getAddressCountry() {
		return addressCountry;
	}



	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}



	public String getAddressLocality() {
		return addressLocality;
	}



	public void setAddressLocality(String addressLocality) {
		this.addressLocality = addressLocality;
	}



	public String getAddressRegion() {
		return addressRegion;
	}



	public void setAddressRegion(String addressRegion) {
		this.addressRegion = addressRegion;
	}



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}



	public String getPostOfficeBoxNumber() {
		return postOfficeBoxNumber;
	}



	public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
		this.postOfficeBoxNumber = postOfficeBoxNumber;
	}



	public String getPostalCode() {
		return postalCode;
	}



	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}



	public String getStreetAddress() {
		return streetAddress;
	}



	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}



	public String getStreetNr() {
		return streetNr;
	}



	public void setStreetNr(String streetNr) {
		this.streetNr = streetNr;
	}



	@Override
	public String toString() {
		return " [addressCountry=" + addressCountry + ", addressLocality=" + addressLocality + ", addressRegion="
				+ addressRegion + ", district=" + district + ", postOfficeBoxNumber=" + postOfficeBoxNumber
				+ ", postalCode=" + postalCode + ", streetAddress=" + streetAddress + ", streetNr=" + streetNr + "]";
	}
	
}
