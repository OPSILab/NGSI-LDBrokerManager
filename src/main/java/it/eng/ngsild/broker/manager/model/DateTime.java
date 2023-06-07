package it.eng.ngsild.broker.manager.model;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.eng.idra.utils.CommonUtil;

public class DateTime {
 String type = "Property";
 ObjectNode value;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

public ObjectNode getValue() {
	return value;
}
public void setValue(ObjectNode value) {
	this.value = value;
}
public DateTime(String date) {
	super();
	this.type = "Property";
	ObjectMapper mapper = new ObjectMapper();


	ObjectNode childNode1 = mapper.createObjectNode();
	childNode1.put("@type", "DateTime");
	childNode1.put("@value", date);
	
	/*JSONObject dateObj = new JSONObject();
	dateObj.put("@type", "DateTime");
	dateObj.put("@value", date);
	System.out.println(dateObj.toString());*/
	this.value = childNode1;
	
}
@Override
public String toString() {
	return "DateTime [type=" + type + ", value=" + value.toString() + "]";
}
 
 
}
