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
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartMouseEvent;

/**
 *
 * @author mark
 */
public class ScanChartPanel extends AbstractInternalFrame implements ActionListener {

    private final MenuDefinition menuDefinition;

    public ScanChartPanel(MenuDefinition menuDef) {

        super(menuDef.getName());

        menuDefinition = menuDef;

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
    public void newEvents(List<Event> scannedEvents) {

        for (Event event : scannedEvents) {
            
            // get required property value and store in map with count
            String value = EventUtils.getEventProperty(menuDefinition.getProperty(), event);
            if (value != null) {
                
                int count = 1;
                if (eventMap.containsKey(value)) {
                    count = eventMap.get(value) + 1;
                }

                eventMap.put(value, count);
            }
        }
    }

    @Override
    public void finishedLoading() {

        chartEvents = EventUtils.entriesSortedByValues(eventMap);

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

    @Override
    protected void updateTextArea() {

        if (chartEvents != null) {

            int count = 0;

            StringBuilder dataString = new StringBuilder();
            for (Map.Entry entry : chartEvents) {

                if (count >= chartData.getTop()) {
                    break;
                }

                dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(NEWLINE);
                count++;
            }

            eventDetailTextArea.setText(dataString.toString());
        }
    }

    @Override
    protected void updateChartEvents(int newTop) {
        
        chartEvents = EventUtils.entriesSortedByValues(eventMap);
        updateChart(newTop);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) { }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
}
