package io.github.hyperf0rm.runner.repository;

import io.github.hyperf0rm.runner.model.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RequestHistoryRepository {

    private final ObservableList<Request> history = FXCollections.observableArrayList();

    public ObservableList<Request> getHistory() {
        return history;
    }
    public void addRequest(Request request) {
        history.addFirst(request);
    }
}
