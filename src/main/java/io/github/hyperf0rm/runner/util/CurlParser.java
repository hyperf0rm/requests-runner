package io.github.hyperf0rm.runner.util;

import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurlParser {

    private String curl;

    public CurlParser(String curl) {
        this.curl = curl;
    }

    public Request parse() {

        // method
        Pattern p = Pattern.compile("--request\\s+([A-Za-z]+)|-X\\s+([A-Za-z]+)");
        Matcher methodMatcher = p.matcher(this.curl);
        String method;
        if (methodMatcher.find()) {
            method = methodMatcher.group(1);
        } else {
            if (this.curl.contains("-data") || this.curl.contains("-d ")) {
                method = "POST";
            } else {
                method = "GET";
            }
        }
        HttpMethod httpMethod = HttpMethod.fromString(method);

        // url
        p = Pattern.compile("--location\\s+'(.+)'|--url\\+'(.+)'");
        Matcher urlMatcher = p.matcher(this.curl);
        String URL = "";
        if (urlMatcher.find()) {
            URL = urlMatcher.group(1);
        }

        System.out.println("URL: " + URL);

        // headers
        p = Pattern.compile("--header\\s+'(.+)'|-H\\s+'(.+)'");
        Matcher headersMatcher = p.matcher(this.curl);
        Map<String, String> headers = new HashMap<>();
        while (headersMatcher.find()) {
            String header = headersMatcher.group(1);
            String[] parts = header.split(":\\s*", 2);
            headers.put(parts[0], parts[1]);
        }

        // body
        p = Pattern.compile("--data\\s+'(.+)'|-d\\s+'(.+)'", Pattern.DOTALL);
        Matcher bodyMatcher = p.matcher(this.curl);
        String body = "";

        if (bodyMatcher.find()) {
            body = bodyMatcher.group(1);
        }

        return new Request(httpMethod, URL, headers, body);
    }

    public String getCurl() {
        return curl;
    }
    public void setCurl(String curl) {
        this.curl = curl;
    }
}

