package com.mryoda.diagnostics.api.utils;

public class RequestContext {

    private static final ThreadLocal<String> token = new ThreadLocal<>();
    private static final ThreadLocal<String> mobile = new ThreadLocal<>();
    private static final ThreadLocal<String> userGuid = new ThreadLocal<>();
    private static final ThreadLocal<String> userId = new ThreadLocal<>();

    // Token handler
    public static void setToken(String value) {
        token.set(value);
    }

    public static String getToken() {
        return token.get();
    }

    // Mobile handler
    public static void setMobile(String value) {
        mobile.set(value);
    }

    public static String getMobile() {
        return mobile.get();
    }

    // GUID handler
    public static void setUserGuid(String value) {
        userGuid.set(value);
    }

    public static String getUserGuid() {
        return userGuid.get();
    }

    // UserId handler (numeric id if needed)
    public static void setUserId(String value) {
        userId.set(value);
    }

    public static String getUserId() {
        return userId.get();
    }

    // Clear all after suite/test
    public static void clear() {
        token.remove();
        mobile.remove();
        userGuid.remove();
        userId.remove();
    }
}
