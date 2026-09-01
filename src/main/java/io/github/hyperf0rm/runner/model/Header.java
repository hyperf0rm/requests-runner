package io.github.hyperf0rm.runner.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Header {

    private StringProperty key;
    public String getKey() { return keyProperty().get(); }
    public void setKey(String key) { keyProperty().set(key); }
    public StringProperty keyProperty() {
        if (key == null) {
            key = new SimpleStringProperty(this, "key");
        }
        return key;
    }

    private StringProperty value;
    public String getValue() { return valueProperty().get(); }
    public void setValue(String value) { valueProperty().set(value); }
    public StringProperty valueProperty() {
        if (value == null) {
            value = new SimpleStringProperty(this, "value");
        }
        return value;
    }

    public Header(String key, String value) {
        setKey(key);
        setValue(value);
    }
}
