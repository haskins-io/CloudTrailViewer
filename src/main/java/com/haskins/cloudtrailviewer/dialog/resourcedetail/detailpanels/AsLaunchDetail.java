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
import com.amazonaws.services.autoscaling.AmazonAutoScaling;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsRequest;
import com.amazonaws.services.autoscaling.model.DescribeLaunchConfigurationsResult;
import com.amazonaws.services.autoscaling.model.LaunchConfiguration;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class AsLaunchDetail extends AbstractDetail {

    public AsLaunchDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {
            
            AmazonAutoScaling asClient = new AmazonAutoScalingClient(credentials);
            asClient.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeLaunchConfigurationsRequest request = new DescribeLaunchConfigurationsRequest();
            request.setLaunchConfigurationNames(Arrays.asList(detailRequest.getResourceName()));
            
            DescribeLaunchConfigurationsResult result = asClient.describeLaunchConfigurations(request);
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
    private void buildUI(DescribeLaunchConfigurationsResult detail) {
        
        this.add(primaryScrollPane, BorderLayout.CENTER);
        
        if (!detail.getLaunchConfigurations().isEmpty()) {
            
            List<LaunchConfiguration> lcs = detail.getLaunchConfigurations();
            LaunchConfiguration lc = lcs.get(0);
            
            if (lc.getAssociatePublicIpAddress() != null) { primaryTableModel.addRow(new Object[]{"Has EIP", lc.getAssociatePublicIpAddress()}); }
            if (lc.getClassicLinkVPCId() != null) { primaryTableModel.addRow(new Object[]{"Classic Link VPC Id", lc.getClassicLinkVPCId()}); }
            
            if (!lc.getClassicLinkVPCSecurityGroups().isEmpty()) { 
            
                StringBuilder sgs = new StringBuilder();
                for (String sg : lc.getClassicLinkVPCSecurityGroups()) {
                    sgs.append(sg).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Classic Link VPC Security Groups", sgs.toString()}); 
            }
            
            if (lc.getCreatedTime()!= null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(lc.getCreatedTime())}); }
            if (lc.getEbsOptimized() != null) { primaryTableModel.addRow(new Object[]{"EBS Optimised", lc.getEbsOptimized()}); }
            if (lc.getIamInstanceProfile() != null) { primaryTableModel.addRow(new Object[]{"Instance Profile", lc.getIamInstanceProfile()}); }
            if (lc.getImageId() != null) { primaryTableModel.addRow(new Object[]{"Image Id", lc.getImageId()}); }
            if (lc.getInstanceType() != null) { primaryTableModel.addRow(new Object[]{"Instance Type", lc.getInstanceType()}); }
            if (lc.getKernelId() != null) { primaryTableModel.addRow(new Object[]{"Kernal Id", lc.getKernelId()}); }
            if (lc.getKeyName() != null) { primaryTableModel.addRow(new Object[]{"Key Name", lc.getKeyName()}); }
            if (lc.getLaunchConfigurationARN() != null) { primaryTableModel.addRow(new Object[]{"Launch Configuration Arn", lc.getLaunchConfigurationARN()}); }
            if (lc.getLaunchConfigurationName() != null) { primaryTableModel.addRow(new Object[]{"Launch Configuration Name", lc.getLaunchConfigurationName()}); }
            if (lc.getPlacementTenancy() != null) { primaryTableModel.addRow(new Object[]{"Placement Tenancy", lc.getPlacementTenancy()}); }
            if (lc.getRamdiskId() != null) { primaryTableModel.addRow(new Object[]{"Ram Disk ID", lc.getRamdiskId()}); }
            
            if (!lc.getSecurityGroups().isEmpty()) { 
            
                StringBuilder sgs = new StringBuilder();
                for (String sg : lc.getSecurityGroups()) {
                    sgs.append(sg).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Security Groups", sgs.toString()}); 
            }
            
            if (lc.getSpotPrice() != null) { primaryTableModel.addRow(new Object[]{"Spot Price", lc.getSpotPrice()}); }
        }
        
    }
}
