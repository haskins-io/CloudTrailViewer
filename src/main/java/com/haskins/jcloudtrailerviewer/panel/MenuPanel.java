/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.model.MenusDefinition;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark.haskins
 */
public class MenuPanel extends JMenuBar implements ActionListener {
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private final EventsDatabase eventsDatabase;
    private final EventLoader eventLoader;
    
    private final Map<String, JMenu> menusMap = new HashMap<>();
        
    public MenuPanel(EventLoader eventLoader, EventsDatabase database) {
        
        this.eventLoader = eventLoader;
        eventsDatabase = database;
          
        buildMenu();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "LoadLocal":
                loadFiles();
                break;
            case "LoadS3":
                loadS3Files();
                break;
            case "EventsByService":
                showEventsByServiceChart();
                break;
        }
    }
        
    private void buildMenu() {
        
        fileChooser.setMultiSelectionEnabled(true);
        
        // -- Menu : File
        JMenu menuFile = new JMenu("File");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                System.exit(0);
            }
        });
        
        menuFile.add(exit);
        
        
        // -- Menu : Events
        JMenu menuEvents = new JMenu("Events");
        
        JMenuItem loadLocal = new JMenuItem("Load Local Files");
        loadLocal.setActionCommand("LoadLocal");
        loadLocal.addActionListener(this);
        
        JMenuItem loadS3 = new JMenuItem("Load S3 Files");
        loadS3.setActionCommand("LoadS3");
        loadS3.addActionListener(this);
        
        if (!PropertiesSingleton.getInstance().validS3Credentials()) {
            loadS3.setEnabled(false);
        }
        
        JMenuItem clearDatabase = new JMenuItem(new AbstractAction("Clear Events") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                eventsDatabase.clear();
            }
        });
        
        menuEvents.add(loadLocal);
        menuEvents.add(loadS3);
        menuEvents.addSeparator();
        menuEvents.add(clearDatabase);
        
        
        // -- Menu : Services
        JMenu menuServices = new JMenu("Services");
        
        JMenuItem eventsByService = new JMenuItem("Events by Service");
        eventsByService.setActionCommand("EventsByService");
        eventsByService.addActionListener(this);
        
        menuServices.add(eventsByService);

        
        // -- Menu : About
        JMenu menuAbout = new JMenu("Help");
        
        JMenuItem about = new JMenuItem("Version " + jCloudTrailViewer.VERSION);
        
        menuAbout.add(about);
        
        this.add(menuFile);
        this.add(menuEvents);
        this.add(menuServices);
        
        createMenusFromFile();
        
        this.add(menuAbout);
    }
    
    private void loadFiles() {
        eventLoader.showFileBrowser();
    }
    
    private void loadS3Files() {
        eventLoader.showS3Browser();
    }
    
    private void showEventsByServiceChart() {
        
        if (eventsDatabase.getEventsPerService().size() > 0) {
            
            ChartData chartData = new ChartData();
            chartData.setChartStyle("bar");
            chartData.setChartSource("Events by Service");

            List<Map.Entry<String, Integer>> events = EventUtils.entriesSortedByValues(eventsDatabase.getEventsPerService());

            ChartWindow chart = new ChartWindow(chartData, events);
            chart.setVisible(true);

            jCloudTrailViewer.DESKTOP.add(chart);

            try {
                chart.setSelected(true);
            }
            catch (java.beans.PropertyVetoException pve) {
            }
           
        }
        else {

            JOptionPane.showMessageDialog(
                jCloudTrailViewer.DESKTOP,
                "No Events Loaded!",
                "Data Error",
                JOptionPane.WARNING_MESSAGE);
        }
        
    }
        
    private void createMenusFromFile() {
        
        ObjectMapper mapper = new ObjectMapper();
        
        MenusDefinition menus = null;
        
        try {
            
            String path = "./config/features.json";
            menus = mapper.readValue(new File(path), MenusDefinition.class);

        } catch (Exception e1) {
            
            Logger.getLogger(MenuPanel.class.getName()).log(Level.WARNING, "Couldn't load features");
                        
            try {
                
                /**
                 * This is here so I can find the file when running with within netbeans
                 */
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                File file = new File(classloader.getResource("config/features.json").getFile());

                menus = mapper.readValue(file, MenusDefinition.class);
                
            } catch (Exception e2) {
                Logger.getLogger(PropertiesSingleton.class.getName()).log(Level.WARNING, "Still no features file found");
            }
        }  
        
        if (menus != null) {
            createMenusFromModels(menus);
        }
    }
      
    private JMenu getMenu(String name, JMenu parent) {

        JMenu menu = null;
        
        int numMenus=parent.getItemCount();
        for (int i=0; i<numMenus; i++) {
            
            JMenuItem tmpMenu = parent.getItem(i);
            if (tmpMenu instanceof JMenu) {
       
                if (tmpMenu.getName() != null) {
                    String tmpName = tmpMenu.getName();
                    if (tmpName.equalsIgnoreCase(name)) {

                        menu = (JMenu)tmpMenu;
                        break;
                    }
                }
            }
        }
        
        return menu;
    }
    
    private void createMenusFromModels(MenusDefinition menus) {
            
        List<MenuDefinition> definitions = menus.getMenus();
        for (final MenuDefinition def : definitions) {
            
            boolean newTopLevelMenu = false;
            
            String menuName = def.getMenu();
            if (!menusMap.containsKey(menuName)) {   
                menusMap.put(menuName, new JMenu(menuName));
                newTopLevelMenu = true;
            }
            
            JMenuItem menuItem = new JMenuItem(new AbstractAction(def.getName()) {

                @Override
                public void actionPerformed(ActionEvent t) {
                    
                    JInternalFrame panel = null;
                    
                    if ( (def.getActions() != null && def.getActions().size() > 0) ||
                         (def.getContains() != null && def.getContains().length() > 0) ) {
                        
                        //panel = new ScanTablePanel(def);
                        panel = new CombinedPanel(def.getName(), null, def);
                        
                    } else if (def.getProperty() != null && def.getProperty().length() > 0) {
                        
                        panel = new ScanChartPanel(def);
                    }
                    
                    if (panel != null) {
                        
                        panel.setVisible(true);

                        jCloudTrailViewer.DESKTOP.add(panel);

                        try {
                            panel.setSelected(true);
                        }
                        catch (java.beans.PropertyVetoException pve) {
                        }
                    }
                }
            });
            
            JMenu parentMenu = menusMap.get(menuName);
            
            if (def.getSubMenu() != null && def.getSubMenu().length() > 0) {
                
                JMenu subMenu = getMenu(def.getSubMenu(), parentMenu);
                if (subMenu == null) {
                    
                    subMenu = new JMenu(def.getSubMenu());
                    subMenu.add(menuItem);
                    
                    parentMenu.add(subMenu);
                }
                
            } else {
                parentMenu.add(menuItem);
            }
            
            if (newTopLevelMenu) {
                this.add(parentMenu);
            }
                        
        }
    }
}
