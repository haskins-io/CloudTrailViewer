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
package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.joda.time.DateTime;

/**
 *
 * @author markhaskins
 */
public class DateFilter extends AbstractFilter implements Filter {

    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATE_PATTERN);
        
    private static final String DIRECTION_FROM = "From";
    private static final String DAIRECTION_TO = "To";
    
    private static final String[] DIRECTIONS = { DIRECTION_FROM, DAIRECTION_TO };
    
    private boolean fromDate = true;
    
    private DateTime selectedDateTime;
    
    ///////////////////////////////////////////////////////////////////////////
    // Filter overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public JPanel getFilterPanel(String name) {
        
        final JComboBox direction = new JComboBox(DIRECTIONS);
        direction.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e) {
        
                String option = (String)direction.getSelectedItem();
                if (option.equalsIgnoreCase(DIRECTION_FROM)) {
                    fromDate = true;
                } else {
                    fromDate = false;
                }
            }
        });

        UtilDateModel model = new UtilDateModel();  
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        datePicker.getModel().addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                UtilDateModel selectedDate = (UtilDateModel)e.getSource();
                selectedDateTime = new DateTime(selectedDate.getValue());
            }
        });
        datePicker.getModel().setSelected(true);
        
        JPanel ui = new JPanel(new BorderLayout());
        ui.add(direction, BorderLayout.LINE_START);
        ui.add(datePicker, BorderLayout.CENTER);
        
        ui.setMinimumSize(defaultSize);
        ui.setPreferredSize(defaultSize);
        ui.setMaximumSize(defaultSize);
        
        return ui;
    }

    @Override
    public boolean passesFilter(Event event) {
        
        boolean passes;
        
        if (fromDate) {
            passes = afterFromDate(event);
        } else {
            passes = beforeToDate(event);
        }
        
        return passes;
    }
    
    @Override
    public boolean isNeedleSet() {
        
        boolean needleSet = false;
        
        if (selectedDateTime != null) {
            needleSet = true;
        }
        
        return needleSet;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    ///////////////////////////////////////////////////////////////////////////
    private boolean afterFromDate(Event event) {
        
        boolean inFuture = false;
        
        if (selectedDateTime.isBefore(event.getTimestamp())) {
            inFuture = true;
        }
        
        return inFuture;
    }
    
    private boolean beforeToDate(Event event) {
        
        boolean inPast = false;
        
        if (selectedDateTime.isAfter(event.getTimestamp())) {
            inPast = true;
        }
        
        return inPast; 
    }
}

class DateLabelFormatter extends AbstractFormatter {

    @Override
    public Object stringToValue(String text) throws ParseException {
        
        return DateFilter.DATE_FORMATER.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        
        if (value != null) {
            Calendar cal = (Calendar) value;
            return DateFilter.DATE_FORMATER.format(cal.getTime());
        }

        return "";
    }
}