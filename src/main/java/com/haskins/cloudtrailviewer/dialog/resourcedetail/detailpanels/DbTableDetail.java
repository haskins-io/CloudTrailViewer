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
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class DbTableDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = 6199277908000843310L;
    
    public DbTableDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonDynamoDB client = new AmazonDynamoDBClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeTableRequest request = new DescribeTableRequest();
            request.setTableName(detailRequest.getResourceName());
            
            DescribeTableResult result = client.describeTable(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving DynamoDb details from AWS", e);
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
    private void buildUI(DescribeTableResult detail) {
        
        this.add(primaryScrollPane, BorderLayout.CENTER);
        
        if (detail.getTable() != null) {
            
            TableDescription table = detail.getTable();
            
            if (table.getCreationDateTime() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(table.getCreationDateTime())}); }
            if (table.getItemCount()!= null) { primaryTableModel.addRow(new Object[]{"Item Count", table.getItemCount()}); }
            if (table.getLatestStreamArn()!= null) { primaryTableModel.addRow(new Object[]{"Latest Stream Arn", table.getLatestStreamArn()}); }
            if (table.getLatestStreamLabel()!= null) { primaryTableModel.addRow(new Object[]{"Latest Stream Label", table.getLatestStreamLabel()}); }
            if (table.getTableArn()!= null) { primaryTableModel.addRow(new Object[]{"Arn", table.getTableArn()}); }
            if (table.getTableName()!= null) { primaryTableModel.addRow(new Object[]{"Name", table.getTableName()}); }
            if (table.getTableSizeBytes()!= null) { primaryTableModel.addRow(new Object[]{"Size (bytes)", table.getTableSizeBytes()}); }
            if (table.getTableStatus()!= null) { primaryTableModel.addRow(new Object[]{"Status", table.getTableStatus()}); }
        }
    }
}
