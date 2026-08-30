package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    public CurlParser() {}

    public Request parse(String curl) {

        // method
        Pattern p = Pattern.compile("(?:--request|-X)\\s+([A-Za-z]+)");
        Matcher methodMatcher = p.matcher(curl);
        String method;
        if (methodMatcher.find()) {
            method = methodMatcher.group(1);
        } else {
            if (curl.contains("-data") || curl.contains("-d ")) {
                method = "POST";
            } else {
                method = "GET";
            }
        }
        HttpMethod httpMethod = HttpMethod.fromString(method);

        // url
        p = Pattern.compile("'(https?://[^'\"\\s]+)'");
        Matcher urlMatcher = p.matcher(curl);
        String URL = "";
        if (urlMatcher.find()) {
            URL = urlMatcher.group(1);
        }

        System.out.println("URL: " + URL);

        // headers
        p = Pattern.compile("(?:--header|-H)\\s+'(.+)'");
        Matcher headersMatcher = p.matcher(curl);
        Map<String, String> headers = new HashMap<>();
        while (headersMatcher.find()) {
            String header = headersMatcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            headers.put(parts[0], parts[1]);
        }

        // body
        p = Pattern.compile("(?:--data|-d)\\s+'(.+)'", Pattern.DOTALL);
        Matcher bodyMatcher = p.matcher(curl);
        String body = "";

        if (bodyMatcher.find()) {
            body = bodyMatcher.group(1);
        }

        return new Request(httpMethod, URL, headers, body);
    }

}

