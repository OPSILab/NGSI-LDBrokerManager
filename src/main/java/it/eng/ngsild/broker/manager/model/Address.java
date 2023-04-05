package it.eng.ngsild.broker.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	private String addressCountry;
    private String addressLocality;
    private String addressRegion;
    private String district;
    private String postOfficeBoxNumber;
    private String postalCode;
    private String streetAddress;
    private String streetNr;
}
