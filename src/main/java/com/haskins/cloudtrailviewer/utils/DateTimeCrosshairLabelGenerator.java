/*
 * Copyright (C) 2016 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.MessageFormat;
import org.jfree.chart.labels.CrosshairLabelGenerator;
import org.jfree.chart.plot.Crosshair;

/**
 *
 * @author markhaskins
 */
public class DateTimeCrosshairLabelGenerator implements CrosshairLabelGenerator, Serializable {

    private static final long serialVersionUID = 1845163455780633764L;

    /** The label format string. */
    private String labelTemplate;


    /** A number formatter for the value. */
    private DateFormat dateFormat;
    
    /**
     * Creates a new instance with default attributes.
     */
    public DateTimeCrosshairLabelGenerator() {
        this("{0}", DateFormat.getTimeInstance());
    }
    
    /**
     * Creates a new instance with the specified attributes.
     *
     * @param labelTemplate  the label template (<code>null</code> not
     *     permitted).
     * @param dateFormat  the date formatter (<code>null</code> not
     *     permitted).
     */
    private DateTimeCrosshairLabelGenerator(String labelTemplate,
                                            DateFormat dateFormat) {
        super();
        if (labelTemplate == null) {
            throw new IllegalArgumentException(
                    "Null 'labelTemplate' argument.");
        }
        if (dateFormat == null) {
            throw new IllegalArgumentException(
                    "Null 'dateFormat' argument.");
        }
        this.labelTemplate = labelTemplate;
        this.dateFormat = dateFormat;
    }
    

    /**
     * Returns the label template string.
     *
     * @return The label template string (never <code>null</code>).
     */
    public String getLabelTemplate() {
        return this.labelTemplate;
    }

    /**
     * Returns the date formatter.
     *
     * @return The formatter (never <code>null</code>).
     */
    public DateFormat getNumberFormat() {
        return this.dateFormat;
    }
    
    @Override
    public String generateLabel(Crosshair crosshair) {
        
        Object[] v = new Object[] {this.dateFormat.format(crosshair.getValue())};
        return MessageFormat.format(this.labelTemplate, v);
    }
    
}
