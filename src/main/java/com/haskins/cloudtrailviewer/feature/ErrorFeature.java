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

import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.NameValueModel;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.CountComparator;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mark.haskins
 */
public class ErrorFeature extends JPanel implements Feature, EventDatabaseListener {
        
    public static final String NAME = "Error Feature";
    
    private final Map<String, NameValueModel> errorsMap = new HashMap<>();
    private final DefaultListModel<NameValueModel> errorListModel = new DefaultListModel<>();    
    private final EventTablePanel eventTable = new EventTablePanel();
        
    private final StatusBar statusBar;
    
    public ErrorFeature(StatusBar sb) {
        
        this.statusBar = sb;
        
        buildUI();
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { 
        GeneralUtils.orderListByComparator(errorListModel, new CountComparator());
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Error-48.png";
    }

    @Override
    public String getTooltip() {
        return "Error Overview";
    }
    
    @Override
    public String getName() {
        return ErrorFeature.NAME;
    }
    
    @Override
    public void will_hide() { }
    
    @Override
    public void will_appear() { }
    
    @Override
    public void showEventsTable(List<Event> events) {
                
        statusBar.setVisibleEvents(events.size());
        eventTable.clearEvents();
        eventTable.setEvents(events);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                 
        String errorName = event.getErrorCode();
        if (errorName.trim().length() > 0) {
            
            if (!errorsMap.containsKey(errorName)) {

                NameValueModel errorModel = new NameValueModel(errorName, event);
                errorsMap.put(errorName, errorModel);
                errorListModel.addElement(errorModel);
            } else {
                
                NameValueModel errorModel = errorsMap.get(errorName);
                errorModel.addEvent(event);
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        final JList errorList = new JList(errorListModel);
        errorList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                NameValueModel model = (NameValueModel)errorList.getSelectedValue();
                eventTable.clearEvents();
                eventTable.setEvents(model.getEvents());
            }
        });
        
        errorList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        errorList.setLayoutOrientation(JList.VERTICAL);
        errorList.setVisibleRowCount(-1);
        errorList.setCellRenderer(new DefaultListCellRenderer() {
            
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                                
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                NameValueModel model = (NameValueModel)value;
                label.setText(model.getName() + " : " + model.getNumberOfEvents());
                
                return label;
            }
        });

        JScrollPane sPane = new JScrollPane(errorList);
        sPane.setMinimumSize(new Dimension(300, 400));
        sPane.setPreferredSize(new Dimension(300, 400));
        sPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        eventTable.setVisible(true);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0)); 
        this.setLayout(new BorderLayout());
        this.add(sPane, BorderLayout.WEST);
        this.add(eventTable, BorderLayout.CENTER);
    }
}