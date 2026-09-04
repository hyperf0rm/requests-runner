package io.github.hyperf0rm.runner.ui;

import io.github.hyperf0rm.runner.model.HttpMethod;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class TopBar extends HBox {

    private final ChoiceBox<HttpMethod> methodChoiceBox;
    private final TextField urlTextField;
    private final Button sendButton;
    private final Button importCURLButton;

    public TopBar() {
        super(8);
        this.methodChoiceBox = createMethodChoiceBox();
        this.urlTextField = createUrlTextField();
        this.urlTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                removeUrlError();
            }
        });
        this.sendButton = new Button("Send");
        this.importCURLButton = new Button("Import cURL");
        this.getChildren().addAll(importCURLButton, methodChoiceBox, urlTextField, sendButton);
        this.setPadding(new Insets(10));
        HBox.setHgrow(this.urlTextField, Priority.ALWAYS);
    }

    private ChoiceBox<HttpMethod> createMethodChoiceBox() {
        ChoiceBox<HttpMethod> cb = new ChoiceBox<>();
        cb.getItems().addAll(HttpMethod.values());
        cb.setValue(HttpMethod.GET);
        return cb;
    }

    private TextField createUrlTextField() {
        TextField tf = new TextField();
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setPromptText("URL");
        return tf;
    }

    public void setUrlError() {
        urlTextField.setStyle("-fx-border-color: #e74c3c;");
        urlTextField.requestFocus();
    }

    public void removeUrlError() {
        urlTextField.setStyle("");
    }

    public String getUrl() {
        return urlTextField.getText();
    }

    public HttpMethod getMethod() {
        return methodChoiceBox.getValue();
    }

    public Button getSendButton() {
        return sendButton;
    }

    public Button getImportCURLButton() { return  importCURLButton; }

    public void setUrl(String url) {
        urlTextField.setText(url);
    }

    public void setMethod(HttpMethod method) {
        methodChoiceBox.setValue(method);
    }

    public TextField getUrlTextField() {
        return urlTextField;
    }
}
