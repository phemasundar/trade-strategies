package com.strategies.trade.api_utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategies.trade.utilities.CustomLogging;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RestApiUtils {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static HttpResponse sendingGetRequest(String urlString, Map<String, String> headers) throws IOException {
        return sendingGetRequest(urlString, headers, null);
    }

    public static HttpResponse sendingGetRequest(String urlString, Map<String, String> headers, Map<String, String> params) throws IOException {
        int timeout = 2;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
        HttpGet request = new HttpGet(urlString);
        headers.put("User-Agent", USER_AGENT);

        headers.forEach((k, v) -> request.addHeader(k, v));

        if (params != null) {
            List<NameValuePair> paramsList = params.entrySet().stream().map(item -> (NameValuePair) new BasicNameValuePair(item.getKey(), item.getValue())).collect(Collectors.toList());
            URI uri = null;
            try {
                uri = new URIBuilder(request.getURI())
                        .addParameters(paramsList)
                        .build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            request.setURI(uri);
        }

        for (int i = 0; true; i++) {

            try {
                HttpResponse response = client.execute(request);
                CustomLogging.writeLog("Request: " + urlString);
                CustomLogging.writeLog("Request Headers: " + headers);

                return response;
            } catch (SocketTimeoutException e) {
                if (i == 2) {
                    throw e;
                }
            }
        }
    }

    public static String getResponse(HttpResponse con) throws IOException {
        InputStream content = con.getEntity().getContent();
        String next = "";
        try (Scanner s = new Scanner(content)) {
            Scanner scanner = s.useDelimiter("\\Z");
            if (scanner.hasNext()) {
                next = scanner.next();
            }
        }
        String responseJsonString = null;
        try {
            if (new ObjectMapper().readTree(next).isArray()) {
                responseJsonString = new JSONArray(next).toString(4);
            } else {
                responseJsonString = new JSONObject(next).toString(4);
            }
        } catch (JSONException | JsonParseException e) {
            responseJsonString = "";
        }

        CustomLogging.writeLog("Response: \n" + responseJsonString);

        return next;

    }

    public static int getResponseCode(HttpResponse con) {
        return con.getStatusLine().getStatusCode();
    }

    public static <T> T convertJsonToJavaBean(String response, Class<T> T) throws IOException {

        return new ObjectMapper().readValue(response, T);
    }

    public static HttpResponse sendingPostRequest(String url, String postDataFileData, Map<String, String> headers) throws IOException {

        if (postDataFileData != null) {
            String requestJsonString = "";
            try {
                requestJsonString = new JSONObject(postDataFileData).toString(4);
            } catch (JSONException e) {
                requestJsonString = postDataFileData;
            }
            CustomLogging.writeLog("Request: " + url + "\n\nRequest Body: " + requestJsonString);
        }

        int timeout = 2;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
        HttpPost post = new HttpPost(url);

        headers.put("User-Agent", USER_AGENT);

        post.addHeader("Content-Type", "application/json");

        headers.forEach((k, v) -> post.addHeader(k, v));
        CustomLogging.writeLog("Request Headers: " + headers);

        if (postDataFileData != null) {
            post.setEntity(new StringEntity(postDataFileData, ContentType.APPLICATION_JSON));
        }

        HttpResponse response = client.execute(post);

        return response;

    }

    public static HttpResponse sendingPutRequest(String url, String putDataFileData, Map<String, String> headers) throws IOException {

        if (putDataFileData != null) {
            String requestJsonString = "";
            try {
                requestJsonString = new JSONObject(putDataFileData).toString(4);
            } catch (JSONException e) {
                requestJsonString = putDataFileData;
            }
            CustomLogging.writeLog("Request: " + url + "\n\nRequest Body: " + requestJsonString);
        }

        int timeout = 2;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();

        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
        HttpPut put = new HttpPut(url);

        headers.put("User-Agent", USER_AGENT);

        put.addHeader("Content-Type", "application/json");

        headers.forEach((k, v) -> put.addHeader(k, v));
        CustomLogging.writeLog("Request Headers: " + headers);

        if (putDataFileData != null) {
            put.setEntity(new StringEntity(putDataFileData, ContentType.APPLICATION_JSON));
        }

        HttpResponse response = client.execute(put);

        return response;

    }

    public static HttpResponse sendingPostRequest(String url, String postDataFileData) throws IOException {
        return sendingPostRequest(url, postDataFileData, new HashMap<>());
    }

}