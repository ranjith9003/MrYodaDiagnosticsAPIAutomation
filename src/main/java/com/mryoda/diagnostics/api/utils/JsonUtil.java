package com.mryoda.diagnostics.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * JSON Utility for JSON operations
 */
public class JsonUtil {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    private JsonUtil() {
        // Private constructor
    }
    
    /**
     * Convert object to JSON string using Jackson
     */
    public static String objectToJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            LoggerUtil.info("Object converted to JSON: " + json);
            return json;
        } catch (Exception e) {
            LoggerUtil.error("Error converting object to JSON", e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object using Jackson
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            T object = objectMapper.readValue(json, clazz);
            LoggerUtil.info("JSON converted to object: " + clazz.getName());
            return object;
        } catch (Exception e) {
            LoggerUtil.error("Error converting JSON to object", e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
    
    /**
     * Convert object to JSON using Gson
     */
    public static String objectToJsonGson(Object object) {
        try {
            String json = gson.toJson(object);
            LoggerUtil.info("Object converted to JSON (Gson): " + json);
            return json;
        } catch (Exception e) {
            LoggerUtil.error("Error converting object to JSON (Gson)", e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON to object using Gson
     */
    public static <T> T jsonToObjectGson(String json, Class<T> clazz) {
        try {
            T object = gson.fromJson(json, clazz);
            LoggerUtil.info("JSON converted to object (Gson): " + clazz.getName());
            return object;
        } catch (Exception e) {
            LoggerUtil.error("Error converting JSON to object (Gson)", e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
    
    /**
     * Pretty print JSON string
     */
    public static String prettyPrintJson(String json) {
        try {
            JsonElement jsonElement = JsonParser.parseString(json);
            return gson.toJson(jsonElement);
        } catch (Exception e) {
            LoggerUtil.error("Error pretty printing JSON", e);
            return json;
        }
    }
    
    /**
     * Validate JSON string
     */
    public static boolean isValidJson(String json) {
        try {
            JsonParser.parseString(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
