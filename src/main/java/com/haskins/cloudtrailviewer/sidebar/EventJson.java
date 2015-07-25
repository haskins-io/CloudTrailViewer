/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author mark
 */
public class EventJson extends JPanel implements SideBar {
    
    public static final String NAME = "EventJson";
    
    private final JTextArea rawEvent = new JTextArea();
    
    public EventJson() {
        
        this.setLayout(new BorderLayout());
        
        rawEvent.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        JScrollPane rawJsonScrollPane = new JScrollPane(rawEvent);
        rawJsonScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        this.add(rawJsonScrollPane, BorderLayout.CENTER);
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return EventJson.NAME;
    }

    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Braces-32.png";
    }

    @Override
    public String getTooltip() {
        return "Show Event JSON";
    }

    @Override
    public void setCurrentEvent(Event event) {
        
        if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
        rawEvent.setText(event.getRawJSON());
    } 
}
