package it.eng.ngsild.broker.manager.model;

import java.util.Arrays;

public class Point {
	String type = "Point";
	Double[] coordinates;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}
	public Point(String type, Double[] coordinates) {
		super();
		this.type = type;
		this.coordinates = coordinates;
	}
	public Point() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Point [type=" + type + ", coordinates=" + Arrays.toString(coordinates) + "]";
	}
	
	
}
