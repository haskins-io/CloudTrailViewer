package io.haskins.java.cloudtrailviewer.model.observable;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by markhaskins on 14/02/2017.
 */
public class FilterChoiceObservable {

    private final SimpleStringProperty name;
    private final SimpleStringProperty filter;
    private final SimpleStringProperty field;

    public FilterChoiceObservable() {
        this("", "", "");
    }

    public FilterChoiceObservable(String name, String filter, String field) {

        this.name = new SimpleStringProperty(name);
        this.filter = new SimpleStringProperty(filter);
        this.field = new SimpleStringProperty(field);
    }

    public String getName() {
        return this.name.get();
    }
    public void setName(String field) {
        this.name.set(field);
    }

    public String getFilter() {
        return this.filter.get();
    }
    public void setFilter(String count) {
        this.filter.set(count);
    }

    public String getField() {
        return this.field.get();
    }
    public void setField(String count) {
        this.field.set(count);
    }
}
