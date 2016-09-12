package com.bluecoatcloud.threatpulse.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

//import edu.apollogrp.common.Privacy;

public class CustomDeserializer<T> extends StdDeserializer<T> {
	private Class classType;
	public CustomDeserializer(Class classType) {
		super(classType);
		this.classType = classType;
	}

	@Override
	public T deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode root = (JsonNode) mapper.readTree(jp);

		return (T) mapper.readValue(root, classType);
	}
}