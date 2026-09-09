package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.controller.MainController;
import io.github.hyperf0rm.runner.model.Request;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


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

    public ExecutionPanel getExecutionPanel() {
        return this.executionPanel;
    }

    public TopBar getTopBar() {
        return this.topBar;
    }

    public RequestTabsPane getRequestTabsPane() {
        return this.requestTabsPane;
    }
}

