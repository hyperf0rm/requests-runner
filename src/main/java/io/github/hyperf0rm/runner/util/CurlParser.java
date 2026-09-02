package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private static final Pattern METHOD_TEMPLATE = Pattern.compile("(?:--request|-X)\\s+([A-Za-z]+)");;
    private static final Pattern URL_TEMPLATE = Pattern.compile("'(https?://[^'\"\\s]+)'");
    private static final Pattern HEADER_TEMPLATE = Pattern.compile("(?:--header|-H)\\s+'(.+)'");
    private static final Pattern BODY_TEMPLATE = Pattern.compile("(?:--data|-d|--data-raw|--data-binary|--data-urlencode)\\s+'(.+)'", Pattern.DOTALL);

    private CurlParser() {}

    public static Request parse(String curl) {

        // method
        Matcher methodMatcher = METHOD_TEMPLATE.matcher(curl);
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
        Matcher urlMatcher = URL_TEMPLATE.matcher(curl);
        String url = "";
        if (urlMatcher.find()) {
            url = urlMatcher.group(1);
        }

        // headers
        Matcher headersMatcher = HEADER_TEMPLATE.matcher(curl);
        List<Header> headers = new ArrayList<>();
        while (headersMatcher.find()) {
            String header = headersMatcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            headers.add(new Header(parts[0], parts[1]));
        }

        // body
        Matcher bodyMatcher = BODY_TEMPLATE.matcher(curl);
        String body = "";

        if (bodyMatcher.find()) {
            body = bodyMatcher.group(1);
        }

        body = JsonFormatter.formatJson(body);

        return new Request(httpMethod, url, headers, body);
    }

}

