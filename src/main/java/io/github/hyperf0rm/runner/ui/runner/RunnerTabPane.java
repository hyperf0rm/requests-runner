package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.repository.RequestHistoryRepository;
import io.github.hyperf0rm.runner.repository.RequestTabRepository;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class RunnerTabPane extends TabPane {

    private final Tab buttonTab = new Tab();
    private final RequestHistoryRepository historyRepository;
    private final RequestTabRepository tabRepository;

    public RunnerTabPane(RequestHistoryRepository historyRepository, RequestTabRepository tabRepository) {
        this.historyRepository = historyRepository;
        this.tabRepository = tabRepository;

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
        this.getTabs().add(buttonTab);
        this.setTabMaxWidth(300);

        List<Request> savedTabs = this.tabRepository.getRequestTabs();
        if (savedTabs != null && !savedTabs.isEmpty()) {
            for (Request request : savedTabs) {
                Tab tab = createRequestTab(new MainRunnerView(request, historyRepository));
                this.getTabs().add((this.getTabs().size() - 1), tab);
            }
        } else {
            this.getTabs().addFirst(createRequestTab());
        }
        this.getSelectionModel().selectFirst();

        this.getTabs().addListener((ListChangeListener<Tab>) c -> {
            while (c.next()) {
                if (c.wasRemoved() || c.wasAdded()) {
                    syncRequestTabsToRepository();
                }
            }
        });

    }

    private void syncRequestTabsToRepository() {
        List<Request> requests = new ArrayList<>();
        for (Tab tab : this.getTabs()) {
            if (tab.getContent() instanceof MainRunnerView view) {
                requests.add(view.getCurrentRequest());
            }
        }
        tabRepository.updateRequestTabs(requests);
    }

    public void saveRequestTabs() {
        syncRequestTabsToRepository();
    }

    public void openRequestInNewTab(Request request) {
        for (Tab tab : this.getTabs()) {
            if (tab.getContent() instanceof MainRunnerView view) {
                if (request.equals(view.getCurrentRequest())) {
                    this.getSelectionModel().select(tab);
                    return;
                }
            }
        }

        MainRunnerView view = new MainRunnerView(request, historyRepository);
        Tab newTab = createRequestTab(view);
        this.getTabs().add(this.getTabs().size() - 1, newTab);
        this.getSelectionModel().select(newTab);
    }

    private Tab createRequestTab() {
        return createRequestTab(new MainRunnerView(historyRepository));
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

        view.getTopBar().getUrlTextField().textProperty().addListener((
                obs, oldV, newV) -> syncRequestTabsToRepository()
        );
        view.getTopBar().getMethodChoiceBox().valueProperty().addListener(
                (obs, oldV, newV) -> syncRequestTabsToRepository()
        );

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
            syncRequestTabsToRepository();
        });

        MenuItem duplicateTab = new MenuItem("Duplicate Tab");
        duplicateTab.setOnAction(event -> {
            MainRunnerView view = (MainRunnerView) tab.getContent();
            Tab newTab = createRequestTab(view.duplicate());
            int index = this.getTabs().indexOf(tab) + 1;
            this.getTabs().add(index, newTab);
            this.getSelectionModel().select(newTab);
            syncRequestTabsToRepository();
        });

        MenuItem closeTab = new MenuItem("Close Tab");
        closeTab.setOnAction(event -> {
            this.getTabs().remove(tab);
            syncRequestTabsToRepository();
        });

        MenuItem closeAllTabs = new MenuItem("Close All Tabs");
        closeAllTabs.setOnAction(event -> {
            this.getTabs().removeIf(currentTab -> currentTab != buttonTab);
            syncRequestTabsToRepository();
        });

        MenuItem closeOtherTabs = new MenuItem("Close Other Tabs");
        closeOtherTabs.setOnAction(event -> {
            this.getTabs().removeIf(
                    currentTab -> currentTab != tab
                            && currentTab != buttonTab);
            syncRequestTabsToRepository();
        });
        closeOtherTabs.disableProperty().bind(Bindings.size(this.getTabs()).lessThanOrEqualTo(2));

        MenuItem closeTabsToTheRight = new MenuItem("Close Tabs To The Right");
        closeTabsToTheRight.setOnAction(event -> {
            this.getTabs().removeIf(
                    currentTab -> this.getTabs().indexOf(currentTab) > this.getTabs().indexOf(tab)
                            && currentTab != buttonTab);
            syncRequestTabsToRepository();
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
            syncRequestTabsToRepository();
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
