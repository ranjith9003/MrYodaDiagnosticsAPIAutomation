package com.mryoda.diagnostics.api.repository;

import java.util.HashMap;
import java.util.Map;

public class LocationDataStore {

    private static final Map<String, String> locationIdMap = new HashMap<>();

    public static void saveLocation(String title, String locationId) {
        locationIdMap.put(title, locationId);
    }

    public static String getLocationId(String title) {
        return locationIdMap.get(title);
    }

    public static Map<String, String> getAllLocations() {
        return locationIdMap;
    }
}
