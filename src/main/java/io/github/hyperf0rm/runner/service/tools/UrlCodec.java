package io.github.hyperf0rm.runner.service.tools;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlCodec implements Codec {

    @Override
    public String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return "Error: Invalid URL-encoded input (" + e.getMessage() + ")";
        }
    }

    @Override
    public String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}