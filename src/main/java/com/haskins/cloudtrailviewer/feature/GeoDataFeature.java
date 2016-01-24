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

package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.GeoIpUtils;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

/**
 *
 * @author mark.haskins
 */
public class GeoDataFeature extends JPanel implements Feature {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    public static final String NAME = "GeoData Feature";
    private static final long serialVersionUID = 2337766480593653058L;

    private final Help help = new Help("GeoData Feature", "geodata");

    private final OverviewContainer geoIpContainer;
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_EVENT);

    private JSplitPane jsp;

    private final StatusBar statusBar;
    private final HelpToolBar helpBar;
    
    private int highestCount = 0;
    private String centerPoint = "";

    public GeoDataFeature(StatusBar sb, HelpToolBar helpBar) {

        this.helpBar = helpBar;
        this.statusBar = sb;

        geoIpContainer = new OverviewContainer(this);

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
        return "Geo-Data-48.png";
    }

    @Override
    public String getTooltip() {
        return "GeoData Overview";
    }

    @Override
    public String getName() {
        return GeoDataFeature.NAME;
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
    public void showEventsTable(List<Event> events) {

        if (!eventTable.isVisible()) {

            jsp.setDividerLocation(0.5);
            jsp.setDividerSize(3);
            eventTable.setVisible(true);
        }

        eventTable.clearEvents();
        statusBar.setEvents(events);
        eventTable.setEvents(events);
    }

    @Override
    public void reset() {

        geoIpContainer.reset();
        geoIpContainer.revalidate();
        this.revalidate();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

        String cityName = event.getCity();
        if (cityName != null && cityName.trim().length() > 0) {
            int totalEvents = geoIpContainer.addEvent(event, "City");
            
            if (totalEvents > highestCount) {
                centerPoint = cityName;
                highestCount = totalEvents;
            }
        }
    }

    @Override
    public void finishedLoading() {
        geoIpContainer.finishedLoading();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        JButton browser = new JButton();
        browser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showHeatMap();
            }
        });
        ToolBarUtils.addImageToButton(browser, "Map-48.png", "", "Open Map");

        JToolBar mapToolBar = new JToolBar();
        mapToolBar.setLayout(new BorderLayout());
        mapToolBar.setBackground(Color.WHITE);
        mapToolBar.setFloatable(false);
        mapToolBar.add(browser, BorderLayout.EAST);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mapToolBar, BorderLayout.PAGE_START);

        geoIpContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(geoIpContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        mainPanel.add(sPane, BorderLayout.CENTER);

        eventTable.setVisible(false);

        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, mainPanel, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }

    private void showHeatMap() {

        Map<String, String> coords = GeoIpUtils.getInstance().getCoords();

        String center = "";

        StringBuilder output = new StringBuilder();
        
        for (Map.Entry<String, String> entry : coords.entrySet()) {
            
            output.append("['").append(entry.getValue()).append("',").append(entry.getKey()).append("]");

            if (entry.getValue().equalsIgnoreCase(centerPoint)) {
                center = entry.getKey();
            }
            
            output.append(",");
        }
        
        String html = getHTML();
        html = html.replaceAll("COORDS", output.toString());
        html = html.replaceAll("CENTER", center);

        writeGeoDataHtml(html);

        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("file://" + getFileName()));
        } catch (URISyntaxException | IOException ex) {
            LOGGER.log(Level.WARNING, "Failed to load Geo HTML file in browser", ex);
        }
    }

    private String getHTML() {

        StringBuilder result = new StringBuilder();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("GeoIp.html").getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load Geo HTML file", e);
        }

        return result.toString();
    }

    private void writeGeoDataHtml(String htmtContent) {
        
        try {
            File f = new File(getFileName());
            if (!f.delete()) {
                LOGGER.log(Level.WARNING, "Failed to delete existing Geo HTML file");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to delete existing Geo HTML file", e);
        }

        try (BufferedWriter out = new BufferedWriter(new FileWriter(getFileName()));) {
            out.write(htmtContent);
            out.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create new Geo HTML file", e);
        }
    }
    
    private String getFileName() {
        
        String userHomeDir = System.getProperty("user.home", ".");
        return userHomeDir + "/.cloudtrailviewer/geoData.html";
    }
}
