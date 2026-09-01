package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private CurlParser() {}

    public static Request parse(String curl) {

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

        // headers
        p = Pattern.compile("(?:--header|-H)\\s+'(.+)'");
        Matcher headersMatcher = p.matcher(curl);
       List<Header> headers = new ArrayList<>();
        while (headersMatcher.find()) {
            String header = headersMatcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            headers.add(new Header(parts[0], parts[1]));
        }

        // body
        p = Pattern.compile("(?:--data|-d|--data-raw|--data-binary|--data-urlencode)\\s+'(.+)'", Pattern.DOTALL);
        Matcher bodyMatcher = p.matcher(curl);
        String body = "";

        if (bodyMatcher.find()) {
            body = bodyMatcher.group(1);
        }

        body = JsonFormatter.formatJson(body);

        return new Request(httpMethod, URL, headers, body);
    }

}

