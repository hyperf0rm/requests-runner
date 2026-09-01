package io.github.hyperf0rm.runner.model;

import java.util.List;

public record Request(
        HttpMethod method,
        String url,
        List<Header> headers,
        String body
) {}
