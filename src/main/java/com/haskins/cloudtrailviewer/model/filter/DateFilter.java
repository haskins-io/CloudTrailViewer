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

/**
 *
 * @author markhaskins
 */
public class DateFilter extends AbstractFilter implements Filter {

    private static final String DIRECTION_FROM = "From";
    private static final String DAIRECTION_TO = "To";
    
    private static final String[] DIRECTIONS = { DIRECTION_FROM, DAIRECTION_TO };
    
    private boolean fromDate = true;
    
    @Override
    public JPanel getFilterPanel() {
        
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
        
        datePicker.getModel().setSelected(true);
        datePicker.getModel().addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                UtilDateModel selectedDate = (UtilDateModel)e.getSource();
            }
        });
        
        JPanel ui = new JPanel(new BorderLayout());
        ui.add(direction, BorderLayout.LINE_START);
        ui.add(datePicker, BorderLayout.CENTER);
        
        return ui;
    }

    @Override
    public boolean passesFilter(Event event) {
        
        boolean passes = false;
        
        if (fromDate) {
            passes = inFuture(event);
        } else {
            passes = inPast(event);
        }
        
        return false;
    }
    
    private boolean inFuture(Event event) {
        
        boolean inFuture = false;
        
        
        return inFuture;
    }
    
    private boolean inPast(Event event) {
        
        boolean inPast = false;
        
        
        return inPast; 
    }
}

class DateLabelFormatter extends AbstractFormatter {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(DATE_PATTERN);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return DATE_FORMATER.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return DATE_FORMATER.format(cal.getTime());
        }

        return "";
    }

}