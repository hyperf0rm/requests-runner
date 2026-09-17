package io.github.hyperf0rm.runner.controller;

import io.github.hyperf0rm.runner.model.HttpTableEntry;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.service.RunnerService;
import io.github.hyperf0rm.runner.tool.UrlCodec;
import io.github.hyperf0rm.runner.ui.runner.HttpEntryTableView;
import io.github.hyperf0rm.runner.ui.runner.MainRunnerView;
import io.github.hyperf0rm.runner.ui.runner.TopBar;
import io.github.hyperf0rm.runner.util.TemplateEngine;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private final RunnerService runnerService = new RunnerService();
    private final MainRunnerView view;
    private Task<List<Result>> runTask;
    private final UrlCodec urlCodec = new UrlCodec();
    private boolean isUrlUpdating = false;

    public MainController(MainRunnerView view) {
        this.view = view;
    }

    public void sendRequests() {
        int errors = 0;

        String url = view.getTopBar().getUrl();
        long delay = 0L;

        if (!view.getExecutionPanel().getDelay().isBlank()) {
            try {
                delay = Long.parseLong(view.getExecutionPanel().getDelay());
                if (delay < 0) throw new IllegalArgumentException();
            } catch (Exception e) {
                view.getExecutionPanel().setDelayError();
                errors++;
            }
        }

        if (url == null || url.isBlank()) {
            view.getTopBar().setUrlError();
            errors++;
        }

        if (errors > 0) return;

        final long finalDelay = delay;
        String normalizedUrl = runnerService.normalizeUrl(url);

        Request request = new Request(
                view.getTopBar().getMethod(),
                normalizedUrl,
                view.getRequestTabsPane().getHeaders(),
                view.getRequestTabsPane().getBody()
        );
        List<String> values = view.getExecutionPanel().getValues();
        List<Request> requests = TemplateEngine.fillWithValues(request, values);

        view.getExecutionPanel().clearResults();
        view.getTopBar().getSendButton().setDisable(true);
        view.getTopBar().getCancelButton().setDisable(false);

        runTask = new Task<>() {
            @Override
            protected List<Result> call() {
                return runnerService.run(requests, finalDelay, result -> {
                    Platform.runLater(() -> {
                        view.getExecutionPanel().addSingleResult(result);
                    });
                });
            }
        };

        runTask.setOnCancelled(event -> {
            view.getTopBar().getSendButton().setDisable(false);
            view.getTopBar().getCancelButton().setDisable(true);
        });
        runTask.setOnSucceeded(event -> {
            view.getTopBar().getSendButton().setDisable(false);
            view.getTopBar().getCancelButton().setDisable(true);
        });
        runTask.setOnFailed(event -> {
            view.getTopBar().getSendButton().setDisable(false);
            view.getTopBar().getCancelButton().setDisable(true);
        });

        Thread thread = new Thread(runTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void cancel() {
        runTask.cancel(true);
    }

    public void initParamBindings() {
        TopBar topBar = view.getTopBar();
        HttpEntryTableView paramsTable = view.getRequestTabsPane().getParamsTable();

        topBar.getUrlTextField().textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUrlUpdating) {
                return;
            }
            isUrlUpdating = true;

            try {
                paramsTable.setEntries(extractParamsFromUrl(newValue));
            } finally {
                isUrlUpdating = false;
            }
        });

        paramsTable.setOnParamsChanged(params -> {
            if (isUrlUpdating) {
                return;
            }
            isUrlUpdating = true;

            try {
                updateUrlWithParams(topBar, params);
            } finally {
                isUrlUpdating = false;
            }
        });
    }

    private List<HttpTableEntry> extractParamsFromUrl(String url) {
        List<HttpTableEntry> entries = new ArrayList<>();
        if (!url.contains("?")) {
            return entries;
        }

        int queryIndex = url.indexOf("?");
        if (queryIndex == url.length() - 1) {
            return entries;
        }

        String queryString = url.substring(queryIndex + 1);
        String[] params = queryString.split("&");
        for (String param : params) {
            if (param.isBlank()) continue;
            String[] parts = param.split("=", 2);
            String key = urlCodec.decode(parts[0]);
            String value = parts.length > 1 ? urlCodec.decode(parts[1]) : "";
            entries.add(new HttpTableEntry(key, value));
        }
        return entries;
    }

    private void updateUrlWithParams(TopBar topBar, List<HttpTableEntry> params) {
        String currentUrl = topBar.getUrl();
        String baseUrl = currentUrl.contains("?") ? currentUrl.substring(0, currentUrl.indexOf("?")) : currentUrl;

        if (params.isEmpty()) {
            topBar.setUrl(baseUrl);
            return;
        }
        List<String> queryParams = new ArrayList<>();
        for (HttpTableEntry param : params) {
            queryParams.add(urlCodec.encode(param.getKey()) + "=" + urlCodec.encode(param.getValue()));
        }

        String query = String.join("&", queryParams);
        topBar.setUrl(baseUrl + "?" + query);
    }
}
