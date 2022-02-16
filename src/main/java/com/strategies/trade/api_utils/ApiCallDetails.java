package com.strategies.trade.api_utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author hemasundarpenugonda
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiCallDetails {
    private String requestURL;
    private Map<String, String> requestHeaders;
    private String requestBody;
    private String response;
    private int apiCallCount;

    public ApiCallDetails(String requestURL, Map<String, String> requestHeaders, String requestBody, String response) {
        this.requestURL = requestURL;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
        this.response = response;
    }
}
