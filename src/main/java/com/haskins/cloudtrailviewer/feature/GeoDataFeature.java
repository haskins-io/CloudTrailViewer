package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.GeoIpUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author mark.haskins
 */
public class GeoDataFeature extends JPanel implements Feature, EventDatabaseListener {

    public static final String NAME = "GeoData Feature";

    private final Help help = new Help("GeoData Feature", "geoData");

    private final OverviewContainer geoIpContainer;
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_EVENT);

    private JSplitPane jsp;

    private final StatusBar statusBar;
    private final HelpToolBar helpBar;

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
            geoIpContainer.addEvent(event, "City");
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

        JPanel topPanel = new JPanel(new BorderLayout());

        JButton browser = new JButton("Browser");
        browser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showHeatMap();
            }
        });

        topPanel.add(browser, BorderLayout.PAGE_START);

        geoIpContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(geoIpContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        topPanel.add(sPane, BorderLayout.CENTER);

        eventTable.setVisible(false);

        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topPanel, eventTable);
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
        Set<String> s = coords.keySet();
        Iterator<String> i = s.iterator();
        while (i.hasNext()) {

            String coord = i.next();
            String city = coords.get(coord);
            
            output.append("['").append(city).append("',").append(coord).append("],");

            center = coord;
        }

        String coordString = output.toString();
        coordString = coordString.substring(0, coordString.length() - 1);

        String html = getHTML();
        html = html.replaceAll("COORDS", coordString);
        html = html.replaceAll("CENTER", center);

        writeGeoDataHtml(html);

        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("file://" + getFileName()));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(GeoDataFeature.class.getName()).log(Level.SEVERE, null, ex);
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
            e.printStackTrace();
        }

        return result.toString();
    }

    private void writeGeoDataHtml(String htmtContent) {
        
        try {
            File f = new File(getFileName());
            f.delete();
        } catch (Exception e) {
            
        }

        try (BufferedWriter out = new BufferedWriter(new FileWriter(getFileName()));) {
            out.write(htmtContent);
            out.close();
        } catch (IOException e) {
        }
    }
    
    private String getFileName() {
        
        String userHomeDir = System.getProperty("user.home", ".");
        return userHomeDir + "/.cloudtrailviewer/geoData.html";
    }
}
