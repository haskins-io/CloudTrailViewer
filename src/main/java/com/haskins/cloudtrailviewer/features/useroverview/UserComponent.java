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
package com.haskins.cloudtrailviewer.features.useroverview;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.table.TableUtils;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class UserComponent extends JPanel {
    
    private final JPanel servicePanel = new JPanel();
    private final EventTablePanel eventTable = new EventTablePanel();
    
    private final Map<String, List<Event>> eventsPerService = new HashMap<>();
        
    public UserComponent(String name) {
        
        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel(name), BorderLayout.WEST);
        topPanel.add(servicePanel, BorderLayout.CENTER);
        topPanel.setBackground(Color.white);
        topPanel.setOpaque(true);
        
        eventTable.setVisible(false);
        
        this.add(topPanel, BorderLayout.PAGE_START);
        this.add(eventTable, BorderLayout.CENTER);
    }
    
    public void addEvent(Event event) {
        
        String serviceName = TableUtils.getService(event);
        List<Event> events = eventsPerService.get(serviceName);
        if (events == null) {
            events = new ArrayList();
            eventsPerService.put(serviceName, events);
        }
        
        events.add(event);
    }
    
    public void buildUI() {
        
        servicePanel.setAlignmentY(Component.TOP_ALIGNMENT);
        servicePanel.setBackground(Color.white);
        servicePanel.setOpaque(true);
        
        Set<String> keys = eventsPerService.keySet();
        List<String> sorted = GeneralUtils.asSortedList(keys);
        for (String serviceName : sorted) {
            
            final List<Event> events = eventsPerService.get(serviceName);
            ServiceLabel label = new ServiceLabel(serviceName, events.size());
            label.addMouseListener(new MouseAdapter(){

                @Override
                public void mouseClicked(MouseEvent e) {
                    eventTable.clearEvents();
                    eventTable.setEvents(events);
                    eventTable.setVisible(true);
                }
            });
            
            servicePanel.add(label);
        }
    }
}
