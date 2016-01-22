/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.dialog.resourcedetail.detailpanels;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.identitymanagement.model.GetGroupRequest;
import com.amazonaws.services.identitymanagement.model.GetGroupResult;
import com.amazonaws.services.identitymanagement.model.Group;
import com.amazonaws.services.identitymanagement.model.User;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class IamGroupDetail extends AbstractDetail {

    private static final long serialVersionUID = 2177710650024418654L;

    protected final DefaultTableModel usersTableModel = new DefaultTableModel();
    
    public IamGroupDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonIdentityManagement client = new AmazonIdentityManagementClient(credentials);
            
            GetGroupRequest request = new GetGroupRequest();
            request.setGroupName(detailRequest.getResourceName());
            
            GetGroupResult result = client.getGroup(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
        }

        return response;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI(GetGroupResult detail) {
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Group", primaryScrollPane);
        
        final JTable usersTable = new JTable(usersTableModel);
        JScrollPane usersScrollPane = new JScrollPane(usersTable);
        tabs.add("Users", usersScrollPane);
        
        this.add(tabs, BorderLayout.CENTER);
        
        if (detail.getGroup() != null) {
            
            Group group = detail.getGroup();
            
            if (group.getCreateDate() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(group.getCreateDate())}); }
            if (group.getArn() != null) { primaryTableModel.addRow(new Object[]{"Arn", group.getArn()}); }
            if (group.getGroupId() != null) { primaryTableModel.addRow(new Object[]{"Group ID", group.getGroupId()}); }
            if (group.getGroupName()!= null) { primaryTableModel.addRow(new Object[]{"Group Name", group.getGroupName()}); }
            if (group.getPath()!= null) { primaryTableModel.addRow(new Object[]{"Path", group.getPath()}); }
            
            /**
             * Users
             * 
             */
            usersTableModel.addColumn("Key");
            usersTableModel.addColumn("Value");
            usersTableModel.addColumn("User Previous Value");
            
            List<User> users = detail.getUsers();
            if (!users.isEmpty()) {
                for (User user : users) {
                    
                    if (user.getCreateDate() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(user.getCreateDate())}); }
                    if (user.getArn() != null) { primaryTableModel.addRow(new Object[]{"Arn", user.getArn()}); }
                    if (user.getPasswordLastUsed() != null) { primaryTableModel.addRow(new Object[]{"Password Last Used", user.getPasswordLastUsed()}); }
                    if (user.getPath()!= null) { primaryTableModel.addRow(new Object[]{"Path", user.getPath()}); }
                    if (user.getUserId()!= null) { primaryTableModel.addRow(new Object[]{"User Id", user.getUserId()}); }
                    if (user.getUserName()!= null) { primaryTableModel.addRow(new Object[]{"User Name", user.getUserName()}); }
                    
                }
            }
        }
    }
    
}
