package io.github.hyperf0rm.runner.model;

import java.util.List;

public class Result {
    private int id;
    private long duration;
    private Request request;
    private List<HttpTableEntry> responseHeaders;
    private int statusCode;
    private String response;
    private String error;

    public Result() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getUrl() {
        return request.url();
    }

    public String getPayload() {
        return request.body();
    }

    public List<HttpTableEntry> getHeaders() {
        return request.headers();
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

    public List<HttpTableEntry> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(List<HttpTableEntry> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
