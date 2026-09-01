package io.github.hyperf0rm.runner.service;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.util.JsonFormatter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RunnerService {

    private final HttpClient client;

    public RunnerService() {
        this.client =  HttpClient.newHttpClient();
    }

    public List<Result> run(List<Request> requests, Consumer<Result> onResult) {

        List<Result> results = new ArrayList<>();
        int id = 1;

        for (Request request : requests) {
            long start = System.nanoTime();
            Result result = new Result();
            try {
                HttpRequest finalRequest = buildFinalRequest(request);
                HttpResponse<String> response = client.send(finalRequest, HttpResponse.BodyHandlers.ofString());
                result.setStatusCode(response.statusCode());
                result.setResponse(JsonFormatter.formatJson(response.body()));
                Map<String, List<String>> headers = response.headers().map();
                List<Header> processedHeaders = new ArrayList<>();
                for (Map.Entry<String, List<String>> header : headers.entrySet()) {
                    if (header.getKey().startsWith(":")) {
                        continue;
                    }
                    String value = String.join(", ", header.getValue());
                    processedHeaders.add(new Header(header.getKey(), value));
                }
                result.setResponseHeaders(processedHeaders);
            } catch (Exception e){
                result.setError(e.getMessage());
            } finally {
                long duration = Duration.ofNanos(System.nanoTime() - start).toMillis();
                result.setDuration(duration);
                result.setId(id);
                result.setURL(request.url());
                result.setHeaders(request.headers());
                result.setPayload(request.body());
            }

            results.add(result);

            if (onResult != null) {
                onResult.accept(result);
            }

            if (id < requests.size()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            id++;
        }
        return results;
    }

    private HttpRequest buildFinalRequest(Request request) {

        try {
            HttpRequest.BodyPublisher bodyPublisher;
            if (request.method().requiresBody() && request.body() != null) {
                bodyPublisher = HttpRequest.BodyPublishers.ofString(request.body());
            } else {
                bodyPublisher = HttpRequest.BodyPublishers.noBody();
            }

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(request.url()))
                    .method(request.method().name(), bodyPublisher);
            List<Header> headers = request.headers();
            headers.forEach(h -> builder.header(h.getKey(), h.getValue()));

            return builder.build();

        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + e.getMessage());
            throw new IllegalArgumentException("Invalid URL", e);
        }
    }

}
