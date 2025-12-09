package com.mryoda.diagnostics.api.utils;

import io.restassured.response.Response;
import java.util.concurrent.TimeUnit;

/**
 * Retry Utility for retrying failed API calls
 */
public class RetryUtil {
    
    private RetryUtil() {
        // Private constructor
    }
    
    /**
     * Retry API call on failure
     */
    public static Response retryOnFailure(APICallable apiCall, int maxRetries, int delayInMs) {
        Response response = null;
        int attempts = 0;
        
        while (attempts < maxRetries) {
            try {
                response = apiCall.call();
                
                // If response is successful, return
                if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                    LoggerUtil.info("API call successful on attempt: " + (attempts + 1));
                    return response;
                }
                
                LoggerUtil.warn("API call failed with status: " + response.getStatusCode() + 
                              ", Attempt: " + (attempts + 1) + "/" + maxRetries);
                
            } catch (Exception e) {
                LoggerUtil.error("API call failed with exception on attempt: " + (attempts + 1), e);
            }
            
            attempts++;
            
            // Wait before retrying
            if (attempts < maxRetries) {
                try {
                    LoggerUtil.info("Waiting " + delayInMs + "ms before retry...");
                    TimeUnit.MILLISECONDS.sleep(delayInMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LoggerUtil.error("Retry interrupted", e);
                }
            }
        }
        
        LoggerUtil.error("API call failed after " + maxRetries + " attempts");
        return response;
    }
    
    /**
     * Functional interface for API calls
     */
    @FunctionalInterface
    public interface APICallable {
        Response call();
    }
}
