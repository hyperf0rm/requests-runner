package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.controller.MainController;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
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
    private final MainController controller = new MainController(this);

    public MainRunnerView() {
        this.topBar.getSendButton().setOnAction(event -> controller.sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            curlImportWindow.show(stage, this::applyParsedRequestToUI);
        });
        this.topBar.getCancelButton().setOnAction(event -> controller.cancel());

        SplitPane splitPane = new SplitPane(requestTabsPane, executionPanel);
        Platform.runLater(() -> {
            splitPane.setDividerPositions(0.65);
        });

        setTop(topBar);
        setCenter(splitPane);
        setMargin(splitPane, new Insets(10));

        controller.initParamBindings();
    }

    public MainRunnerView duplicate() {
        MainRunnerView duplicate = new MainRunnerView();
        duplicate.getTopBar().getMethodChoiceBox().setValue(this.getTopBar().getMethod());
        duplicate.getTopBar().getUrlTextField().setText(this.getTopBar().getUrl());
        duplicate.getRequestTabsPane().setBody(this.getRequestTabsPane().getBody());
        duplicate.getRequestTabsPane().setHeaders(this.getRequestTabsPane().getHeaders());
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

