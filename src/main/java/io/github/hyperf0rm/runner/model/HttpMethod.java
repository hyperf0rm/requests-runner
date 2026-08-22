package io.github.hyperf0rm.runner.model;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HttpMethod fromString(String method) {
        if (method == null) {
            return HttpMethod.GET;
        }
        try {
            return HttpMethod.valueOf(method.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return HttpMethod.GET;
        }
    }
}
