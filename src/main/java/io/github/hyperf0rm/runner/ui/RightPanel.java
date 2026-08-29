package io.github.hyperf0rm.runner.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RightPanel extends VBox {

    private final TextArea valuesTextArea;

    public RightPanel(double spacing) {
        super(spacing);
        this.valuesTextArea = new TextArea();
        this.getChildren().addAll(new Label("Enter values:"), this.valuesTextArea);
        this.setPadding(new Insets(10));
    }

    public RightPanel() {
        this(10);
    }

    public List<String> getValues() {
        String[] parts = valuesTextArea.getText().split("( |\n)+");
        return new ArrayList<>(Arrays.asList(parts));
    }
}
