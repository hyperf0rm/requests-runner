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

        return template.replaceAll(PLACEHOLDER_PATTERN.pattern(), Matcher.quoteReplacement(value));
    }

    public static List<Request> fillWithValues (Request request, List<String> values) {
        boolean hasPlaceholders = hasPlaceholders(request.body());

        if (values == null || values.isEmpty() || !hasPlaceholders) {
            return List.of(request);
        }

        List<Request> requests = new ArrayList<>();

        for (String value : values) {
            String filledBody = interpolate(request.body(), value);
            Request newRequest = new Request(
                    request.method(), request.url(), request.headers(), filledBody
            );
            requests.add(newRequest);
        }
        return requests;
    }

}
