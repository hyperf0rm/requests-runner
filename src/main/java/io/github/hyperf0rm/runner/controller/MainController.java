package io.github.hyperf0rm.runner.controller;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.service.RunnerService;
import io.github.hyperf0rm.runner.ui.MainView;
import io.github.hyperf0rm.runner.util.TemplateEngine;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.List;

public class MainController {

    private final RunnerService runnerService = new RunnerService();
    private final MainView view;

    public MainController(MainView view) {
        this.view = view;
    }

    public void sendRequests() {

        Request request = new Request(
                view.getTopBar().getMethod(),
                view.getTopBar().getUrl(),
                view.getTabs().getHeaders(),
                view.getTabs().getBody()
        );
        List<String> values = view.getRightPanel().getValues();
        List<Request> requests = TemplateEngine.fillWithValues(request, values);

        view.getRightPanel().clearResults();
        view.getTopBar().getSendButton().setDisable(true);

        Task<List<Result>> runTask = new Task<>() {
            @Override
            protected List<Result> call() {
                return runnerService.run(requests, result -> {
                    Platform.runLater(() -> {
                        view.getRightPanel().addSingleResult(result);
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
