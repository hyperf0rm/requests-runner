package io.github.hyperf0rm.runner.repository;

import io.github.hyperf0rm.runner.data.AppDataManager;
import io.github.hyperf0rm.runner.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RequestHistoryRepository {

    private final ObservableList<Request> history = FXCollections.observableArrayList();

    public RequestHistoryRepository() {
        List<Request> savedRequests = AppDataManager.get().requests();
        if (savedRequests != null && !savedRequests.isEmpty()) {
            history.addAll(savedRequests);
        }
    }

    public ObservableList<Request> getHistory() {
        return history;
    }
    public void addRequest(Request request) {
        history.addFirst(request);
        AppDataManager.saveRequests(history);
    }
}
