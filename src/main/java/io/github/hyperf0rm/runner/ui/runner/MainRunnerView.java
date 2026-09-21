package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.controller.MainController;
import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.HttpTableEntry;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.repository.RequestHistoryRepository;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;


public class MainRunnerView extends BorderPane {

    private final TopBar topBar = new TopBar();
    private final RequestTabsPane requestTabsPane = new RequestTabsPane();
    private final ExecutionPanel executionPanel = new ExecutionPanel();
    private final CurlImportWindow curlImportWindow = new CurlImportWindow();
    private final MainController controller;
    private final RequestHistoryRepository repository;

    public MainRunnerView(RequestHistoryRepository repository) {
        this.repository = repository;
        this.controller = new MainController(this, repository);
        this.topBar.getSendButton().setOnAction(event -> controller.sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            curlImportWindow.show(stage, this::applyParsedRequestToUI);
        });
        this.topBar.getCancelButton().setOnAction(event -> controller.cancel());

        SplitPane splitPane = new SplitPane(requestTabsPane, executionPanel);
        requestTabsPane.setPadding(new Insets(0, 10, 0, 0));
        Platform.runLater(() -> {
            splitPane.setDividerPositions(0.65);
        });

        setTop(topBar);
        setCenter(splitPane);
        setMargin(splitPane, new Insets(10));

        controller.initParamBindings();
    }

    public MainRunnerView(Request request, RequestHistoryRepository historyRepository) {
        this(historyRepository);
        this.setMethod(request.method());
        this.setUrl(request.url());
        this.setHeaders(request.headers());
        this.setBody(request.body());
    }

    public MainRunnerView duplicate() {
        MainRunnerView duplicate = new MainRunnerView(this.repository);
        duplicate.setMethod(this.getTopBar().getMethod());
        duplicate.setUrl(this.getTopBar().getUrl());
        duplicate.setBody(this.getRequestTabsPane().getBody());
        duplicate.setHeaders(this.getRequestTabsPane().getHeaders());
        return duplicate;
    }

    private void applyParsedRequestToUI(Request request) {
        topBar.setUrl(request.url());
        topBar.setMethod(request.method());
        requestTabsPane.setBody(request.body());
        requestTabsPane.setHeaders(request.headers());
    }

    public Request buildRequest(String normalizedUrl) {
        return new Request(
                topBar.getMethod(),
                normalizedUrl,
                requestTabsPane.getHeaders(),
                requestTabsPane.getBody()
        );
    }

    public Request getCurrentRequest() {
        return buildRequest(getUrl());
    }

    public void setRunningStatus(boolean running) {
        topBar.getSendButton().setDisable(running);
        topBar.getCancelButton().setDisable(!running);
    }

    public void addResult(Result result) {
        executionPanel.addSingleResult(result);
    }

    public void clearResults() {
        executionPanel.clearResults();
    }

    public List<String> getValuesForTemplate() {
        return executionPanel.getValues();
    }

    public String getUrl() {
        return topBar.getUrl();
    }

    public void setUrl(String url) {
        topBar.setUrl(url);
    }

    public void setMethod(HttpMethod method) {
        topBar.setMethod(method);
    }

    public void setHeaders(List<HttpTableEntry> headers) {
        requestTabsPane.setHeaders(headers);
    }

    public void setBody(String body) {
        requestTabsPane.setBody(body);
    }

    public String getDelay() {
        return executionPanel.getDelay();
    }

    public void setDelayError() {
        executionPanel.setDelayError();
    }

    public void setUrlError() {
        topBar.setUrlError();
    }

    public HttpEntryTableView getParamsTable() {
        return requestTabsPane.getParamsTable();
    }

    public TopBar getTopBar() {
        return this.topBar;
    }

    public RequestTabsPane getRequestTabsPane() {
        return this.requestTabsPane;
    }
}

