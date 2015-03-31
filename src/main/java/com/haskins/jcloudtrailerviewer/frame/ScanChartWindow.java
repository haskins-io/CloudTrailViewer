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
package com.haskins.jcloudtrailerviewer.frame;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.components.TriDataPanel;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.util.List;

/**
 *
 * @author mark
 */
public class ScanChartWindow extends AbstractInternalFrame {

    private final TriDataPanel triPanel;
    
    public ScanChartWindow(MenuDefinition menuDef) {

        super(menuDef.getName());

        chartData.setChartSource(menuDef.getProperty());
        chartData.setTop(5);
        
        triPanel = new TriDataPanel(chartData, false);

        eventLoader.addListener(this);

        int scanDialogResult = 0;
        if (PropertiesSingleton.getInstance().validS3Credentials()) {
            scanDialogResult = showScanDialog();
        }
        
        if (scanDialogResult == 0) {
            buildUI();
            eventLoader.showFileBrowser();
        } else if (scanDialogResult == 1) {
            buildUI();
            eventLoader.showS3Browser();
        } else {
            this.dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> newEvents) {

        for (Event event : newEvents) {
            
            // get required property value and store in map with count
            String value = EventUtils.getEventProperty(chartData.getChartSource(), event);
            if (value != null) {
                events.add(event);
            }
        }
    }

    @Override
    public void finishedLoading() {

        triPanel.setEvents(events);
    }

    @Override
    public void newMessage(String message) { }

    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {

        this.setSize(500, 280);

        this.setJMenuBar(triPanel.getChartMenu());
        this.add(triPanel, BorderLayout.CENTER);
    }
}
