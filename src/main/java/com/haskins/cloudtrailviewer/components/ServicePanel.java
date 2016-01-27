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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class ServicePanel extends JPanel {
    
    private static final DefaultComboBoxModel MODEL_SERVICES = new DefaultComboBoxModel();
    
    private JComboBox servicesCombo = null;
    
    private ServicePanelListener listener;
        
    public ServicePanel() {
        
        super(new BorderLayout());
        
        List<String> services = AwsService.getInstance().getServices();
        for (String service : services) {
            MODEL_SERVICES.addElement(service);
        }
        
        servicesCombo = new JComboBox(MODEL_SERVICES);
        servicesCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String service = (String)cb.getSelectedItem();
                
                if (listener != null) {
                    listener.serviceChanged(AwsService.getInstance().getEndpointFromFriendlyName(service));
                }    
            }
        });
        
        this.add(servicesCombo, BorderLayout.CENTER);
    }
    
    public void addListener(ServicePanelListener l) {
        this.listener = l;
    }
}
