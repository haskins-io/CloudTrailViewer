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
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult;
import com.amazonaws.services.autoscaling.model.Instance;
import com.amazonaws.services.autoscaling.model.TagDescription;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author mark.haskins
 */
public class AsGroupDetail extends AbstractDetail {

    public AsGroupDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {
            
            AmazonAutoScaling asClient = new AmazonAutoScalingClient(credentials);
            asClient.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));

            DescribeAutoScalingGroupsRequest request = new DescribeAutoScalingGroupsRequest();
            request.setAutoScalingGroupNames(Arrays.asList(detailRequest.getResourceName()));
            
            DescribeAutoScalingGroupsResult result = asClient.describeAutoScalingGroups(request);
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
    
    private void buildUI(DescribeAutoScalingGroupsResult detail) {
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("AS Group", primaryScrollPane);
        tabs.add("Tags", tagsScrollPane);
        
        this.setLayout(new BorderLayout());
        this.add(tabs, BorderLayout.CENTER);
        
        List<AutoScalingGroup> groups = detail.getAutoScalingGroups();
        if (!groups.isEmpty()) {
            AutoScalingGroup group = groups.get(0);
            
            if (group.getAutoScalingGroupARN() != null) { primaryTableModel.addRow(new Object[]{"AutoScaling Group Arn", group.getAutoScalingGroupARN()}); }
            if (group.getAutoScalingGroupName()!= null) { primaryTableModel.addRow(new Object[]{"AutoScaling Group Name", group.getAutoScalingGroupName()}); }
            
            if (!group.getAvailabilityZones().isEmpty()) { 
            
                StringBuilder azs = new StringBuilder();
                for (String az : group.getAvailabilityZones()) {
                    azs.append(az).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Availability Zones", azs.toString()}); 
            }
            
            if (group.getCreatedTime()!= null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(group.getCreatedTime())}); }
            if (group.getDefaultCooldown()!= null) { primaryTableModel.addRow(new Object[]{"Default Cooldown", group.getDefaultCooldown()}); }
            if (group.getDesiredCapacity()!= null) { primaryTableModel.addRow(new Object[]{"Desired Capacity", group.getDesiredCapacity()}); }
            if (group.getHealthCheckGracePeriod()!= null) { primaryTableModel.addRow(new Object[]{"HealthCheck Grace Period", group.getHealthCheckGracePeriod()}); }
            if (group.getHealthCheckType()!= null) { primaryTableModel.addRow(new Object[]{"HealthCheck Type", group.getHealthCheckType()}); }
            
            if (!group.getInstances().isEmpty()) { 
            
                StringBuilder instances = new StringBuilder();
                for (Instance instance : group.getInstances()) {
                    instances.append(instance.getInstanceId()).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"Instances", instances.toString()}); 
            }
            
            if (group.getLaunchConfigurationName()!= null) { primaryTableModel.addRow(new Object[]{"Launch Configuration Name", group.getLaunchConfigurationName()}); }
            
            if (!group.getLoadBalancerNames().isEmpty()) { 
            
                StringBuilder instances = new StringBuilder();
                for (String instance : group.getLoadBalancerNames()) {
                    instances.append(instance).append(", ");
                }
                
                primaryTableModel.addRow(new Object[]{"LoadBalancer names", instances.toString()}); 
            }
            
            if (group.getMaxSize()!= null) { primaryTableModel.addRow(new Object[]{"Max Size", group.getMaxSize()}); }
            if (group.getMinSize()!= null) { primaryTableModel.addRow(new Object[]{"Min Size", group.getMinSize()}); }
            if (group.getPlacementGroup()!= null) { primaryTableModel.addRow(new Object[]{"Placement Group", group.getPlacementGroup()}); }
            if (group.getVPCZoneIdentifier()!= null) { primaryTableModel.addRow(new Object[]{"VPC Zone Identifier", group.getVPCZoneIdentifier()}); }
            
                
            List<TagDescription> tags = group.getTags();
            for (TagDescription tag : tags) {
                tagsTableModel.addRow(new Object[]{tag.getKey(), tag.getValue()});
            }
        }    
    }
    
}
