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
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.DescribeStreamRequest;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.StreamDescription;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class KinesisStreamDetail extends AbstractDetail {

    private static final long serialVersionUID = -3914862385729636742L;

    public KinesisStreamDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {

            AmazonKinesis client = new AmazonKinesisClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeStreamRequest request = new DescribeStreamRequest();
            request.setStreamName(detailRequest.getResourceName());
            
            DescribeStreamResult result = client.describeStream(request);
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
    private void buildUI(DescribeStreamResult detail) {
        
        this.add(primaryScrollPane, BorderLayout.CENTER);
        
        if (detail.getStreamDescription() != null) {
            
            StreamDescription stream = detail.getStreamDescription();
            
            if (stream.getHasMoreShards() != null) { primaryTableModel.addRow(new Object[]{"Has More Shards", stream.getHasMoreShards()}); }
            if (stream.getStreamARN() != null) { primaryTableModel.addRow(new Object[]{"Stream Arn", stream.getStreamARN()}); }
            if (stream.getStreamName() != null) { primaryTableModel.addRow(new Object[]{"Stream Name", stream.getStreamName()}); }
            if (stream.getStreamStatus() != null) { primaryTableModel.addRow(new Object[]{"Stram Status", stream.getStreamStatus()}); }
        }
        
    }
    
}
