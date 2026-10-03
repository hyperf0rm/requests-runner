package io.github.hyperf0rm.runner.repository;

import io.github.hyperf0rm.runner.data.AppDataManager;
import io.github.hyperf0rm.runner.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RequestTabRepository {

    private final ObservableList<Request> tabs = FXCollections.observableArrayList();


    public RequestTabRepository() {
        List<Request> savedTabs = AppDataManager.get().requestTabs();
        if (savedTabs != null && !savedTabs.isEmpty()) {
            tabs.addAll(savedTabs);
        }
    }

    public ObservableList<Request> getRequestTabs() {
        return tabs;
    }

    public void updateRequestTabs(List<Request> updatedTabs) {
        tabs.setAll(updatedTabs);
        AppDataManager.saveRequestTabs(tabs);
    }
}
