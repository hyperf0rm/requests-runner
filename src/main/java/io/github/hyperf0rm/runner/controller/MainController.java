package io.github.hyperf0rm.runner.controller;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.service.RunnerService;
import io.github.hyperf0rm.runner.ui.runner.MainRunnerView;
import io.github.hyperf0rm.runner.util.TemplateEngine;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class MainController {

    private final RunnerService runnerService = new RunnerService();
    private final MainRunnerView view;

    public MainController(MainRunnerView view) {
        this.view = view;
    }

    public void sendRequests() {

        String url = view.getTopBar().getUrl();

        if (url == null || url.isBlank()) {
            view.getTopBar().setUrlError();
            return;
        }

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

        Task<List<Result>> runTask = new Task<>() {
            @Override
            protected List<Result> call() {
                return runnerService.run(requests, result -> {
                    Platform.runLater(() -> {
                        view.getExecutionPanel().addSingleResult(result);
                    });
                });
            }
        };

        runTask.setOnSucceeded(event -> view.getTopBar().getSendButton().setDisable(false));
        runTask.setOnFailed(event -> view.getTopBar().getSendButton().setDisable(false));

        Thread thread = new Thread(runTask);
        thread.setDaemon(true);
        thread.start();
    }
}
