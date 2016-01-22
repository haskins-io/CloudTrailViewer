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
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class RdsInstanceDetail extends AbstractDetail {

    private static final long serialVersionUID = 7198083071987548396L;

    public RdsInstanceDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonRDS client = new AmazonRDSClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
            request.setDBInstanceIdentifier(detailRequest.getResourceName());
            
            DescribeDBInstancesResult result = client.describeDBInstances(request);
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
    private void buildUI(DescribeDBInstancesResult detail) {
        
        if (!detail.getDBInstances().isEmpty()) {
            
            List<DBInstance> instances = detail.getDBInstances();
            DBInstance instance = instances.get(0);
            
        }
        
    }
}
