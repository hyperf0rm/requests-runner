package io.github.hyperf0rm.runner.service;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunnerService {

    private final List<Request> requests;
    private List<Result> results;
    private final HttpClient client;

    public RunnerService(List<Request> requests) {
        this.requests = requests;
        this.results = new ArrayList<>();
        this.client =  HttpClient.newHttpClient();
    }

    public void run() {
        for (Request request : requests) {
            HttpRequest finalRequest = buildFinalRequest(request);
            client.sendAsync(finalRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        }
    }


    private HttpRequest buildFinalRequest(Request request) {

        try {
            HttpRequest.BodyPublisher bodyPublisher;
            if (request.getMethod().requiresBody() && request.getBody() != null) {
                bodyPublisher = HttpRequest.BodyPublishers.ofString(request.getBody());
            } else {
                bodyPublisher = HttpRequest.BodyPublishers.noBody();
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(request.getUrl()))
                    .method(request.getMethod().name(), bodyPublisher);
            Map<String, String> headers = request.getHeaders();
            headers.forEach(builder::header);

            return builder.build();

        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + e.getMessage());
            throw new IllegalArgumentException("Invalid URL", e);
        }
    }

    public List<Result> getResults() {
        return this.results;
    }
}
