package com.mryoda.diagnostics.api.builders;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.restassured.AllureRestAssured;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.utils.LoggerUtil;
import com.mryoda.diagnostics.api.utils.RequestContext;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    private RequestSpecification requestSpec;
    private String baseUri;
    private String endpoint;
    private Map<String, Object> queryParams;
    private Map<String, Object> pathParams;
    private Map<String, String> headers;
    private Object requestBody;

    public RequestBuilder() {
        this.requestSpec = RestAssured.given();
        this.queryParams = new HashMap<>();
        this.pathParams = new HashMap<>();
        this.headers = new HashMap<>();
        this.requestSpec.filter(new AllureRestAssured());
        this.requestSpec.relaxedHTTPSValidation();
    }

    public RequestBuilder setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RequestBuilder setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public RequestBuilder addQueryParam(String key, Object value) {
        this.queryParams.put(key, value);
        return this;
    }

    public RequestBuilder addPathParam(String key, Object value) {
        this.pathParams.put(key, value);
        return this;
    }

    public RequestBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public RequestBuilder setRequestBody(Object body) {
        this.requestBody = body;
        return this;
    }

    public RequestBuilder addAuthToken(String token) {
        this.headers.put("Authorization", "Bearer " + token);
        return this;
    }

    private RequestSpecification build() {

        requestSpec.baseUri(baseUri != null ? baseUri : ConfigLoader.getConfig().baseUrl());

        // Inject Token
        String token = RequestContext.getToken();
        if (token != null && !token.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + token);
        }

        if (!headers.isEmpty()) {
            requestSpec.headers(headers);
        }

        if (!queryParams.isEmpty()) {
            requestSpec.queryParams(queryParams);
        }

        if (!pathParams.isEmpty()) {
            requestSpec.pathParams(pathParams);
        }

        if (requestBody != null) {
            requestSpec.body(requestBody);
        }

        LoggerUtil.info("\n=========== REQUEST ===========");
        LoggerUtil.info("URI: " +
                (baseUri != null ? baseUri : ConfigLoader.getConfig().baseUrl())
                + endpoint);
        LoggerUtil.info("Headers: " + headers);
        LoggerUtil.info("Query: " + queryParams);
        LoggerUtil.info("Path: " + pathParams);
        LoggerUtil.info("Body: " + requestBody);
        LoggerUtil.info("================================");

        return requestSpec;
    }

    public Response get() {
        return build().get(endpoint);
    }

    public Response post() {
        return build().post(endpoint);
    }

    public Response put() {
        return build().put(endpoint);
    }

    public Response patch() {
        return build().patch(endpoint);
    }

    public Response delete() {
        return build().delete(endpoint);
    }

    public Response head() {
        return build().head(endpoint);
    }

    public Response options() {
        return build().options(endpoint);
    }
}
