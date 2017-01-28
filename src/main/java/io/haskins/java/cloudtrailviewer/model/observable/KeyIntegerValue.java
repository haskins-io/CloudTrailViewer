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

package io.haskins.java.cloudtrailviewer.model.observable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class that provides a Key / Value TableModel
 *
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
