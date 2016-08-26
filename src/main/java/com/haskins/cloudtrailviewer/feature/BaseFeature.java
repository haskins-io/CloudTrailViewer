package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Abstract class that contains common Feature functionality
 *
 * Created by markhaskins on 25/08/2016.
 */
abstract class BaseFeature extends JPanel implements Feature {

    protected final static Logger LOGGER = Logger.getLogger("CloudTrail");

    protected final HelpToolBar helpBar;
    protected Help help;

    private final StatusBar statusBar;

    protected OverviewContainer container;
    EventTablePanel eventTable;

    JSplitPane jsp;

    BaseFeature(StatusBar sb, HelpToolBar hb, OverviewContainer container, EventTablePanel tb, Help hp) {

        this.helpBar = hb;
        this.statusBar = sb;

        if (container != null) {
            this.container = container;
            this.container.setFeature(this);
        }

        eventTable = tb;

        help = hp;

        buildUI();
    }

    void buildUI() {

        container.setBackground(Color.white);

        JScrollPane sPane = new JScrollPane(container);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        eventTable.setVisible(false);

        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }

    @Override
    public void reset() {

        container.reset();
        container.revalidate();

        eventTable.clearEvents();
        eventTable.setVisible(false);

        this.revalidate();
    }

    @Override
    public boolean showOnToolBar() {
        return true;
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
    public void showPrimaryData(java.util.List<Event> events) {

        if (!eventTable.isVisible()) {

            jsp.setDividerLocation(0.5);
            jsp.setDividerSize(3);
            eventTable.setVisible(true);
        }

        eventTable.clearEvents();
        statusBar.setEvents(events);
        eventTable.setEvents(events);
    }
}
