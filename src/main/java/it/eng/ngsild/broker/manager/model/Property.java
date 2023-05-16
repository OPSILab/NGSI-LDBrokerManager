package it.eng.ngsild.broker.manager.model;

public class Property {
	String type = "Property";
	 String value;
	 
	 
public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}
	public Property( ) {
		
	}
	public Property( String value) {
		super();
		this.type = "Property";
		this.value = value;
	}
	

}
