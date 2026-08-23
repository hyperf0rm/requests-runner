package io.github.hyperf0rm.runner.model;

public enum HttpMethod {
    GET(false),
    POST(true),
    PUT(true),
    PATCH(true),
    DELETE(false);

    private final boolean requiresBody;

    HttpMethod(boolean requiresBody) {
        this.requiresBody = requiresBody;
    }

    public boolean requiresBody() {
        return requiresBody;
    }

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
