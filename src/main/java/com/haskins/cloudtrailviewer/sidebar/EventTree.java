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

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.core.DbManager;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailDialog;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author mark
 */
public class EventTree extends JPanel implements SideBar {

    public static final String NAME = "EventTree";
    
    private Event event;
    private final JTree tree;
        
    public EventTree() {
        
        this.setLayout(new BorderLayout());
                
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Event");
        tree = new JTree(root);
        MouseListener ml = new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                
                int selRow = tree.getRowForLocation(e.getX(), e.getY());        
                if(selRow != -1 && e.getClickCount() == 2) {
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    handleDoubleClick(selPath);
                }
            }
        };
        tree.addMouseListener(ml);
        
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
        
        this.event = event;
        
        DefaultTreeModel model = new DefaultTreeModel(this.event.populateTree());
        tree.setModel(model);
    }
    
    private void handleDoubleClick(TreePath selPath) {
        
        if (selPath.getPathCount() >= 3) {
            int numNodes = selPath.getPathCount();
            TreeNode typeNode = (TreeNode)selPath.getPath()[numNodes-2];
            String resourceType = typeNode.toString();
            
            if (ResourceDetailDialog.handledResourceTypes.contains(resourceType)) {
                TreeNode nameNode = (TreeNode)selPath.getPath()[numNodes-1];
                String resourceName = nameNode.toString();
                
                AwsAccount account = null;
                String query = "SELECT * FROM aws_credentials WHERE aws_acct = " + event.getRecipientAccountId();
                List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
                for (ResultSetRow row : rows) {

                    account = new AwsAccount(
                            (Integer) row.get("id"),
                            (String) row.get("aws_name"),
                            (String) row.get("aws_acct_num"),
                            (String) row.get("aws_bucket"),
                            (String) row.get("aws_key"),
                            (String) row.get("aws_secret"),
                            (String) row.get("aws_prefix")
                    );
                }
                
                if (account != null) {
                    ResourceDetailDialog.showDialog(CloudTrailViewer.frame, resourceType, resourceName, account);
                    
                } else {
                    
                    JOptionPane.showMessageDialog(CloudTrailViewer.frame,
                        "The account has not been defined in the AWS Account section of the properties. To get more information about this resource add the account information.",
                        "Unknown AWS Account : " + event.getRecipientAccountId(),
                        JOptionPane.ERROR_MESSAGE);
                    }
            }
        }
    }
}
