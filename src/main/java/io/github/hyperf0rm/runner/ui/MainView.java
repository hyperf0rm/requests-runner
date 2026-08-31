package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.HttpMethod;
import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.model.Result;
import io.github.hyperf0rm.runner.service.RunnerService;
import io.github.hyperf0rm.runner.util.CurlParser;
import io.github.hyperf0rm.runner.util.TemplateEngine;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainView extends BorderPane {

    private TopBar topBar = new TopBar();
    private Tabs tabs = new Tabs();
    private CurlParser curlParser = new CurlParser();
    private RightPanel rightPanel = new RightPanel();
    private TemplateEngine templateEngine = new TemplateEngine();
    private RunnerService runnerService = new RunnerService();

    public MainView() {
        this.topBar.getSendButton().setOnAction(event -> sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            showImportCURLPopup(stage);
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
        Map<String, String> headers = tabs.getHeaders();
        List<String> values = rightPanel.getValues();
        Request request = new Request(method, url, headers, body);
        List<Request> requests = templateEngine.fillWithValues(request, values);

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

    private void showImportCURLPopup(Stage ownerStage) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(ownerStage);
        popupStage.setTitle("Import cURL");

        TextArea textArea = new TextArea();
        Button importButton = new Button("Import");
        importButton.setOnAction(event -> {
            Request request = curlParser.parse(textArea.getText());
            topBar.setURL(request.getUrl());
            topBar.setMethod(request.getMethod());
            tabs.setBody(request.getBody());
            tabs.setHeaders(request.getHeaders());
            popupStage.close();
        });
        VBox layout = new VBox(10, new Label("Enter cURL:"), textArea, importButton);
        layout.setPadding(new Insets(10));
        Scene popupScene = new Scene(layout, 650, 400);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}

