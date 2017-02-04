package io.haskins.java.cloudtrailviewer.model.observable;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by markhaskins on 04/02/2017.
 */
public class StringObservable {

    private SimpleStringProperty value = new SimpleStringProperty("");

    public StringObservable(String value) {
        this.value.set(value);
    }

    public String getValue() {
        return this.value.get();
    }
}
