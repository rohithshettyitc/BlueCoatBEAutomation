package com.bluecoatcloud.threatpulse.utils;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class JsonToXMLMapper {	
	public static String convertJsonToXML(String jsonString) {
		 XMLSerializer serializer = new XMLSerializer(); 
		 JSON json = JSONSerializer.toJSON(jsonString);
		 serializer.setRootName("Response");
		 String xml = serializer.write(json);
		 return xml;
	}
	
	public static String convertJsonToXML(String jsonString, String rootNodeName) {
		 XMLSerializer serializer = new XMLSerializer(); 
		 JSON json = JSONSerializer.toJSON(jsonString);
		 serializer.setRootName(rootNodeName);
		 String xml = serializer.write(json);
		 return xml;
	}
}
