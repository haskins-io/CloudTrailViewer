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
package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.util.List;
import org.jfree.chart.ChartMouseEvent;

/**
 *
 * @author mark
 */
public class ScanChartPanel extends AbstractInternalFrame {

    public ScanChartPanel(MenuDefinition menuDef) {

        super(menuDef.getName());

        chartData.setChartSource(menuDef.getProperty());
        chartData.setTop(5);

        eventLoader.addListener(this);

        int scanDialogResult = showScanDialog();
        if (scanDialogResult == 0) {
            eventLoader.showFileBrowser();
            buildUI();
        }
        else if (scanDialogResult == 1) {
            eventLoader.showS3Browser();
            buildUI();
        }
        else {
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

        generateInitialChartData();
        addTabbedChartDetail(480, 160);

        this.validate();
    }

    @Override
    public void newMessage(String message) { }

    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {

        this.setSize(500, 280);

        addTopXmenu();
        addStatusBar();
        
        this.add(tabs, BorderLayout.CENTER);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) { }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
}
