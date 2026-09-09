package io.github.hyperf0rm.runner.ui.tools;

import io.github.hyperf0rm.runner.controller.SearchController;
import io.github.hyperf0rm.runner.tool.Codec;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.List;
import java.util.function.UnaryOperator;

public class TransformTextView extends BorderPane {

    private final StyleClassedTextArea inputTextArea = new StyleClassedTextArea();
    private final StyleClassedTextArea outputTextArea = new StyleClassedTextArea();
    private final List<StyleClassedTextArea> textAreas = List.of(inputTextArea, outputTextArea);
    private final SearchController searchController = new SearchController();
    private final TextField searchField = new TextField();
    private final HBox searchBar = createSearchBar();
    private final Button searchButton = new Button("Search");

    public TransformTextView(TransformTextAction... actions) {
        GridPane gridPane = createGrid();
        HBox actionBar = createActionBar(actions);
        actionBar.getChildren().add(searchBar);
        setTop(actionBar);
        setCenter(gridPane);
        setMargin(gridPane, new Insets(10));
        initSearchEventHandlers();
    }

    public static TransformTextView forCodec(Codec codec) {
        return new TransformTextView(
                new TransformTextAction("Decode", codec::decode),
                new TransformTextAction("Encode", codec::encode)
        );
    }

    public static TransformTextView forSingleAction(String name, UnaryOperator<String> action) {
        return new TransformTextView(new TransformTextAction(name, action));
    }

    private void initSearchEventHandlers() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchController.search(textAreas, newValue);
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

    private VBox createVBoxContainer(String labelName, VirtualizedScrollPane<StyleClassedTextArea> scrollPane, boolean isEditable) {
        Label label = new Label(labelName);
        VBox container = new VBox(label, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getContent().setEditable(isEditable);
        scrollPane.getContent().setWrapText(true);
        return container;
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(50.0);
        leftCol.setHgrow(Priority.ALWAYS);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(50.0);
        rightCol.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(leftCol, rightCol);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().add(rowConstraints);

        inputTextArea.getStyleClass().add("input-text-area");
        outputTextArea.getStyleClass().add("output-text-area");

        VirtualizedScrollPane<StyleClassedTextArea> inputScrollPane = new VirtualizedScrollPane<>(inputTextArea);
        VirtualizedScrollPane<StyleClassedTextArea> outputScrollPane = new VirtualizedScrollPane<>(outputTextArea);

        VBox inputVBox = createVBoxContainer("Input:", inputScrollPane, true);
        VBox outputVBox = createVBoxContainer("Output:", outputScrollPane, false);

        gridPane.add(inputVBox, 0, 0);
        gridPane.add(outputVBox, 1, 0);

        return gridPane;
    }

    private HBox createActionBar(TransformTextAction... actions) {
        HBox actionBar = new HBox();
        actionBar.setSpacing(10);
        actionBar.setPadding(new Insets(10, 10, 0, 10));
        actionBar.setAlignment(Pos.CENTER);

        for (TransformTextAction action : actions) {
            Button button = new Button(action.name());
            button.disableProperty().bind(
                    Bindings.createBooleanBinding(
                            () -> inputTextArea.getLength() == 0,
                            inputTextArea.lengthProperty()
                    )
            );
            button.setOnAction(event -> {
                String output = action.action().apply(inputTextArea.getText());
                outputTextArea.replaceText(output);
                searchField.clear();
            });
            button.setFocusTraversable(false);
            actionBar.getChildren().add(button);
        }

        searchButton.setOnAction(event -> {
            searchBar.setVisible(true);
            searchField.requestFocus();
            searchButton.setVisible(false);
        });
        searchButton.managedProperty().bind(searchButton.visibleProperty());
        actionBar.getChildren().add(searchButton);

        return actionBar;
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
