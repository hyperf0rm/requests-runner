package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;

public class TemplateEngine {

    public TemplateEngine() {}

    public List<Request> fillWithValues (Request request, List<String> values) {
        List<Request> requests = new ArrayList<>();
        String templateBody = request.getBody();

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
