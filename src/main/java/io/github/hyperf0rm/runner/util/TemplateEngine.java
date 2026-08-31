package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private TemplateEngine() {}

    public static List<Request> fillWithValues (Request request, List<String> values) {
        List<Request> requests = new ArrayList<>();
        String templateBody = request.getBody();

        Pattern pattern = Pattern.compile("\\{\\{\\w+}}");
        Matcher matcher = pattern.matcher(templateBody);

        if (!matcher.find()) {
            requests.add(request);
            return requests;
        }

        for (String value : values) {
            String filledBody = templateBody.replaceAll("\\{\\{\\w+}}", value);
            Request newRequest = new Request(
                    request.getMethod(), request.getUrl(), request.getHeaders(), filledBody
            );
            requests.add(newRequest);
        }
        return requests;
    }
}
