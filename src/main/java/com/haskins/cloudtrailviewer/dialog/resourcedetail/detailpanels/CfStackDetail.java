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
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.Output;
import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.cloudformation.model.Tag;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class CfStackDetail extends AbstractDetail {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final long serialVersionUID = -2152867004948703721L;
 
    protected final DefaultTableModel outputTableModel = new DefaultTableModel();
    protected final DefaultTableModel parametersTableModel = new DefaultTableModel();
    
    public CfStackDetail(ResourceDetailRequest detailRequest) {
        super(detailRequest);
    }
    
    @Override
    public String retrieveDetails(ResourceDetailRequest detailRequest) {
       
        String response = null;
        
        try {
            
            AmazonCloudFormation client = new AmazonCloudFormationClient(credentials);
            client.setRegion(Region.getRegion(Regions.fromName(detailRequest.getRegion())));
            
            DescribeStacksRequest request = new DescribeStacksRequest();
            request.setStackName(detailRequest.getResourceName());
            
            DescribeStacksResult result = client.describeStacks(request);
            buildUI(result); 
            
        } catch (IllegalArgumentException | AmazonClientException e) {
            response = e.getMessage();
            LOGGER.log(Level.WARNING, "Problem retrieving CloudFormation details from AWS", e);
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
    private void buildUI(DescribeStacksResult detail) {
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Stack", primaryScrollPane);
        
        final JTable outputCheckTable = new JTable(outputTableModel);
        JScrollPane healthCheckScrollPane = new JScrollPane(outputCheckTable);
        tabs.add("Output", healthCheckScrollPane);
        
        final JTable paramsTable = new JTable(parametersTableModel);
        JScrollPane listenersScrollPane = new JScrollPane(paramsTable);
        tabs.add("Parameters", listenersScrollPane);
        
        this.add(tabs, BorderLayout.CENTER);
        
        if (!detail.getStacks().isEmpty()) {
            
            List<Stack> stacks = detail.getStacks();
            Stack stack = stacks.get(0);
            
            if (stack.getCreationTime() != null) { primaryTableModel.addRow(new Object[]{"Created", getDateString(stack.getCreationTime())}); }
            if (stack.getDescription() != null) { primaryTableModel.addRow(new Object[]{"Description", stack.getDescription()}); }
            if (stack.getDisableRollback() != null) { primaryTableModel.addRow(new Object[]{"Disable Rollback", stack.getDisableRollback()}); }
            if (stack.getLastUpdatedTime() != null) { primaryTableModel.addRow(new Object[]{"Last Updated", getDateString(stack.getLastUpdatedTime())}); }
            if (stack.getNotificationARNs() != null) { primaryTableModel.addRow(new Object[]{"Notification Arns", stack.getNotificationARNs()}); }
            if (stack.getStackId() != null) { primaryTableModel.addRow(new Object[]{"Stacks Id", stack.getStackId()}); }
            if (stack.getStackName()!= null) { primaryTableModel.addRow(new Object[]{"Stacks Name", stack.getStackName()}); }
            if (stack.getStackStatus()!= null) { primaryTableModel.addRow(new Object[]{"Stacks Status", stack.getStackStatus()}); }
            if (stack.getStackStatusReason()!= null) { primaryTableModel.addRow(new Object[]{"Stacks Status Reason", stack.getStackStatusReason()}); }
            if (stack.getTimeoutInMinutes()!= null) { primaryTableModel.addRow(new Object[]{"Timeout (minutes)", stack.getTimeoutInMinutes()}); }
            
            /**
             * Tags
             */
            List<Tag> tags = stack.getTags();
            for (Tag tag : tags) {
                tagsTableModel.addRow(new Object[]{tag.getKey(), tag.getValue()});
            }
            
            /**
             * Output
             */
            outputTableModel.addColumn("Description");
            outputTableModel.addColumn("Key");
            outputTableModel.addColumn("Value");
            
            List<Output> outputs = stack.getOutputs();
            for (Output output : outputs) {
                tagsTableModel.addRow(new Object[]{output.getDescription(), output.getOutputKey(), output.getOutputValue()});
            }
            
            /**
             * Parameters
             */
            parametersTableModel.addColumn("Key");
            parametersTableModel.addColumn("Value");
            parametersTableModel.addColumn("User Previous Value");
            
            List<Parameter> parameters = stack.getParameters();
            for (Parameter parameter : parameters) {
                tagsTableModel.addRow(new Object[]{parameter.getParameterKey(), parameter.getParameterValue(), parameter.getUsePreviousValue()});
            }
        }
    }
    
}
