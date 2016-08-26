package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

import javax.swing.*;
import java.awt.*;
import java.util.List;
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
    EventTablePanel eventTable;

    JSplitPane pJSP;
    OverviewContainer pContainer;

    JSplitPane sJSP;
    OverviewContainer sContainer;

    BaseFeature(StatusBar sb, HelpToolBar hb, OverviewContainer primary, OverviewContainer secondary, EventTablePanel tb, Help hp) {

        this.helpBar = hb;
        this.statusBar = sb;
        this.eventTable = tb;
        this.help = hp;

        if (primary != null) {
            this.pContainer = primary;
            this.pContainer.setFeature(this);
        }

        if (secondary != null) {
            this.sContainer = secondary;
            this.sContainer.setFeature(this);
        }

        buildUI();
    }

    void buildUI() {

        this.pContainer.setBackground(Color.white);

        JScrollPane sPane = new JScrollPane(this.pContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        eventTable.setVisible(false);

        pJSP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        pJSP.setDividerSize(0);
        pJSP.setResizeWeight(1);
        pJSP.setDividerLocation(pJSP.getSize().height - pJSP.getInsets().bottom - pJSP.getDividerSize());
        pJSP.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        this.setLayout(new BorderLayout());
        this.add(pJSP, BorderLayout.CENTER);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }

    @Override
    public void finishedLoading() {
        pContainer.finishedLoading();
    }

    @Override
    public void reset() {

        pContainer.reset();
        pContainer.revalidate();

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
    public void showPrimaryData(List<Event> events) {

        if (!eventTable.isVisible()) {

            pJSP.setDividerLocation(0.6);
            pJSP.setDividerSize(3);
            eventTable.setVisible(true);
        }

        eventTable.clearEvents();
        statusBar.setEvents(events);
        eventTable.setEvents(events);
    }
}
