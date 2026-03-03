package it.eng.ngsild.broker.manager.model;

public class GeoProperty {
	String type;
	Object coordinates;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Object coordinates) {
		this.coordinates = coordinates;
	}
	public GeoProperty(String type, Object coordinates) {
		super();
		this.type = type;
		this.coordinates = coordinates;
	}
	
	
}
