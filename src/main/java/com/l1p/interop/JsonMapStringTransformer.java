package com.l1p.interop;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to convert a Map into a JSON String. The instance of
 * ObjectMapper used to perform conversion is stored as a ThreadLocal.
 *
 * TODO: - look at using JSON native map of some sort
 *
 * Created by Bryan on 8/17/2016.
 */
public class JsonMapStringTransformer {

	private static ThreadLocal<ObjectMapper> threadLocalObjectMapper = new ThreadLocal<ObjectMapper>() {
		@Override
		protected ObjectMapper initialValue() {
			return new ObjectMapper();
		}
	};

	/**
	 * Converts a Map into its JSON representation. If null input is provided an
	 * empty map representation will be returned.
	 *
	 * @param source
	 *            - Map to convert
	 * @return String representation of the provided map in JSON format
	 * @throws RuntimeException
	 *             if the conversion fails
	 */
	public static String mapToString(Map<String, Object> source) {

		if (source == null)
			return "{}";

		try {
			return threadLocalObjectMapper.get().writeValueAsString(source);
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert source Map to JSON: " + e.getMessage());
		}
	}

	/**
	 * Converts a JSON String representing a Map to an instance of Map
	 * <String,Object>. If null input is provided an empty map will be returned.
	 *
	 * @param jsonString
	 *            - string to convert
	 * @return Map
	 * @throws RuntimeException if the string cannot be converted to a map
	 */
	public static Map<String, Object> stringToMap(final String jsonString) {

		try {
			return jsonString != null ? (Map<String, Object>) threadLocalObjectMapper.get()
					.readValue(jsonString.toString(), new TypeReference<Map<String, Object>>() {
					}) : new HashMap<String, Object>();
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert source String to Map<String,Object>: " + e.getMessage());
		}
	}
}
