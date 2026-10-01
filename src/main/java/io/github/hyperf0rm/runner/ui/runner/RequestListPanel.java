package io.github.hyperf0rm.runner.ui.runner;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.repository.RequestHistoryRepository;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


public class RequestListPanel extends VBox {

    private final TextField searchField = new TextField();
    private final ListView<Request> listView = new ListView<>();

    public RequestListPanel(RequestHistoryRepository repository, Consumer<Request> onRequestSelected) {
        VBox searchContainer = new VBox();
        searchContainer.setSpacing(5);
        searchContainer.getChildren().addAll(new Label("Search previous requests:"), searchField);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        FilteredList<Request> history = new FilteredList<>(repository.getHistory(), p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            history.setPredicate(request -> {
                if (newValue.isBlank()) {
                    return true;
                }

                String query = newValue.trim().toLowerCase();

                if (request.url().toLowerCase().contains(query)) {
                    return true;
                }

                if (request.body().toLowerCase().contains(query)) {
                    return true;
                }
                return false;
            });
        });

        listView.setItems(history);
        VBox.setVgrow(listView, Priority.ALWAYS);
        this.setSpacing(5);
        this.getChildren().addAll(searchContainer, listView);
        this.setPadding(new Insets(10));

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                onRequestSelected.accept(newValue);
                Platform.runLater(() -> listView.getSelectionModel().clearSelection());
            }
        });
    }
}
