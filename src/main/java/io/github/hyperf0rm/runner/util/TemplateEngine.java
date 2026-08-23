package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;

public class TemplateEngine {

    private final Request request;
    private final List<String> values;

    public TemplateEngine(Request request, List<String> values) {
        this.request = request;
        this.values = values;
    }

    public List<Request> fillWithValues () {
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
