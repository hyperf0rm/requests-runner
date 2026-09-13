package io.github.hyperf0rm.runner.ui.notes;

import io.github.hyperf0rm.runner.controller.SearchController;
import io.github.hyperf0rm.runner.model.Note;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.List;

public class NotesView extends GridPane {

    private final TextField titleField = new TextField();
    private final StyleClassedTextArea textArea = new StyleClassedTextArea();
    private final ListView<Note> listView = new ListView<>();
    private final SearchController searchController = new SearchController();
    private final TextField searchField = new TextField();
    private final HBox searchBar = createSearchBar();
    private final Button searchButton = new Button("Search");


    public NotesView() {
        setupGrid();
        VBox rightContainer = createNoteContainer();
        VBox leftContainer = createListView();
        this.add(leftContainer, 0, 0);
        this.add(rightContainer, 1, 0);
        initListeners();
        initSearchEventHandlers();
    }

    private void initListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            titleField.setVisible(true);
            textArea.setVisible(true);
            searchButton.setVisible(true);
            titleField.setText(newValue.getTitle());
            textArea.replaceText(newValue.getText());
        });

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            Note note = listView.getSelectionModel().getSelectedItem();
            note.setTitle(newValue);
            listView.refresh();
        });

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            Note note = listView.getSelectionModel().getSelectedItem();
            note.setText(newValue);
        });
    }

    private void initSearchEventHandlers() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchController.search(List.of(textArea), newValue);
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F && event.isControlDown()) {
                searchBar.setVisible(true);
                searchField.requestFocus();
                searchButton.setVisible(false);
            }
        });

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchController.moveToNextMatch();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                searchField.clear();
                searchBar.setVisible(false);
                searchButton.setVisible(true);
            }
        });
    }

    private void setupGrid() {
        this.setHgap(4);
        this.setPadding(new Insets(10));

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(25.0);
        leftCol.setHgrow(Priority.ALWAYS);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(75.0);
        rightCol.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(leftCol, rightCol);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        this.getRowConstraints().add(rowConstraints);
    }

    private VBox createNoteContainer() {
        textArea.setWrapText(true);
        textArea.setVisible(false);
        textArea.getStyleClass().add("input-text-area");
        VirtualizedScrollPane<StyleClassedTextArea> scrollPane = new VirtualizedScrollPane<>(textArea);
        titleField.setVisible(false);
        searchButton.setVisible(false);

        VBox container = new VBox();
        HBox titleBar = new HBox();
        titleBar.setSpacing(10);
        searchButton.setOnAction(event -> {
            searchBar.setVisible(true);
            searchField.requestFocus();
            searchButton.setVisible(false);
        });
        searchButton.managedProperty().bind(searchButton.visibleProperty());

        container.setSpacing(10);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(titleField, Priority.ALWAYS);
        titleBar.getChildren().addAll(titleField, searchButton, searchBar);
        container.getChildren().addAll(titleBar, scrollPane);
        return container;
    }

    private VBox createListView() {
        VBox container = new VBox();
        container.setSpacing(10);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox.setVgrow(listView, Priority.ALWAYS);
        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(10);
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            listView.getItems().add(new Note("Note", ""));
            listView.getSelectionModel().selectLast();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Note selectedNote = listView.getSelectionModel().getSelectedItem();
            if (selectedNote != null) {
                listView.getItems().remove(selectedNote);
                if (listView.getItems().isEmpty()) {
                    titleField.setVisible(false);
                    textArea.setVisible(false);
                    searchButton.setVisible(false);
                }
            }
        });
        buttonHBox.getChildren().addAll(addButton, deleteButton);
        container.getChildren().addAll(listView, buttonHBox);
        return container;
    }

    private HBox createSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setVisible(false);

        Button close = new Button("X");
        close.setOnAction(event -> {
            searchField.clear();
            searchBar.setVisible(false);
            searchButton.setVisible(true);
        });

        Button previous = new Button("<");
        previous.setOnAction(event -> {
            searchController.moveToPreviousMatch();
        });
        previous.disableProperty().bind(searchController.matchesCountProperty().lessThan(2));
        previous.setFocusTraversable(false);

        Button next = new Button(">");
        next.setOnAction(event -> {
            searchController.moveToNextMatch();
        });
        next.disableProperty().bind(searchController.matchesCountProperty().lessThan(2));
        next.setFocusTraversable(false);

        searchBar.getChildren().addAll(searchField, previous, next, close);
        searchBar.managedProperty().bind(searchBar.visibleProperty());

        return searchBar;
    }
}
