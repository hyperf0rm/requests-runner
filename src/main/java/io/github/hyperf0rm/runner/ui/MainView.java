package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.controller.UiController;
import io.github.hyperf0rm.runner.model.Request;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainView extends BorderPane {

    private final TopBar topBar = new TopBar();
    private final Tabs tabs = new Tabs();
    private final RightPanel rightPanel = new RightPanel();
    private final CurlImportWindow curlImportWindow = new CurlImportWindow();
    private final UiController controller = new UiController(this);

    public MainView() {
        this.topBar.getSendButton().setOnAction(event -> controller.sendRequests());
        this.topBar.getImportCURLButton().setOnAction(event -> {
            Stage stage = (Stage) this.getScene().getWindow();
            curlImportWindow.show(stage, this::applyParsedRequestToUI);
        });

        SplitPane splitPane = new SplitPane(tabs, rightPanel);
        splitPane.setDividerPositions(0.5);
        SplitPane.setResizableWithParent(tabs, true);
        SplitPane.setResizableWithParent(rightPanel, false);

        setTop(topBar);
        setCenter(splitPane);
        setMargin(splitPane, new Insets(10));
    }

    private void applyParsedRequestToUI(Request request) {
        topBar.setUrl(request.url());
        topBar.setMethod(request.method());
        tabs.setBody(request.body());
        tabs.setHeaders(request.headers());
    }

    public RightPanel getRightPanel() {
        return this.rightPanel;
    }

    public TopBar getTopBar() {
        return this.topBar;
    }

    public Tabs getTabs() {
        return this.tabs;
    }
}

