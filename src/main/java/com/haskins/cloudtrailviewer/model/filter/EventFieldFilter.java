/*  
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2016  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.components.ServiceApiPanel;
import com.haskins.cloudtrailviewer.components.ServiceApiPanelListener;
import com.haskins.cloudtrailviewer.components.ServicePanel;
import com.haskins.cloudtrailviewer.components.ServicePanelListener;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.EventUtils;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mark.haskins
 */
public class EventFieldFilter extends AbstractFilter {

    private String fieldName;
    
    JLabel filterName = new JLabel();
    
    public void setOption(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public JPanel getFilterPanel(String name) {
        
        JPanel ui = new JPanel(new BorderLayout());
        
        filterName.setText(name + " ");
        filterName.setVisible(true);
        
        ui.add(filterName, BorderLayout.LINE_START);
        
        addInputPanel(ui);
        
        ui.setMinimumSize(DEFAULT_SIZE);
        ui.setPreferredSize(DEFAULT_SIZE);
        ui.setMaximumSize(DEFAULT_SIZE);
        
        return ui;
    }

    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        String fieldValue = EventUtils.getEventProperty(fieldName, event).toLowerCase();
        String lowerFilter = this.needle.toLowerCase();
        if (fieldValue.contains(lowerFilter)) {
            passesFilter = true;
        }
                
        return passesFilter;
    }

    @Override
    public boolean isNeedleSet() {
        
        boolean needleSet = false;
        
        if (needle != null && needle.length() > 0) {
            needleSet = true;
        }
        
        return needleSet;
    }
    
    private void addInputPanel(JPanel ui) {
        
        final JComponent component;
        
        switch (this.fieldName) {
            case "eventSource":
                component = new ServicePanel();
                ((ServicePanel)component).addListener( new ServicePanelListener() {
                    @Override
                    public void serviceChanged(String newService) {
                        needle = newService;
                    }
                });
                break;
                
            case "eventName":
                component = new ServiceApiPanel(ServiceApiPanel.ORIENTATION_HORIZONTAL);
                ((ServiceApiPanel)component).addListener( new ServiceApiPanelListener() {
                    @Override
                    public void apiSelected(String api) {
                        needle = api;
                    }
                });
                
                filterName.setVisible(false);
                break;
                
            default:

               component = new JTextField();
               component.addKeyListener(new KeyListener(){

                   @Override
                   public void keyTyped(KeyEvent e) {
                       needle = ((JTextField)component).getText();
                   }

                   @Override
                   public void keyPressed(KeyEvent e) { }

                   @Override
                   public void keyReleased(KeyEvent e) { }

               });
        }
        
        ui.add(component, BorderLayout.CENTER);
    }
}
