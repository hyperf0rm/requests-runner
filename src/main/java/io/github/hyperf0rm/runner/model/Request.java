package io.github.hyperf0rm.runner.model;

import java.util.Map;

public record Request(
        HttpMethod method,
        String url,
        Map<String, String> headers,
        String body
) {}
