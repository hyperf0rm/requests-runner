package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{\\w+}}");

    private TemplateEngine() {}

    public static boolean hasPlaceholders(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        return PLACEHOLDER_PATTERN.matcher(text).find();
    }

    public static String interpolate(String template, String value) {
        if (template == null || template.isBlank()) {
            return template;
        }
        if (value == null) {
            value = "";
        }

        return PLACEHOLDER_PATTERN.matcher(template).replaceAll(Matcher.quoteReplacement(value));
    }

    public static List<Request> fillWithValues (Request request, List<String> values) {

        boolean hasBodyPlaceholders = hasPlaceholders(request.body());
        boolean hasUrlPlaceholders = hasPlaceholders(request.url());

        boolean hasAnyPlaceholders = hasUrlPlaceholders || hasBodyPlaceholders;

        if (values == null || values.isEmpty() || !hasAnyPlaceholders) {
            return List.of(request);
        }

        List<Request> requests = new ArrayList<>();

        for (String value : values) {
            String filledUrl = interpolate(request.url(), value);
            String filledBody = interpolate(request.body(), value);
            Request newRequest = new Request(
                    request.method(), filledUrl, request.headers(), filledBody
            );
            requests.add(newRequest);
        }
        return requests;
    }

}
