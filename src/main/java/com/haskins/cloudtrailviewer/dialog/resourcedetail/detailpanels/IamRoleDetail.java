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
import com.amazonaws.services.identitymanagement.model.GetRoleRequest;
import com.amazonaws.services.identitymanagement.model.GetRoleResult;
import com.amazonaws.services.identitymanagement.model.Role;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class IamRoleDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = 4523406339689209595L;

    public IamRoleDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonIdentityManagement client = new AmazonIdentityManagementClient(credentials);
            
            GetRoleRequest request = new GetRoleRequest();
            request.setRoleName(detailRequest.getResourceName());
            
            GetRoleResult result = client.getRole(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving IAM Role details from AWS", e);
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
    private void buildUI(GetRoleResult detail) {
        
        this.add(primaryScrollPane, BorderLayout.CENTER);
        
        if (detail.getRole() != null) {
            
            Role role = detail.getRole();
            
            if (role.getCreateDate() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(role.getCreateDate())}); }
            if (role.getArn() != null) { primaryTableModel.addRow(new Object[]{"Arn", role.getArn()}); }
            if (role.getAssumeRolePolicyDocument() != null) { primaryTableModel.addRow(new Object[]{"Assume Role Policy Document", role.getAssumeRolePolicyDocument()}); }
            if (role.getPath()!= null) { primaryTableModel.addRow(new Object[]{"Path", role.getPath()}); }
            if (role.getRoleId()!= null) { primaryTableModel.addRow(new Object[]{"Role Id", role.getRoleId()}); }
            if (role.getRoleName()!= null) { primaryTableModel.addRow(new Object[]{"Role Name", role.getRoleName()}); }
            
        }
        
    }
    
}
