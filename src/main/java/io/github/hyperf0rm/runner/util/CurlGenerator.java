package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.HttpTableEntry;
import io.github.hyperf0rm.runner.model.Request;

import java.util.ArrayList;
import java.util.List;

public class CurlGenerator {

    public static String generateCurl(Request request) {
        List<String> curlParts = new ArrayList<>();

        String basePart = "curl --location --request " + request.method().toString() + " '" + request.url() + "'";
        curlParts.add(basePart);

        if (!request.headers().isEmpty()) {
            for (HttpTableEntry entry : request.headers()) {
                curlParts.add("--header '" + entry.getKey() + ": " + entry.getValue() + "'");
            }
        }

        if (!request.body().isBlank()) {
            String bodyPart = "--data '" + request.body() + "'";
            curlParts.add(bodyPart);
        }

        return String.join(" \\\n", curlParts);
    }
}
