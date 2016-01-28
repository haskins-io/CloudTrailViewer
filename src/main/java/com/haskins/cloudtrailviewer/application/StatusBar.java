/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class that provides the Status Bar component
 *
 * @author mark
 */
public class StatusBar extends JPanel {

    private static final long serialVersionUID = 3812674421318175487L;

    private final JPanel messageLabel = new JPanel();

    private final JLabel from = new JLabel();
    private final JLabel to = new JLabel();

    private final JLabel visibleEvents = new JLabel();
    private final JLabel loadedEvents = new JLabel();
    private final JLabel statusMessage = new JLabel();
    
    private final MemoryCheck memoryCheck;
    private final static JLabel MEMORY = new JLabel();

    private long earliestEvent = -1;
    private long latestEvent = -1;

    /**
     * Default Constructor
     */
    public StatusBar() {

        memoryCheck = new MemoryCheck();
        
        buildStatusBar();
        
    }

    /**
     * Sets a message in the center section of the bar
     *
     * @param message message to display
     */
    public void setStatusMessage(String message) {
        statusMessage.setText(message);

        if (message.length() == 0) {
            messageLabel.setVisible(false);
        } else {
            messageLabel.setVisible(true);
        }
    }

    /**
     * When called will calculate the earliest and latest Event timestamp.
     *
     * @param event
     */
    public void newEvent(Event event) {

        if (earliestEvent == -1 || event.getTimestamp() < earliestEvent) {
            earliestEvent = event.getTimestamp();
            this.setFromDate(event.getEventTime());
        }

        if (latestEvent == -1 || event.getTimestamp() > latestEvent) {
            latestEvent = event.getTimestamp();
            this.setToDate(event.getEventTime());
        }
    }

    /**
     * When called will calculate the earliest and latest Event timestamp.
     *
     * @param events
     */
    public void setEvents(List<Event> events) {

        earliestEvent = -1;
        latestEvent = -1;

        setVisibleEvents(events.size());

        for (Event event : events) {
            newEvent(event);
        }
    }

    /**
     * resets all the labels, and hides those that should not be visible when no
     * events are loaded.
     */
    public void eventsCleared() {
        messageLabel.setVisible(false);
        from.setVisible(false);
        to.setVisible(false);
        loadedEvents.setVisible(false);
        visibleEvents.setVisible(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void setFromDate(String from) {

        this.from.setVisible(true);
        this.from.setText(from);
    }

    private void setToDate(String to) {

        this.to.setVisible(true);
        this.to.setText(to);
    }

    /**
     * Indicates the number of Loaded events in the right section of the bar
     *
     * @param eventCount
     */
    public void setLoadedEvents(int eventCount) {

        loadedEvents.setVisible(true);
        loadedEvents.setText("Events Loaded : " + eventCount);
    }

    private void setVisibleEvents(int eventCount) {

        visibleEvents.setVisible(true);
        visibleEvents.setText("Current Events : " + eventCount);
    }

    private void buildStatusBar() {

        JPanel timeCovered = new JPanel();
        timeCovered.add(from);
        timeCovered.add(new JLabel(" - "));
        timeCovered.add(to);

        JPanel visibleEventsLabel = new JPanel();
        visibleEventsLabel.add(visibleEvents);

        messageLabel.add(statusMessage);

        JPanel loadedEventsLabel = new JPanel();
        loadedEventsLabel.add(loadedEvents);

        JPanel memoryLabel = new JPanel();
        memoryLabel.add(MEMORY);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(timeCovered);
        panel.add(visibleEventsLabel);
        panel.add(messageLabel);
        panel.add(loadedEventsLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(memoryLabel);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    private static class MemoryCheck implements Serializable {

        private static final long serialVersionUID = 4016944833521287316L;

        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        private final Runtime runtime = Runtime.getRuntime();

        public MemoryCheck() {
            checkMemory();
        }
        
        private void checkMemory() {

            final Runnable memoryChecker = new Runnable() {

                @Override
                public void run() {

                    long total = runtime.totalMemory() / 1024 / 1024;
                    long free = runtime.freeMemory() / 1024 / 1024;
                    long max = runtime.maxMemory() / 1024 / 1024;
                    long used = total - free;

                    MEMORY.setText(String.format("Memory : Used %sMb | Free %dMb | Max Available %dMb", used, free, max));
                }
            };
                
            scheduler.scheduleAtFixedRate(memoryChecker, 1, 5, SECONDS);
        }
    }
}
