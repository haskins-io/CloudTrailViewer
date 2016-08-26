package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Container that holds APIs
 *
 * Created by markhaskins on 26/08/2016.
 */
public class ServiceApiContainer extends OverviewContainer {

    private static final long serialVersionUID = -8153765138734665655L;

    private final JPanel apiPanel = new JPanel();

    private final List<String> securityEvents = new ArrayList<>();
    private final Icon securityIcon;

    /**
     * Default Constructor.
     */
    public ServiceApiContainer() {

        super();

        this.setLayout(new GridLayout(1,1));

        apiPanel.setLayout(new BoxLayout(apiPanel, BoxLayout.Y_AXIS));
        this.add(apiPanel);

        loadSecurityEvents();
        securityIcon = ToolBarUtils.getIcon("Warning-48.png");
    }

    /**
     * Adds a collection of events to the panel
     * @param events Collection of Events to add to panel
     */
    @Override
    public void setEvents(List<Event> events) {

        eventsMap.clear();
        this.removeAll();

        for (Event event : events) {
            addEvent(event);
        }

        finishedLoading();
    }

    /**
     * Adds an events to the container.
     * @param event Event to add to container
     */
    public void addEvent(Event event) {
        addEventToPanel(event, event.getEventName());
    }

    @Override
    public void finishedLoading() {

        java.util.List<Map.Entry<String, NameValuePanel>> sortedPanels = entriesSortedByValues(eventsMap);
        for (Map.Entry<String, NameValuePanel> aPanel : sortedPanels) {

            String service = aPanel.getKey();
            NameValuePanel panel = eventsMap.get(service);

            apiPanel.add(panel);
        }

        this.revalidate();
    }

    @Override
    public void reset() {

        this.removeAll();
        eventsMap.clear();

        this.revalidate();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addEventToPanel(Event event, String apiCall) {

        final NameValuePanel resourcePanel;

        if (!eventsMap.containsKey(apiCall)) {

            Icon icon = null;
            if (securityEvents.contains(apiCall)) {
                icon = securityIcon;
            }

            resourcePanel = new NameValuePanel(apiCall, icon, feature);
            resourcePanel.addEvent(event);

            eventsMap.put(apiCall, resourcePanel);
        }
        else {

            resourcePanel = eventsMap.get(apiCall);
            resourcePanel.addEvent(event);
        }
    }

    private void loadSecurityEvents() {

        String query = "SELECT api_call FROM aws_security";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {

            String aws_name = (String)row.get("api_call");
            securityEvents.add(aws_name);
        }
    }
}
