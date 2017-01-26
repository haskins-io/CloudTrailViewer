/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package io.haskins.java.cloudtrailviewer.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class that provides a Key / Value TableModel
 *
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
