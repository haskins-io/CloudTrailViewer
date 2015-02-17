package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.TopFiveTable;
import com.haskins.jcloudtrailerviewer.table.TopFiveTableModel;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author mark.haskins
 */
public class AnalysisPanel extends JPanel implements EventsDatabaseListener {
    
    private CopyOnWriteArrayList<Event> filteredEvents = new CopyOnWriteArrayList<>();
    
    private final DefaultCategoryDataset activityDataset = new DefaultCategoryDataset();
    private final DefaultCategoryDataset bar1Dataset = new DefaultCategoryDataset();
    private final DefaultCategoryDataset topIpsDataset = new DefaultCategoryDataset();
    private final DefaultPieDataset topEventsDataset = new DefaultPieDataset();
    
    private JComboBox optionsCombo;
    private final TopFiveTableModel topFiveTableModel = new TopFiveTableModel();
    
    static <K,V extends Comparable<? super V>> 
                List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries, 
                new Comparator<Entry<K,V>>() {
                    @Override
                    public int compare(Entry<K,V> e1, Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }
    
    public AnalysisPanel() {
        
        buildUI();
    }
    
    @Override
    public void onEventsUpdated(CopyOnWriteArrayList<Event> events) {
        
        filteredEvents = events;
                
        Thread updateCharts = new Thread() {
            @Override
            public void run() {
                
                List<Entry<String,Integer>> sorted = getEventsSortedByCriteria("eventName");
                List<Entry<String,Integer>> top5 = getTopX(sorted, 5);
                
                reloadTopEventsCharts(top5);
                //reloadActivtyChart(top5);

            };
        };
        updateCharts.start();
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(248,249,243));
        
        JPanel activityPanel = new JPanel();
        activityPanel.setLayout(new BorderLayout());
        ChartPanel activityChart = ChartCreator.createLineChart(activityDataset, 1280, 250);
        
        activityPanel.add(activityChart, BorderLayout.CENTER);
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(2,2,10,10));
        
        ChartPanel topEventsChartPanel = ChartCreator.createPieChart(topEventsDataset, 200, 200);
        
        ChartPanel bar1ChartPanel = ChartCreator.createBarChart(bar1Dataset, 600, 200,PlotOrientation.HORIZONTAL);
        ChartPanel TopIpsChartPanel = ChartCreator.createBarChart(topIpsDataset, 600, 200,PlotOrientation.HORIZONTAL);
              
        JPanel topValuesTablePanel = new JPanel();
        topValuesTablePanel.setLayout(new BorderLayout());
        
        TopFiveTable topFiveTable = new TopFiveTable(topFiveTableModel);
        topFiveTable.setPreferredSize(new Dimension(200,200));
        
        topValuesTablePanel.add(topFiveTable, BorderLayout.CENTER);
        
        String[] options = {"eventName","eventSource","sourceIPAddress", "userAgent", "recipientAccountId"};
        optionsCombo = new JComboBox(options);
        optionsCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                String selectedOption = optionsCombo.getSelectedItem().toString();
                
                List<Entry<String,Integer>> sorted = getEventsSortedByCriteria(selectedOption);
                List<Entry<String,Integer>> topX = getTopX(sorted, 10);  

                topFiveTableModel.update(topX);
            }
        });
        
        dataPanel.add(wrapChartPanel(topEventsChartPanel, "Top 5 Events", null) );
        dataPanel.add(wrapChartPanel(topValuesTablePanel, "Top Values", optionsCombo));
        dataPanel.add(wrapChartPanel(bar1ChartPanel, "", null));
        dataPanel.add(wrapChartPanel(TopIpsChartPanel, "Top Users", null));
        
        this.add(activityPanel, BorderLayout.NORTH);
        this.add(dataPanel, BorderLayout.CENTER);
    }
    
    private JPanel wrapChartPanel(JPanel chartPanel, String label, Component component) {
        
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(245,245,245));
        
        JLabel title = new JLabel(label);        
        header.add(title, BorderLayout.CENTER);
        
        if (component != null) {
            header.add(component, BorderLayout.EAST);
        }
        
        container.add(header, BorderLayout.NORTH);
        container.add(chartPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    private List<Entry<String,Integer>> getEventsSortedByCriteria(String criteria) {
        
        Map<String, Integer> eventsByOccurance = new HashMap<>();
        
        for (Event event : filteredEvents) {
            
            String property = getEventProperty(criteria, event);

            int count = 1;
            if (eventsByOccurance.containsKey(property)) {
                count = eventsByOccurance.get(property);
                count++;
            }

            eventsByOccurance.put(property, count);
        } 

        return entriesSortedByValues(eventsByOccurance);
    }
    
    private void reloadTopEventsCharts(List<Entry<String,Integer>> top5) {
        
        topEventsDataset.clear();
        
        int count = 5;
        if (top5.size() < 5) {
            count = top5.size();
        }
        
        for (int i=0; i<count; i++) {
            
            Entry<String,Integer> obj = top5.get(i);
            topEventsDataset.setValue(obj.getKey(), obj.getValue());
        }
    }
    
    private List<Entry<String,Integer>> getTopX(List<Entry<String,Integer>> sorted, int requiredNumber) {
        
        List<Entry<String,Integer>> top = new ArrayList<>();
        
        int count = requiredNumber;
        if (sorted.size() < requiredNumber) {
            count = sorted.size();
        }
        
        for (int i=0; i<count; i++) {
            
            top.add(sorted.get(i));
        }
        
        return top;
    }
    
    private void reloadActivtyChart(List<Entry<String,Integer>> sortedEntries) {
        
        activityDataset.clear();
        
        List<String> topEvents = new ArrayList<>();
        for (Entry<String, Integer> entry : sortedEntries) {
            topEvents.add(entry.getKey());
        }
        
        Map<String, Map<String, Integer>> tmpActivityMap = new HashMap();
        
        for (Event event : filteredEvents) {
            
            String eventName = event.getEventName();
            String eventTime = event.getEventTime();
            
            if (topEvents.contains(eventName)) {
                
                Map<String, Integer> eventMap = tmpActivityMap.get(eventName);
                if (eventMap != null) {
                    
                    if (eventMap.containsKey(eventTime)) {
                        
                        int count = eventMap.get(eventTime);
                        count++;
                        eventMap.put(eventTime, count);
                    }
                    else {
                        int count = 1;
                        eventMap.put(eventTime, count);
                    }
                   
                } else {
                    
                        int count = 1;
                        eventMap = new HashMap();
                        eventMap.put(eventTime, count);
                        tmpActivityMap.put(eventName, eventMap);
                }
            }
        }
        
        Set<String> nameSet = tmpActivityMap.keySet();
        Iterator<String> nameIt = nameSet.iterator();
        while (nameIt.hasNext()) {
            
            String eventName = nameIt.next();
            
            Map<String, Integer> valuesData = tmpActivityMap.get(eventName);
            Set<String> dataSet = valuesData.keySet();
            Iterator<String> valuesIt = dataSet.iterator();
            while (valuesIt.hasNext()) {
                
                String valuesKey = valuesIt.next();
                int count = valuesData.get(valuesKey);
                                
                activityDataset.addValue(count, eventName, valuesKey);
            }
        }
    }
    
    private String getEventProperty(String property, Event event) {
        
        String requiredValue = "";
        
        switch(property) {
            
            case "eventSource":
                requiredValue = event.getEventSource();
                break;
            case "eventName":
                requiredValue = event.getEventName();
                break;
            case "sourceIPAddress":
                requiredValue = event.getSourceIPAddress();
                break;
            case "userAgent":
                requiredValue = event.getUserAgent();
                break;
            case "recipientAccountId":
                requiredValue = event.getRecipientAccountId();
                break;
            default:
                requiredValue = "Not Supported";
        }
        
        return requiredValue;
    }
}
