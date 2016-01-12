/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2016  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.thirdparty.WrapLayout;
import com.haskins.cloudtrailviewer.utils.TableUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author mark.haskins
 */
public class MetricsFeature extends JPanel implements Feature, ActionListener {

    public static final String NAME = "Metrics Feature";

    private final Help help = new Help("Metrics Feature", "metrics");
    
    private final HelpToolBar helpBar;
    
    private final JToolBar toolbar = new JToolBar();
    private final JPanel chartCards = new JPanel(new CardLayout());
    
    private final Map<String, List<Event>> serviceSorted = new HashMap<>();
    private final Map<String, XYDataset> timeservicePerService = new HashMap<>();
    
    private final SimpleDateFormat less_seconds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    
    public MetricsFeature(StatusBar sb, HelpToolBar helpBar) {

        this.helpBar = helpBar;
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Metrics-48.png";
    }

    @Override
    public String getTooltip() {
        return "Metrics Overview";
    }

    @Override
    public String getName() {
        return MetricsFeature.NAME;
    }

    @Override
    public void will_hide() {
        helpBar.setHelp(null);
    }

    @Override
    public void will_appear() {
        helpBar.setHelp(help);
    }

    @Override
    public void showEventsTable(List<Event> events) {}

    @Override
    public void reset() {}

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

        List<Event> allEvents = serviceSorted.get("ALL");
        if (allEvents == null) {
            allEvents = new ArrayList<>();
            addButton("ALL");
        }
        allEvents.add(event);
        serviceSorted.put("ALL", allEvents);
        
        
        String serviceName = TableUtils.getService(event);
        List<Event> events = serviceSorted.get(serviceName);
        if (events == null) {
            events = new ArrayList<>();
            addButton(serviceName);
        }
        events.add(event);
        serviceSorted.put(serviceName, events);
    }

    @Override
    public void finishedLoading() {
      
        showChart("ALL");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        showChart(e.getActionCommand());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {
        
        toolbar.setLayout(new WrapLayout(FlowLayout.CENTER, 1, 1));
        toolbar.setFloatable(false);
        toolbar.setBackground(Color.white);
        toolbar.setBorder(BorderFactory.createMatteBorder(1,0,1,0, Color.black));
        
        this.setLayout(new BorderLayout());
        this.add(toolbar, BorderLayout.PAGE_START);
        this.add(chartCards, BorderLayout.CENTER);
    }
    
    private void showChart(String service) {
        
        chartCards.removeAll();
        
        XYDataset chartData = timeservicePerService.get(service);
        if (chartData == null) {
            chartData = generateTimeSeriesData(service);
            timeservicePerService.put(service, chartData);
        }
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(service, "Range", "Count", chartData, false, true, false);
        
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        TextTitle t = chart.getTitle();
        t.setFont(new Font("Arial", Font.BOLD, 14));
        
        ChartPanel chartPanel = new ChartPanel( chart );
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setMouseZoomable( true , false );    
        
        chartCards.add(chartPanel, "");
        chartCards.revalidate();
    }
    
    private XYDataset generateTimeSeriesData(String service) {
        
        List<Event> events = serviceSorted.get(service);
        
        Map<Long, Integer> tickCount = new HashMap<>();
        for (Event event : events) {
            
            try {
                long minutes = less_seconds.parse(less_seconds.format(event.getTimestamp())).getTime();
                Integer count = tickCount.get(minutes);
                if (count == null) {
                    count = 1;
                }
                count ++;
                tickCount.put(minutes, count);
            } catch (ParseException ex) {
                Logger.getLogger(MetricsFeature.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        TimeSeries series = new TimeSeries(service); 
        
        Set<Long> keys = tickCount.keySet();
        Iterator<Long> it = keys.iterator();
        while (it.hasNext()) {
            Long minutes = it.next();
            Integer count = tickCount.get(minutes);
            
            Minute minute = new Minute(new Date(minutes));
            series.add(minute, count);
        }
        
        return new TimeSeriesCollection(series);
    }
    
    private void addButton(String service) {
            
        JButton btn = new JButton(service);
        btn.addActionListener(this);
        btn.setActionCommand(service);
        
        toolbar.add(btn);
    }
}
