package com.ai.analy.cache.util;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONValidator {

	private JSONValidator() {

	}

	public static boolean isChanged(JsonNode jsonObj, String key, String value) {
		if (jsonObj.has(key) && jsonObj.get(key) != null
				&& !jsonObj.get(key).asText().equals(value)) {
			return true;
		}
		return false;
	}

	public static boolean isChanged(JsonNode jsonObj, String key, int value) {
		if (jsonObj.has(key) && jsonObj.get(key) != null
				&& !jsonObj.get(key).asText().equals(value+"")) {
			return true;
		}
		return false;
	}
}
