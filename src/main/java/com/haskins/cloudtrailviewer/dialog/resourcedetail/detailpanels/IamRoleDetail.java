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
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class IamRoleDetail extends AbstractDetail {

    protected final DefaultTableModel primaryTableModel = new DefaultTableModel();
    
    public IamRoleDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonIdentityManagement client = new AmazonIdentityManagementClient(credentials);
            
            buildUI(null); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
        }

        return response;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }
    
    private void buildUI(DescribeInstancesResult detail) {
        
    }
    
}
