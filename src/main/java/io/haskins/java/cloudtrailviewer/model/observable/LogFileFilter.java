package io.haskins.java.cloudtrailviewer.model.observable;

import io.haskins.java.cloudtrailviewer.controls.warningcell.WarningCell;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by markhaskins on 14/02/2017.
 */
public class LogFileFilter implements WarningCell {

    private final SimpleStringProperty name;
    private final SimpleStringProperty filter;
    private final SimpleStringProperty field;

    private final SimpleStringProperty needle;

    public LogFileFilter() {
        this("", "", "", "");
    }

    public LogFileFilter(String name, String filter, String field, String needle) {

        this.name = new SimpleStringProperty(name);
        this.filter = new SimpleStringProperty(filter);
        this.field = new SimpleStringProperty(field);
        this.needle = new SimpleStringProperty(needle);
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

    public String getNeedle() {
        return needle.get();
    }
    public void setNeedle(String needle) {
        this.needle.setValue(needle);
    }

    public boolean isfilterConfigure() {

        boolean set = false;

        if (needle != null && needle.get().length() > 0) {
            set = true;
        }

        return set;
    }

    public String toString() {
        return this.name.get();
    }

    public boolean displayWarning(int index) {

        return this.getNeedle() == null || this.getNeedle().length() == 0;

    }
}
