package com.bluecoatcloud.threatpulse.utils;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JSONUtils.class);

	private static ObjectMapper objectMapper = null;
	
	static {
		objectMapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		objectMapper.getDeserializationConfig().appendAnnotationIntrospector(introspector);
		objectMapper.getSerializationConfig().appendAnnotationIntrospector(introspector);
		objectMapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public static String marshal(Object bean)  {
		Writer writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, bean);
		} catch (Exception e) {
			LOG.error("Could not parse json .", e);
			throw new RuntimeException(e);
		}
		return writer.toString();
	}

	public static <T> T unmarshal(String json, Class<T> klass) {
		try {
			return objectMapper.readValue(json, klass);
		}  catch (Exception e) {
			LOG.error("Could not parse json .", e);
			throw new RuntimeException(e);
		}
	}
	
	public static <T> List<T> unmarshal(String json, TypeReference<List<T>> typeRef) {
		try {
			return objectMapper.readValue(json, typeRef);
		}  catch (Exception e) {
			LOG.error("Could not parse json .", e);
			throw new RuntimeException(e);
		}

	}

	public static <T> T unmarshal(byte[] json, Class<T> klass) {
		try {
			return objectMapper.readValue(json, klass);
		}  catch (Exception e) {
			LOG.error("Could not parse json .", e);
			throw new RuntimeException(e);
		}
	}
}