package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabase;
import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.github.githublemming.jcloudtrailerviewer.filter.AccountFilter;
import com.github.githublemming.jcloudtrailerviewer.filter.EventNameFilter;
import com.github.githublemming.jcloudtrailerviewer.filter.Filters;
import com.github.githublemming.jcloudtrailerviewer.filter.FreeformFilter;
import com.github.githublemming.jcloudtrailerviewer.filter.RegionFilter;
import com.github.githublemming.jcloudtrailerviewer.filter.UsernameFilter;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class FilterPanel extends JPanel implements EventsDatabaseListener {
    
    private final TextField search = new TextField();
    
    private final DefaultComboBoxModel accountModel = new DefaultComboBoxModel();
    private final DefaultComboBoxModel regionModel = new DefaultComboBoxModel();
    private final DefaultComboBoxModel usernameModel = new DefaultComboBoxModel();
    private final DefaultComboBoxModel eventModel = new DefaultComboBoxModel();
    
    private final Filters filters;
    private final AccountFilter accountFilter = new AccountFilter();
    private final EventNameFilter eventNameFilter = new EventNameFilter();
    private final FreeformFilter freeformFilter = new FreeformFilter();
    private final RegionFilter regionFilter = new RegionFilter();
    private final UsernameFilter usernameFilter = new UsernameFilter();
    
    public FilterPanel(Filters filters) {

        this.filters = filters;
        addFilters();
        
        buildUI();        
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventsDatabaseListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onEventsUpdated(CopyOnWriteArrayList<Event> events) {
        
        updateControls(events);
    }

    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        JPanel comboPanel = new JPanel();
        comboPanel.setPreferredSize(new Dimension(800, 25));
        
        comboPanel.setLayout(new GridLayout());
        comboPanel.add(createComboPanel("Account", accountModel));
        comboPanel.add(createComboPanel("Region", regionModel));
        comboPanel.add(createComboPanel("Username", usernameModel));
        comboPanel.add(createComboPanel("Event", eventModel));
        
        search.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) { 
                
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    freeformFilter.setValue(search.getText());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        });
        
        this.setLayout(new BorderLayout());
        //this.add(comboPanel, BorderLayout.WEST);
        this.add(search, BorderLayout.CENTER);
    }

    private JPanel createComboPanel(String name, DefaultComboBoxModel model) {
        
        JComboBox combo = new JComboBox(model);
        JLabel label = new JLabel(name);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(combo, BorderLayout.CENTER);
        
        return panel;
    }
        
    private void addFilters() {
        
        filters.addEventFilter(this.accountFilter);
        filters.addEventFilter(this.eventNameFilter);
        filters.addEventFilter(this.freeformFilter);
        filters.addEventFilter(this.regionFilter);
        filters.addEventFilter(this.usernameFilter);
    }
    
    private void updateControls(List<Event> events) {
        
        Map<String, Event> accountMap = new HashMap();
        Map<String, Event> regionMap = new HashMap();
        Map<String, Event> usernameMap = new HashMap();
        Map<String, Event> eventNameMap = new HashMap();
        
        accountMap.put("", null);
        usernameMap.put("", null);
        eventNameMap.put("", null);
        regionMap.put("", null);
        
        for(Event event : events) {
            
            if (event.getUserIdentity().getAccountId() != null &&
                event.getUserIdentity().getAccountId().length() > 0) {
                
                accountMap.put(event.getUserIdentity().getAccountId(), event);
            }
            
            if (event.getUserIdentity().getUserName() != null &&
                event.getUserIdentity().getUserName().length() > 0) {
                
                usernameMap.put(event.getUserIdentity().getUserName(), event);
            }
            
            if (event.getEventName() != null &&
                event.getEventName().length() > 0) {
                
                eventNameMap.put(event.getEventName(), event);
            }
            
            if (event.getAwsRegion() != null &&
                event.getAwsRegion().length() > 0) {
                
                regionMap.put(event.getAwsRegion(), event);
            }  
        }
        
        updateChoice(accountMap.keySet(), accountModel);
        updateChoice(usernameMap.keySet(), usernameModel);
        updateChoice(eventNameMap.keySet(), eventModel);
        updateChoice(regionMap.keySet(), regionModel);
    }
    
    private void updateChoice(Set<String> data, DefaultComboBoxModel model) {
        
        model.removeAllElements();
        
        for (String item : data) {
            model.addElement(item);
        }        
    }
}
