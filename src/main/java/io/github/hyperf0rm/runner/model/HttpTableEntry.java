package io.github.hyperf0rm.runner.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class HttpTableEntry {

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

    public HttpTableEntry(String key, String value) {
        setKey(key);
        setValue(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof HttpTableEntry that) {
            return this.getKey().equals(that.getKey())
                    && this.getValue().equals(that.getValue());
        } else {
            return false;
        }
    }
}
