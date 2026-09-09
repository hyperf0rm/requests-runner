package io.github.hyperf0rm.runner.ui.runner;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class RunnerTabPane extends TabPane {

    public RunnerTabPane() {
        Tab tab = createRequestTab();
        Tab buttonTab = new Tab();
        buttonTab.getStyleClass().add("new-tab-button");
        buttonTab.setClosable(false);
        Button newTabButton = new Button("+");
        newTabButton.setPadding(new Insets(0, 10, 0, 10));
        newTabButton.setFocusTraversable(false);
        newTabButton.setOnAction(event -> {
            Tab newTab = createRequestTab();
            this.getTabs().add((this.getTabs().size() - 1), newTab);
            this.getSelectionModel().select(newTab);
        });
        buttonTab.setGraphic(newTabButton);

        buttonTab.setOnSelectionChanged(event -> {
            this.getSelectionModel().selectPrevious();
        });
        this.setTabMaxWidth(200);
        this.getTabs().addAll(tab, buttonTab);
    }

    public Tab createRequestTab() {
        Tab tab = new Tab();
        MainRunnerView view = new MainRunnerView();
        tab.setContent(view);
        tab.textProperty().bind(Bindings.createStringBinding(() -> {
            String method = view.getTopBar().getMethod().toString();
            String url = (!view.getTopBar().getUrl().isBlank()) ? view.getTopBar().getUrl() : "Request";
            return method + " " + url;
        }, view.getTopBar().getMethodChoiceBox().valueProperty(), view.getTopBar().getUrlTextField().textProperty()));

        return tab;
    }
}
