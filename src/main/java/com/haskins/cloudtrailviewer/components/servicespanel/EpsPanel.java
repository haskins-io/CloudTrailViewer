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

package com.haskins.cloudtrailviewer.components.servicespanel;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.DateFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel that shows the Peak number of Event Per Second for a Service
 * 
 * @author mark.haskins
 */
class EpsPanel extends JPanel {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private final static DateFormatter DATE_FORMATTER = new DateFormatter();
    
    private static final long serialVersionUID = -756065840178911148L;
        
    private final SimpleDateFormat less_seconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private final SimpleDateFormat less_minutes = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    
    private final Map<Long, Integer> secondMap = new HashMap<>();
    private final Map<Long, Integer> minuteMap = new HashMap<>();
    private final Map<Long, Integer> hourMap = new HashMap<>();
    
    private int peakSecond = 0;
    private int peakMinute = 0;
    private int peakHour = 0;
    
    private final JLabel secondLabel = new JLabel(String.valueOf(peakSecond), SwingConstants.CENTER);
    private final JLabel minuteLabel = new JLabel(String.valueOf(peakMinute), SwingConstants.CENTER);
    private final JLabel hourLabel = new JLabel(String.valueOf(peakHour), SwingConstants.CENTER);
    
    EpsPanel(Color bgColour) {
        
        this.setBackground(bgColour);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(125,25));
        this.setMaximumSize(new Dimension(125,25));
        this.setPreferredSize(new Dimension(125,25));
                
        secondLabel.setToolTipText("Peak Events per Second");
        secondLabel.setForeground(Color.white);
        secondLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        minuteLabel.setToolTipText("Peak Events per Minute");
        minuteLabel.setForeground(Color.white);
        minuteLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        hourLabel.setToolTipText("Peak Events per Hour");
        hourLabel.setForeground(Color.white);
        hourLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel countPanel = new JPanel(new GridLayout(1,3));
        countPanel.add(secondLabel);
        countPanel.add(minuteLabel);
        countPanel.add(hourLabel);
        countPanel.setOpaque(false);
        
        this.add(countPanel, BorderLayout.CENTER);
        this.setOpaque(true);
    }
    
    /**
     * When called will adjust the Peak events per Second/Minute/Hour based on the
     * timestamp of the passed Event.
     * @param event Event to include in Peak events per second calculations
     */
    void newEvent(Event event) {
        
        try {
        
            boolean revalidate = false;

            Date d = DATE_FORMATTER.convertStringToDate(event.getEventTime());

            // Events per Second
            long time = event.getTimestamp();

            int count = 0;
            if (secondMap.containsKey(time)) {
                count = secondMap.get(time);
            }

            count++;
            secondMap.put(time, count);

            if (count > peakSecond) {
                
                peakSecond = count;
                secondLabel.setText(String.valueOf(peakSecond) + " ");
                revalidate = true;
            }

            // Events per Minute
            long lng_less_seconds = less_seconds.parse(less_seconds.format(d)).getTime();

            count = 0;
            if (minuteMap.containsKey(lng_less_seconds)) {
                count = minuteMap.get(lng_less_seconds);
            }

            count++;
            minuteMap.put(lng_less_seconds, count);

            if (count > peakMinute) {
                peakMinute = count;
                minuteLabel.setText(String.valueOf(peakMinute) + " ");
                revalidate = true;
            }

            // Events per Hour
            long lng_less_minutes = less_minutes.parse(less_minutes.format(d)).getTime();

            count = 0;
            if (hourMap.containsKey(lng_less_minutes)) {
                count = hourMap.get(lng_less_minutes);
            }

            count++;
            hourMap.put(lng_less_minutes, count);

            if (count > peakHour) {
                peakHour = count;
                hourLabel.setText( String.valueOf(peakHour));
                revalidate = true;
            }

            if (revalidate) {
                this.revalidate();
            }
        
        } catch (ParseException ex) {
            LOGGER.log(Level.WARNING, "Failed to add event", ex);
        }
    }
}
