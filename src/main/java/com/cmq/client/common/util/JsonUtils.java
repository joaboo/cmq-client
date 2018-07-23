package com.cmq.client.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cmq.client.exception.SerializationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

public class JsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	public static String toJson(Object bean) {
		if (bean == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(bean);
		} catch (Exception e) {
			throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
		}
	}

	public static <T> T toBean(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
		}
	}

	public static <T> List<T> toBeanList(String json, Class<T> clazz) {
		if (json == null) {
			return Collections.emptyList();
		}
		try {
			CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
			return objectMapper.readValue(json, collectionType);
		} catch (Exception e) {
			throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
		}
	}

	public static Map<?, ?> toMap(String json) {
		if (json == null) {
			return Collections.emptyMap();
		}
		try {
			JsonNode node = objectMapper.readTree(json);
			return objectMapper.convertValue(node, Map.class);
		} catch (Exception e) {
			throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
		}
	}

	public static List<Map<?, ?>> toMapList(String json) {
		if (json == null) {
			return Collections.emptyList();
		}
		try {
			List<Map<?, ?>> ret = new ArrayList<>();
			JsonNode listNode = objectMapper.readTree(json);
			for (JsonNode node : listNode) {
				ret.add(objectMapper.convertValue(node, Map.class));
			}
			return ret;
		} catch (Exception e) {
			throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
		}
	}

}
