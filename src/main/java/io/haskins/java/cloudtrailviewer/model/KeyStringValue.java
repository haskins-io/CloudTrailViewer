package io.haskins.java.cloudtrailviewer.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class KeyStringValue {

    private final SimpleStringProperty key;
    private final SimpleStringProperty value;

    public KeyStringValue() {
        this("", "");
    }

    public KeyStringValue(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public String getKey() {
        return this.key.get();
    }
    public void setKey(String field) {
        this.key.set(field);
    }

    public String getValue() {
        return this.value.get();
    }
    public void setValue(String count) {
        this.value.set(count);
    }

    public String toString() {
        return key.get();
    }
}
