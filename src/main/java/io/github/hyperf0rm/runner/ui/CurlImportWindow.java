package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.Request;
import io.github.hyperf0rm.runner.util.CurlParser;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class CurlImportWindow {

    public void show(Stage ownerStage, Consumer<Request> onImport) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(ownerStage);
        popupStage.setTitle("Import cURL");

        TextArea textArea = new TextArea();
        Button importButton = new Button("Import");
        importButton.setOnAction(event -> {
            String text = textArea.getText();
            if (text != null && !text.isBlank()) {
                Request request = CurlParser.parse(text);
                onImport.accept(request);
            }
            popupStage.close();
        });
        VBox layout = new VBox(10, new Label("Enter cURL:"), textArea, importButton);
        layout.setPadding(new Insets(10));
        Scene popupScene = new Scene(layout, 650, 400);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
