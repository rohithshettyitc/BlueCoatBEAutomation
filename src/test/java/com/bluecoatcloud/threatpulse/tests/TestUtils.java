package com.bluecoatcloud.threatpulse.tests;



import org.json.JSONException;
import org.json.JSONObject;

public class TestUtils {
	
	/**
	 * @param property
	 * @return
	 */
	public static boolean ifNotNull(String property) {
		if (property == null)
			return false;
		return true;
	}
	
	public static boolean ifNotNull(Object ResponseBody, Object ResponseCode, Object Url, Object ResponseTime, Object requestMethod) {
		if(ResponseBody == null || ResponseCode == null || Url == null || ResponseTime == null || requestMethod == null)
			return false;
		else
			return true;
	}
	
	/*public synchronized static String searchValueByName(String name, List<edu.apollogrp.lp.activity.domain.dto.NameValue> list) {
		String value = "";
		for(edu.apollogrp.lp.activity.domain.dto.NameValue pair : list) {
			if(pair.getName().equals(name))
				value = pair.getValue();
		}
		return value;
	}*/
	
	public static String getJsonValue(String json,String key) throws JSONException{		
		return (String) new JSONObject(json).get(key);		
	}

}
