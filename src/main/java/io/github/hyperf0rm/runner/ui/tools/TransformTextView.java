package io.github.hyperf0rm.runner.ui.tools;

import io.github.hyperf0rm.runner.service.tools.Codec;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

import java.util.function.UnaryOperator;

public class TransformTextView extends BorderPane {

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final HBox actionBar = new HBox();

    public TransformTextView(TransformTextAction... actions) {

        VBox inputVBox = createVBoxContainer("Input:", inputTextArea, true);
        VBox outputVBox = createVBoxContainer("Output:", outputTextArea, false);

        GridPane gridPane = createGrid();
        gridPane.add(inputVBox, 0, 0);
        gridPane.add(outputVBox, 1, 0);

        actionBar.setSpacing(10);
        actionBar.setPadding(new Insets(10, 10, 0, 10));
        actionBar.setAlignment(Pos.CENTER);

        for (TransformTextAction action : actions) {
            Button button = new Button(action.name());
            button.disableProperty().bind(inputTextArea.textProperty().isEmpty());
            button.setOnAction(event -> {
                String output = action.action().apply(inputTextArea.getText());
                outputTextArea.setText(output);
            });
            actionBar.getChildren().add(button);
        }

        setTop(actionBar);
        setCenter(gridPane);
        setMargin(gridPane, new Insets(10));
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

    private VBox createVBoxContainer(String labelName, TextArea textArea, boolean isEditable) {
        Label label = new Label(labelName);
        VBox container = new VBox(label, textArea);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        textArea.setEditable(isEditable);
        textArea.setWrapText(true);
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

        return gridPane;
    }

}
