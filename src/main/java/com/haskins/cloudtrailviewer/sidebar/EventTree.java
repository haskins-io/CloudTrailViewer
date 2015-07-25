/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
