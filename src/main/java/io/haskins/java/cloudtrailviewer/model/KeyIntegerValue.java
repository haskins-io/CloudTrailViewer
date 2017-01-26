package io.haskins.java.cloudtrailviewer.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by markhaskins on 06/01/2017.
 */
public class KeyIntegerValue {

    private final SimpleStringProperty field;
    private final IntegerProperty count;

    public KeyIntegerValue(String field, int count) {
        this.field = new SimpleStringProperty(field);
        this.count = new SimpleIntegerProperty(count);
    }

    public String getField() {
        return this.field.get();
    }

    public void setField(String field) {
        this.field.set(field);
    }


    public int getCount() {
        return this.count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

}
