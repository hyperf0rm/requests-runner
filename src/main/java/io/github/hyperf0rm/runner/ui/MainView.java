package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Header;
import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.service.RunnerService;
import io.github.hyperf0rm.runner.util.TemplateEngine;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;


public class MainView extends BorderPane {

    private TopBar topBar = new TopBar();
    private Tabs tabs = new Tabs();
    private RightPanel rightPanel = new RightPanel();
    private RunnerService runnerService = new RunnerService();
    private CurlImportWindow curlImportWindow = new CurlImportWindow();

    public MainView() {
        this.topBar.getSendButton().setOnAction(event -> sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            curlImportWindow.show(stage, this::applyParsedRequestToUI);
        });
        setTop(topBar);
        setCenter(tabs);
        setRight(rightPanel);
        setMargin(tabs, new Insets(10));
    }

    public void sendRequests() {
        String url = topBar.getURL();
        HttpMethod method =  topBar.getMethod();
        String body = tabs.getBody();
        List<Header> headers = tabs.getHeaders();
        List<String> values = rightPanel.getValues();
        Request request = new Request(method, url, headers, body);
        List<Request> requests = TemplateEngine.fillWithValues(request, values);

        rightPanel.clearResults();
        topBar.getSendButton().setDisable(true);

        Task<List<Result>> runTask = new Task<>() {
            @Override
            protected List<Result> call() {
                return runnerService.run(requests, MainView.this::handleSingleResult);
            }
        };

        runTask.setOnSucceeded(event -> topBar.getSendButton().setDisable(false));
        runTask.setOnFailed(event -> topBar.getSendButton().setDisable(false));

        Thread thread = new Thread(runTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleSingleResult(Result result) {
        Platform.runLater(() -> {
            rightPanel.addSingleResult(result);
        });
    }

    private void applyParsedRequestToUI(Request request) {
        topBar.setURL(request.url());
        topBar.setMethod(request.method());
        tabs.setBody(request.body());
        tabs.setHeaders(request.headers());
    }
}

