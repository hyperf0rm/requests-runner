package io.github.hyperf0rm.runner.service;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunnerService {

    private final HttpClient client;

    public RunnerService() {
        this.client =  HttpClient.newHttpClient();
    }

    public List<Result> run(List<Request> requests) {

        List<Result> results = new ArrayList<>();
        int id = 1;

        for (Request request : requests) {
            long start = System.nanoTime();
            Result result = new Result();
            try {
                System.out.println("Request: " + request.getMethod() + " " + request.getUrl());
                HttpRequest finalRequest = buildFinalRequest(request);
                HttpResponse<String> response = client
                        .sendAsync(finalRequest, HttpResponse.BodyHandlers.ofString())
                        .join();
                result.setStatusCode(response.statusCode());
                result.setResponse(response.body());
            } catch (Exception e){
                System.out.printf("Error: %s\n", e.getMessage());
                result.setError(e.getMessage());
            } finally {
                long duration = Duration.ofNanos(System.nanoTime() - start).toMillis();
                result.setDuration(duration);
                result.setId(id);
                result.setURL(request.getUrl());
                result.setHeaders(request.getHeaders());
                result.setPayload(request.getBody());
                id++;
                System.out.printf("Finished Request: %s\n", request.getMethod() + " " + request.getUrl());
            }

            results.add(result);

        }
        return results;
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

}
