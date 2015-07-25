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

package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author mark
 */
public class EventTree extends JPanel implements SideBar {

    public static final String NAME = "EventTree";
    
    private final JTree tree;
        
    public EventTree() {
        
        this.setLayout(new BorderLayout());
                
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Event");
        tree = new JTree(root);
        
        tree.setShowsRootHandles(true);
        JScrollPane treeView = new JScrollPane(tree);
        
        this.add(treeView, BorderLayout.CENTER);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return EventTree.NAME;
    }

    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Tree-32.png";
    }

    @Override
    public String getTooltip() {
        return "Event Tree View";
    }

    @Override
    public void setCurrentEvent(Event event) {
        
        DefaultTreeModel model = new DefaultTreeModel(event.populateTree());
        tree.setModel( model );
    }
}
