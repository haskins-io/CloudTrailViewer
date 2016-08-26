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
import com.haskins.cloudtrailviewer.components.ServiceApiContainer;
import com.haskins.cloudtrailviewer.components.servicespanel.ServiceOverviewContainer;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

import javax.swing.*;
import java.awt.*;

/**
 * Feature that shows loaded events broken down into AWS Services
 * 
 * @author mark
 */
public class OverviewFeature extends BaseFeature {
    
    public static final String NAME = "Overview Feature";
    private static final long serialVersionUID = -2287861024079990428L;

    public OverviewFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new ServiceOverviewContainer(),
                new ServiceApiContainer(),
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Overview", "overview")
        );
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// BaseFeature implementation
    ////////////////////////////////////////////////////////////////////////////
    void buildUI() {

        this.pContainer.setBackground(Color.white);
        this.sContainer.setBackground(Color.white);

        JScrollPane primaryPane = new JScrollPane(this.pContainer);
        primaryPane.setBackground(Color.WHITE);
        primaryPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        JScrollPane secondaryPane = new JScrollPane(this.sContainer);
        secondaryPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        eventTable.setVisible(false);
        sContainer.setVisible(true);
        sContainer.setVisible(false);

        sJSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, primaryPane, secondaryPane);
        sJSP.setDividerSize(2);
        sJSP.setResizeWeight(0.9);
        sJSP.setDividerLocation(0.9);
        sJSP.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        sJSP.setVisible(true);

        pJSP = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sJSP, eventTable);
        pJSP.setDividerSize(0);
        pJSP.setResizeWeight(1);
        pJSP.setDividerLocation(pJSP.getSize().height - pJSP.getInsets().bottom - pJSP.getDividerSize());
        pJSP.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        pJSP.setVisible(true);

        this.setLayout(new BorderLayout());
        this.add(pJSP, BorderLayout.CENTER);

    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getIcon() {
        return "Service-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "Service API Overview";
    }
    
    @Override
    public String getName() {
        return OverviewFeature.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

        ((ServiceOverviewContainer)pContainer).addEvent(event);
        ((ServiceApiContainer)sContainer).addEvent(event);
    }

    @Override
    public void finishedLoading() {

        sContainer.finishedLoading();
        sContainer.setVisible(true);

        sJSP.setDividerSize(3);
        sJSP.setResizeWeight(0.75);
        sJSP.setDividerLocation(0.75);
    }
}