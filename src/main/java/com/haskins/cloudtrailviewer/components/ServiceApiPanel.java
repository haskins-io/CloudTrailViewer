/*
 * Copyright (C) 2015 Mark P. Haskins
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
package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.core.AwsService;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceApiPanel extends JPanel {
    
    private static final DefaultComboBoxModel servicesModel = new DefaultComboBoxModel();
    private static final DefaultComboBoxModel apisModel = new DefaultComboBoxModel();
    
    private JComboBox servicesCombo = null;
    private final JComboBox apisCombo = new JComboBox(apisModel);
    
    public ServiceApiPanel() {
        
        List<String> services = AwsService.getInstance().getServices();
        for (String service : services) {
            servicesModel.addElement(service);
        }
        
        buildUI();
    }
    
    public String getService() {
        return (String)servicesCombo.getSelectedItem();
    }
    
    public String getApi() {
        return (String)apisCombo.getSelectedItem();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        servicesCombo = new JComboBox(servicesModel);
        servicesCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String service = (String)cb.getSelectedItem();
                
                apisModel.removeAllElements();
                List<String> apis = AwsService.getInstance().getApiCallsForService(service);
                for (String api : apis) {
                   apisModel.addElement(api);
                }
            }
        });
        
        this.setLayout(new GridLayout(2,1));
        this.add(servicesCombo);
        this.add(apisCombo);
    }
}
