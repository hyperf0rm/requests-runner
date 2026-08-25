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

    public TopBar(double spacing) {
        super(spacing);
        this.methodChoiceBox = createMethodChoiceBox();
        this.urlTextField = createUrlTextField();
        this.sendButton = new Button("Send");
        this.getChildren().addAll(methodChoiceBox, urlTextField, sendButton);
        this.setPadding(new Insets(10, 10, 10, 10));
        HBox.setHgrow(this.urlTextField, Priority.ALWAYS);
    }

    public TopBar() {
        this(8);
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

    public String getURL() {
        return urlTextField.getText();
    }

    public HttpMethod getMethod() {
        return methodChoiceBox.getValue();
    }

    public Button getSendButton() {
        return sendButton;
    }
}
