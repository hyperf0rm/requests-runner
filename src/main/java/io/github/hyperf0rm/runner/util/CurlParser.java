package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.HttpTableEntry;
import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.tool.JsonFormatter;
import io.github.hyperf0rm.runner.tool.UrlCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private static final Pattern METHOD_PATTERN = Pattern.compile(
            "(?:--request|-X)\\s+([A-Za-z]+)"
    );;
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(?:--url\\s+)?(?:\\^?['\"])?(https?://[^\\s\"'^]+)"
    );
    private static final Pattern HEADER_PATTERN = Pattern.compile(
            "(?:--header|-H)\\s+\\^?['\"](.+?)\\^?['\"](?:\\s|$)"
    );
    private static final Pattern BODY_PATTERN = Pattern.compile(
            "(?:--data|-d|--data-raw|--data-binary|--data-urlencode)\\s+" +
                    "\\^?(['\"])((?s).*?)\\^?\\1(?:\\s+(?:--|-X|$)|\n|\r|$)",
            Pattern.DOTALL
    );
    private static final Pattern FORM_DATA_BODY_PATTERN = Pattern.compile(
            "(?:--data|-d|--data-raw|--data-binary|--data-urlencode)\\s+\\^?(['\"])((?s).*?)\\^?\\1"
    );
    private static final UrlCodec urlCodec = new UrlCodec();

    private CurlParser() {}

    public static Request parse(String curl) {
        HttpMethod httpMethod = getHttpMethod(curl);
        String url = getUrl(curl);
        List<HttpTableEntry> headers = getHeaders(curl);
        String body = getBody(curl, headers);
        return new Request(httpMethod, url, headers, body);
    }

    private static HttpMethod getHttpMethod(String curl) {
        Matcher methodMatcher = METHOD_PATTERN.matcher(curl);
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
        return HttpMethod.fromString(method);
    }

    private static String getUrl(String curl) {
        Matcher urlMatcher = URL_PATTERN.matcher(curl);
        String url = "";
        if (urlMatcher.find()) {
            url = urlMatcher.group(1);
        }
        return url;
    }

    private static List<HttpTableEntry> getHeaders(String curl) {
        Matcher headersMatcher = HEADER_PATTERN.matcher(curl);
        List<HttpTableEntry> headers = new ArrayList<>();
        while (headersMatcher.find()) {
            String header = headersMatcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            headers.add(new HttpTableEntry(parts[0], parts[1]));
        }
        return headers;
    }

    private static String getBody(String curl, List<HttpTableEntry> headers) {
        boolean hasFormHeader = headers.stream()
                .anyMatch(h -> h.getValue().toLowerCase().contains("application/x-www-form-urlencoded"));
        boolean isUrlEncoded = hasFormHeader || curl.contains("--data-urlencode");

        if (isUrlEncoded) {
            Matcher matcher = FORM_DATA_BODY_PATTERN.matcher(curl);

            List<String> finalData = new ArrayList<>();
            while (matcher.find()) {
                String flag = matcher.group(0).trim();

                if (flag.startsWith("--data-urlencode")) {
                    String[] parts = matcher.group(2).split("=", 2);
                    String key = urlCodec.encode(parts[0]);
                    String value = parts.length > 1 ? urlCodec.encode(parts[1]) : "";
                    finalData.add(key + "=" + value);
                } else {
                    finalData.add(matcher.group(2));
                }
            }
            return String.join("&", finalData);
        }

        Matcher bodyMatcher = BODY_PATTERN.matcher(curl);
        String body = "";

        if (bodyMatcher.find()) {
            body = cleanBody(bodyMatcher.group(2));
        }
        return JsonFormatter.formatJson(body);
    }

    private static String cleanBody(String body) {
        body = body.replace("'\\''", "'").replace("\\'", "'");

        body = body.replace("^\\^\"", "\"")
                .replace("\\^\"", "\"")
                .replace("^\"", "\"")
                .replace("\\\"", "\"");

        body = body.replace("^{", "{")
                .replace("^}", "}")
                .replace("^[", "[")
                .replace("^]", "]");

        body = body.replace("^", "");

        return body;
    }
}

