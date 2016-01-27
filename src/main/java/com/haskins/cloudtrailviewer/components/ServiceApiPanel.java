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
    
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    
    private int orientation;
    
    private static final DefaultComboBoxModel MODEL_SERVICES = new DefaultComboBoxModel();
    private static final DefaultComboBoxModel MODEL_APIS = new DefaultComboBoxModel();
    private static final long serialVersionUID = 6392140943533697206L;
    
    private JComboBox servicesCombo = null;
    private final JComboBox apisCombo = new JComboBox(MODEL_APIS);
    
    private ServiceApiPanelListener listener;
    
    public ServiceApiPanel() {
        this(ORIENTATION_VERTICAL);
    }
    
    public ServiceApiPanel(int orientation) {
        
        this.orientation= orientation;
        
        List<String> services = AwsService.getInstance().getServices();
        for (String service : services) {
            MODEL_SERVICES.addElement(service);
        }
        
        buildUI();
    }
    
    /**
     * Returns the name of the selected service.
     * @return 
     */
    public String getService() {
        return (String)servicesCombo.getSelectedItem();
    }
    
    /**
     * Returns the name of the selected API.
     * @return 
     */
    public String getApi() {
        return (String)apisCombo.getSelectedItem();
    }
    
    public void addListener(ServiceApiPanelListener l) {
        this.listener = l;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        servicesCombo = new JComboBox(MODEL_SERVICES);
        servicesCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String service = (String)cb.getSelectedItem();
                
                MODEL_APIS.removeAllElements();
                List<String> apis = AwsService.getInstance().getApiCallsForService(service);
                for (String api : apis) {
                   MODEL_APIS.addElement(api);
                }
            }
        });
        
        apisCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String api = (String)cb.getSelectedItem();
                
                if (listener != null) {
                    listener.apiSelected(api);
                }
            }
        });
        
        if (orientation == ORIENTATION_VERTICAL) {
            this.setLayout(new GridLayout(2,1));
        } else {
            this.setLayout(new GridLayout(1,2));
        }

        this.add(servicesCombo);
        this.add(apisCombo);
    }
}
