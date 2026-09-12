package io.github.hyperf0rm.runner.ui.notes;

import io.github.hyperf0rm.runner.model.Note;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NotesView extends GridPane {

    private final TextField titleField = new TextField();
    private final TextArea textArea = new TextArea();
    private final ListView<Note> listView = new ListView<>();


    public NotesView() {
        setupGrid();
        VBox rightContainer = createNoteContainer();
        VBox leftContainer = createListView();
        this.add(leftContainer, 0, 0);
        this.add(rightContainer, 1, 0);
        initListeners();
    }

    private void initListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            titleField.setVisible(true);
            textArea.setVisible(true);
            titleField.setText(newValue.getTitle());
            textArea.setText(newValue.getText());
        });

        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            Note note = listView.getSelectionModel().getSelectedItem();
            note.setTitle(newValue);
            listView.refresh();
            System.out.println(note);
        });

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            Note note = listView.getSelectionModel().getSelectedItem();
            note.setText(newValue);
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
        VBox container = new VBox();
        container.getChildren().addAll(titleField, textArea);
        container.setSpacing(10);
        textArea.setWrapText(true);
        titleField.setVisible(false);
        textArea.setVisible(false);
        VBox.setVgrow(textArea, Priority.ALWAYS);
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
                }
            }
        });
        buttonHBox.getChildren().addAll(addButton, deleteButton);
        container.getChildren().addAll(listView, buttonHBox);
        return container;
    }
}
