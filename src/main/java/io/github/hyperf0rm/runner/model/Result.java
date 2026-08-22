package io.github.hyperf0rm.runner.model;

import java.util.Map;

public class Result {
    private int id;
    private String targetValue;
    private float duration;
    private String URL;
    private String payload;
    private Map<String, String> headers;
    private int statusCode;
    private String response;
    private String error;

    public Result(int id,
                  String targetValue,
                  float duration,
                  String URL,
                  Map<String, String> headers,
                  int statusCode,
                  String response,
                  String error) {
        this.id = id;
        this.targetValue = targetValue;
        this.duration = duration;
        this.URL = URL;
        this.headers = headers;
        this.statusCode = statusCode;
        this.response = response;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
