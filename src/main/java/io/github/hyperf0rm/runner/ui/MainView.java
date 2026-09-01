package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.controller.MainController;
import io.github.hyperf0rm.runner.model.Request;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainView extends BorderPane {

    private final TopBar topBar = new TopBar();
    private final RequestTabsPane requestTabsPane = new RequestTabsPane();
    private final ExecutionPanel executionPanel = new ExecutionPanel();
    private final CurlImportWindow curlImportWindow = new CurlImportWindow();
    private final MainController controller = new MainController(this);

    public MainView() {
        this.topBar.getSendButton().setOnAction(event -> controller.sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            curlImportWindow.show(stage, this::applyParsedRequestToUI);
        });

        SplitPane splitPane = new SplitPane(requestTabsPane, executionPanel);
        splitPane.setDividerPositions(0.5);
        SplitPane.setResizableWithParent(requestTabsPane, true);
        SplitPane.setResizableWithParent(executionPanel, false);

        setTop(topBar);
        setCenter(splitPane);
        setMargin(splitPane, new Insets(10));
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

