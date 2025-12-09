package com.mryoda.diagnostics.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date Utility for date operations
 */
public class DateUtil {
    
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    private DateUtil() {
        // Private constructor
    }
    
    /**
     * Get current date in default format
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
    }
    
    /**
     * Get current date time in default format
     */
    public static String getCurrentDateTime() {
        return new SimpleDateFormat(DEFAULT_DATETIME_FORMAT).format(new Date());
    }
    
    /**
     * Get current date in custom format
     */
    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
    
    /**
     * Get ISO format date time
     */
    public static String getISODateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    
    /**
     * Get date after specified days
     */
    public static String getDateAfterDays(int days) {
        return LocalDateTime.now().plusDays(days)
                .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }
    
    /**
     * Get date before specified days
     */
    public static String getDateBeforeDays(int days) {
        return LocalDateTime.now().minusDays(days)
                .format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }
    
    /**
     * Get timestamp
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * Format timestamp to date string
     */
    public static String formatTimestamp(long timestamp, String format) {
        return new SimpleDateFormat(format).format(new Date(timestamp));
    }
}
