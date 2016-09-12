package com.bluecoatcloud.threatpulse.utils;

import org.json.JSONException;

public class JsonParser {

	/*
	 * Param
	 * jsonStringBody jsonString (should be a valid json)
	 * jsonPath Path to the key -  format - root/node1[2]/node2[1]
	 */
	public String parseJsonData(String jsonStringBody, String jsonPath) throws JSONException {

		org.json.JSONObject json = new org.json.JSONObject(jsonStringBody);

		String parsedData = "";
		String jsonTempPath = "";
		String nodeValue = "";

		//Tokenize the json path
		String nodes[] = jsonPath.split("/");
		int arrayValue = 1;
		System.out.println(nodes.length);
		if (nodes.length == 1) {
			return json.getString(nodes[0]);
		}
		else {
			//int noOfArrays = json.getJSONArray(nodes[0]).length();
			if (nodes[1].indexOf("[") > 0) {
				arrayValue = Integer.parseInt(nodes[1].substring(nodes[1].indexOf("[") + 1, nodes[1].indexOf("]")));
			}

			//modifying json path for next iteration.
			if (nodes[1].indexOf("[") > 0)
				nodeValue = nodes[1].substring(0, nodes[1].indexOf("["));
			else
				nodeValue = nodes[1];

			jsonTempPath = nodeValue;
			for (int i=2; i < nodes.length; i++) {	
				jsonTempPath = jsonTempPath + "/" + nodes[i];
			}

			parsedData = parseJsonData(json.getJSONArray(nodes[0]).getString(arrayValue-1), jsonTempPath);
		}
		return parsedData;
	}
}
