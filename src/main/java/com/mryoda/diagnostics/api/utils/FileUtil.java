package com.mryoda.diagnostics.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * File Utility for file operations
 */
public class FileUtil {
    
    private FileUtil() {
        // Private constructor
    }
    
    /**
     * Read file content as string
     */
    public static String readFileAsString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LoggerUtil.error("Error reading file: " + filePath, e);
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
    
    /**
     * Write string to file
     */
    public static void writeStringToFile(String content, String filePath) {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
            LoggerUtil.info("Content written to file: " + filePath);
        } catch (IOException e) {
            LoggerUtil.error("Error writing to file: " + filePath, e);
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
    
    /**
     * Create directory if not exists
     */
    public static void createDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                LoggerUtil.info("Directory created: " + dirPath);
            }
        }
    }
    
    /**
     * Delete file
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                LoggerUtil.info("File deleted: " + filePath);
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * Load properties from file
     */
    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            LoggerUtil.info("Properties loaded from: " + filePath);
        } catch (IOException e) {
            LoggerUtil.error("Error loading properties from: " + filePath, e);
            throw new RuntimeException("Failed to load properties", e);
        }
        return properties;
    }
    
    /**
     * Save properties to file
     */
    public static void saveProperties(Properties properties, String filePath, String comments) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            properties.store(fos, comments);
            LoggerUtil.info("Properties saved to: " + filePath);
        } catch (IOException e) {
            LoggerUtil.error("Error saving properties to: " + filePath, e);
            throw new RuntimeException("Failed to save properties", e);
        }
    }
}
