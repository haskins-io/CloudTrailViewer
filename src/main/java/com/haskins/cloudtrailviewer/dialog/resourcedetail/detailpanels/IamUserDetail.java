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
import com.amazonaws.services.identitymanagement.model.GetUserRequest;
import com.amazonaws.services.identitymanagement.model.GetUserResult;
import com.amazonaws.services.identitymanagement.model.User;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class IamUserDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = 1510425799781716098L;

    public IamUserDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonIdentityManagement client = new AmazonIdentityManagementClient(credentials);
            
            GetUserRequest request = new GetUserRequest();
            request.setUserName(detailRequest.getResourceName());
            
            GetUserResult result = client.getUser(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving IAM User details from AWS", e);
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
    private void buildUI(GetUserResult detail) {
        
        this.add(primaryScrollPane, BorderLayout.CENTER);
        
        if (detail.getUser() != null) {
            
            User user = detail.getUser();
                
            if (user.getCreateDate() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(user.getCreateDate())}); }
            if (user.getArn() != null) { primaryTableModel.addRow(new Object[]{"Arn", user.getArn()}); }
            if (user.getPasswordLastUsed() != null) { primaryTableModel.addRow(new Object[]{"Password Last Used", user.getPasswordLastUsed()}); }
            if (user.getPath()!= null) { primaryTableModel.addRow(new Object[]{"Path", user.getPath()}); }
            if (user.getUserId()!= null) { primaryTableModel.addRow(new Object[]{"User Id", user.getUserId()}); }
            if (user.getUserName()!= null) { primaryTableModel.addRow(new Object[]{"User Name", user.getUserName()}); }
            
        }
        
    }
    
}
