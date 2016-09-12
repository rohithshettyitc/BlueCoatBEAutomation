package com.bluecoatcloud.threatpulse.clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;


import org.apache.http.NameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleModule;

import com.bluecoatcloud.threatpulse.utils.CustomDeserializer;


public class BlueCoatTestBase {

	public static ObjectMapper mapper = new ObjectMapper();

	static {
		resetObjectMapper();
	}

	public static Object getJsonCollectionFromString(String body,
			TypeReference reference) throws Exception {
		// ObjectMapper mapper = new ObjectMapper();
		// mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION,
		// false);
		return mapper.readValue(body, reference);
	}

	public static void resetObjectMapper() {

		mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION,
				false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES,
				false);
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
				false);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public static String getJsonFromObject(Object o)
			throws JsonMappingException, JsonGenerationException, IOException {
		resetObjectMapper();
		return mapper.writeValueAsString(o);
	}


	/*public static String getJsonFromCollection(Object o, TypeReference view)
    	    throws JsonMappingException, JsonGenerationException, IOException {
    	resetObjectMapper();
    	//ObjectWriter withType(TypeReference<?> rootType)
    	mapper.getTypeFactory().constructType(contentRepositoryTestObjectListTypeReference);
    	//mapper.getSerializationConfig().setSerializationInclusion(view);
    	String retval = mapper.writeValueAsString(o);
    	resetObjectMapper();
    	return retval;
    }*/

	public static String getJsonFromObject(Object o, Class view)
			throws JsonMappingException, JsonGenerationException, IOException {
		resetObjectMapper();
		mapper.getSerializationConfig().setSerializationView(view);
		String retval = mapper.writeValueAsString(o);
		resetObjectMapper();
		return retval;
	}

	public static Object getJsonObjectFromString(String s, Class objectView)
			throws Exception {
		resetObjectMapper();
		return mapper.readValue(s, objectView);
	}

	public static Object getJsonObjectFromString(String s, Class targetClass,
			Class objectView) throws Exception {
		resetObjectMapper();
		mapper.getSerializationConfig().setSerializationView(objectView);
		return mapper.readValue(s, targetClass);

	}

	public static ArrayList<NameValuePair> getHeader(
			NameValuePair... nameValuePairs) {
		ArrayList<NameValuePair> header = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++)
			header.add(nameValuePairs[i]);
		return header;
	}

	public static <T> T getParameterizedObjectFromJsonString(String jsonString, Class<T> targetClass, Map<Class,Class> deserializingClassContextMap) throws Exception{
		List<Module> modules = new ArrayList<Module>();
		if(deserializingClassContextMap != null){
			for(Class clazz : deserializingClassContextMap.keySet()){
				CustomDeserializer deserializer = new CustomDeserializer(deserializingClassContextMap.get(clazz));
				SimpleModule module =  new SimpleModule(clazz.getName(),new Version(1, 0, 0, null));  
				module.addDeserializer(clazz, deserializer);
				modules.add(module);
			}
		}

		return (T) getJsonObjectFromStringUsingDeserializingContext(jsonString, targetClass, modules.toArray(new Module[0]));
	}

	public static <T> T getJsonObjectFromStringUsingDeserializingContext(String jsonString, Class<T> targetClass, Module... modules) throws Exception {
		resetObjectMapper();
		for (Module module : modules) {
			mapper.registerModule(module);
		}
		return (T) mapper.readValue(jsonString, targetClass);
	}

	/*public static <T> Object getParameterizedJsonCollectionFromString(String body,
	    TypeReference<T> reference, Map<Class,Class> deserializingClassContextMap) throws Exception {
    	List<Module> modules = new ArrayList<Module>();
    	if(deserializingClassContextMap != null){
	    	for(Class clazz : deserializingClassContextMap.keySet()){
		    	CustomDeserializer deserializer = new CustomDeserializer(deserializingClassContextMap.get(clazz));
				SimpleModule module =  new SimpleModule(clazz.getName(),new Version(1, 0, 0, null));  
				module.addDeserializer(clazz, deserializer);
				modules.add(module);
	    	}
    	}
    	return getJsonTypeRefFromStringUsingDeserializingContext(body, reference, modules.toArray(new Module[0]));
    }*/

	public static <T> Object getJsonTypeRefFromStringUsingDeserializingContext(String jsonString, TypeReference<T> targetRef, Module... modules) throws Exception {
		resetObjectMapper();
		for (Module module : modules) {
			mapper.registerModule(module);
		}
		return mapper.readValue(jsonString, targetRef);
	}

}
