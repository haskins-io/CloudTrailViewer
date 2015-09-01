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
package com.haskins.cloudtrailviewer.dialog.resourcedetail;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class EC2Detail extends JPanel implements ResourceDetail {

    protected final DefaultTableModel primaryTableModel = new DefaultTableModel();
    protected final DefaultTableModel tagsTableModel = new DefaultTableModel();
    
    @Override
    public boolean retrieveDetails(AwsAccount awsAccount, String resourceName) {
        
        boolean sucessfullyRetrievedData;
        
        try {
            
            AWSCredentials credentials= new BasicAWSCredentials(
                awsAccount.getKey(),
                awsAccount.getSecret()
            );

            AmazonEC2 ec2Client = new AmazonEC2Client(credentials);

            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.setInstanceIds(Arrays.asList(resourceName));

            DescribeInstancesResult result = ec2Client.describeInstances(request);
            buildUI(result); 
            
            sucessfullyRetrievedData = true;
            
        } catch (Exception e) {
            sucessfullyRetrievedData = false;
        }

        return sucessfullyRetrievedData;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }

    private void buildUI(DescribeInstancesResult detail) {
        
        this.setLayout(new BorderLayout());
       
        JTabbedPane tabs = new JTabbedPane();
        this.add(tabs, BorderLayout.CENTER);
        
        final JTable primaryTable = new JTable(primaryTableModel);
        tabs.add("Instance", primaryTable);
        
        final JTable tagsTable = new JTable(tagsTableModel);
        tabs.add("Tags", tagsTable);
        
        List<Reservation> reservations = detail.getReservations();
        if (!reservations.isEmpty()) {
            
            Reservation reservation = reservations.get(0);
            List<Instance> instances = reservation.getInstances();
            if (!instances.isEmpty()) {
                
                Instance instance = instances.get(0);
                
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"AMI Launch Index", instance.getAmiLaunchIndex()}); }
                if (instance.getArchitecture() != null) { primaryTableModel.addRow(new Object[]{"Architecture", instance.getArchitecture()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Block Mapping Device", ""}); }
                if (instance.getClientToken() != null) { primaryTableModel.addRow(new Object[]{"Client Token", instance.getClientToken()}); }
                primaryTableModel.addRow(new Object[]{"EBS Optimised", instance.isEbsOptimized()});
                if (instance.getHypervisor() != null) { primaryTableModel.addRow(new Object[]{"Hypervisor", instance.getHypervisor()}); }
                if (instance.getIamInstanceProfile() != null) { primaryTableModel.addRow(new Object[]{"Instance Profile", instance.getIamInstanceProfile().toString()}); }
                if (instance.getImageId() != null) { primaryTableModel.addRow(new Object[]{"Image Id", instance.getImageId()}); }
                if (instance.getInstanceId() != null) { primaryTableModel.addRow(new Object[]{"Instance Id", instance.getInstanceId()}); }
                if (instance.getInstanceLifecycle() != null) { primaryTableModel.addRow(new Object[]{"Instance Lifecyle", instance.getInstanceLifecycle()}); }
                if (instance.getInstanceType() != null) { primaryTableModel.addRow(new Object[]{"Instance Type", instance.getInstanceType()}); }
                if (instance.getKernelId() != null) { primaryTableModel.addRow(new Object[]{"Kernel Id", instance.getKernelId()}); }
                if (instance.getKeyName() != null) { primaryTableModel.addRow(new Object[]{"Key Name", instance.getKeyName()}); }
                if (instance.getLaunchTime() != null) { primaryTableModel.addRow(new Object[]{"Launch Time", instance.getLaunchTime()}); }
                if (instance.getMonitoring() != null) { primaryTableModel.addRow(new Object[]{"Monitoring", instance.getMonitoring().toString()}); }
                if (instance.getPlacement() != null) { primaryTableModel.addRow(new Object[]{"Placement", instance.getPlacement().getAvailabilityZone()}); }
                if (instance.getPlatform() != null) { primaryTableModel.addRow(new Object[]{"Platform", instance.getPlatform()}); }
                if (instance.getPrivateDnsName() != null) { primaryTableModel.addRow(new Object[]{"Private DNS Name", instance.getPrivateDnsName()}); }
                if (instance.getPrivateIpAddress() != null) { primaryTableModel.addRow(new Object[]{"Private IP Address", instance.getPrivateIpAddress()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Product Codes", ""}); }
                if (instance.getPublicDnsName() != null) { primaryTableModel.addRow(new Object[]{"Public DNS Name", instance.getPublicDnsName()}); }
                if (instance.getPublicIpAddress() != null) { primaryTableModel.addRow(new Object[]{"Public IP Address", instance.getPublicIpAddress()}); }
                if (instance.getRamdiskId() != null) { primaryTableModel.addRow(new Object[]{"Ram Disk Id", instance.getRamdiskId()}); }
                if (instance.getRootDeviceName() != null) { primaryTableModel.addRow(new Object[]{"Root Device Name", instance.getRootDeviceName()}); }
                if (instance.getRootDeviceType() != null) { primaryTableModel.addRow(new Object[]{"Root Device Type", instance.getRootDeviceType()}); }
                if (instance.getAmiLaunchIndex() != null) { primaryTableModel.addRow(new Object[]{"Security Groups", ""}); }
                if (instance.getSourceDestCheck() != null) { primaryTableModel.addRow(new Object[]{"Source Destination Check", instance.getSourceDestCheck()}); }
                if (instance.getSpotInstanceRequestId() != null) { primaryTableModel.addRow(new Object[]{"Spot Instance Request Id", instance.getSpotInstanceRequestId()}); }
                if (instance.getSriovNetSupport() != null) { primaryTableModel.addRow(new Object[]{"Sriov network Support", instance.getSriovNetSupport()}); }
                if (instance.getState() != null) { primaryTableModel.addRow(new Object[]{"State", instance.getState().getName()}); }
                if (instance.getStateReason() != null) { primaryTableModel.addRow(new Object[]{"State Reason", instance.getStateReason().getMessage()}); }
                if (instance.getSubnetId() != null) { primaryTableModel.addRow(new Object[]{"Subnet Id", instance.getSubnetId()}); }
                if (instance.getVirtualizationType() != null) { primaryTableModel.addRow(new Object[]{"Virtualisation Type", instance.getVirtualizationType()}); }
                if (instance.getVpcId() != null) { primaryTableModel.addRow(new Object[]{"Vpc Id", instance.getVpcId()}); }
                
                List<Tag> tags = instance.getTags();
                for (Tag tag : tags) {
                    tagsTableModel.addRow(new Object[]{tag.getKey(), tag.getValue()});
                }
            }
        }
    }
}
