package io.github.hyperf0rm.runner.ui.runner;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;

public class RunnerTabPane extends TabPane {

    private final Tab buttonTab = new Tab();

    public RunnerTabPane() {
        Tab tab = createRequestTab();
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

    private Tab createRequestTab() {
        return createRequestTab(new MainRunnerView());
    }

    private Tab createRequestTab(MainRunnerView view) {
        Tab tab = new Tab();
        ContextMenu contextMenu = createContextMenu(tab);
        tab.setContextMenu(contextMenu);
        tab.setContent(view);
        tab.textProperty().bind(Bindings.createStringBinding(() -> {
            String method = view.getTopBar().getMethod().toString();
            String url = (!view.getTopBar().getUrl().isBlank()) ? view.getTopBar().getUrl() : "Request";
            return method + " " + url.trim();
        }, view.getTopBar().getMethodChoiceBox().valueProperty(), view.getTopBar().getUrlTextField().textProperty()));

        return tab;
    }

    private ContextMenu createContextMenu(Tab tab) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem newRequest = new MenuItem("New Request");
        newRequest.setOnAction(event -> {
            Tab newTab = createRequestTab();
            int index = this.getTabs().indexOf(tab) + 1;
            this.getTabs().add(index, newTab);
            this.getSelectionModel().select(newTab);
        });

        MenuItem duplicateTab = new MenuItem("Duplicate Tab");
        duplicateTab.setOnAction(event -> {
            MainRunnerView view = (MainRunnerView) tab.getContent();
            Tab newTab = createRequestTab(view.duplicate());
            int index = this.getTabs().indexOf(tab) + 1;
            this.getTabs().add(index, newTab);
            this.getSelectionModel().select(newTab);
        });

        MenuItem closeTab = new MenuItem("Close Tab");
        closeTab.setOnAction(event -> {
            this.getTabs().remove(tab);
        });

        MenuItem closeAllTabs = new MenuItem("Close All Tabs");
        closeAllTabs.setOnAction(event -> {
            this.getTabs().removeIf(currentTab -> currentTab != buttonTab);
        });

        MenuItem closeOtherTabs = new MenuItem("Close Other Tabs");
        closeOtherTabs.setOnAction(event -> {
            this.getTabs().removeIf(
                    currentTab -> currentTab != tab
                            && currentTab != buttonTab);
        });
        closeOtherTabs.disableProperty().bind(Bindings.size(this.getTabs()).lessThanOrEqualTo(2));

        MenuItem closeTabsToTheRight = new MenuItem("Close Tabs To The Right");
        closeTabsToTheRight.setOnAction(event -> {
            this.getTabs().removeIf(
                    currentTab -> this.getTabs().indexOf(currentTab) > this.getTabs().indexOf(tab)
                            && currentTab != buttonTab);
        });
        closeTabsToTheRight.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> this.getTabs().indexOf(tab) >= this.getTabs().size() - 2,
                        this.getTabs()));

        MenuItem closeTabsToTheLeft = new MenuItem("Close Tabs To The Left");
        closeTabsToTheLeft.setOnAction(event -> {
            this.getTabs().removeIf(
                    currentTab -> this.getTabs().indexOf(currentTab) < this.getTabs().indexOf(tab)
            );
        });
        closeTabsToTheLeft.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> this.getTabs().indexOf(tab) <= 0,
                        this.getTabs()));


        contextMenu.getItems().addAll(
                newRequest,
                duplicateTab,
                closeTab,
                closeAllTabs,
                closeOtherTabs,
                closeTabsToTheRight,
                closeTabsToTheLeft
        );

        return contextMenu;
    }
}
