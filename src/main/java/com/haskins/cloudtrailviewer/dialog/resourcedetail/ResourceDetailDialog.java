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

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.requestInfo.AsResource;
import com.haskins.cloudtrailviewer.requestInfo.CfResource;
import com.haskins.cloudtrailviewer.requestInfo.CsResource;
import com.haskins.cloudtrailviewer.requestInfo.DbResource;
import com.haskins.cloudtrailviewer.requestInfo.Ec2Resource;
import com.haskins.cloudtrailviewer.requestInfo.ElbResoure;
import com.haskins.cloudtrailviewer.requestInfo.IamResource;
import com.haskins.cloudtrailviewer.requestInfo.KinesisResource;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mark.haskins
 */
public class ResourceDetailDialog extends JDialog {
    
    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final String PACKAGE = "com.haskins.cloudtrailviewer.dialog.resourcedetail.detailpanels.";
    
    private static boolean exceptionThrown = false;
    
    private static ResourceDetailDialog dialog;
    private static final long serialVersionUID = 5453485769149911186L;
    
    public static Map<String, String> handledResourceTypes() {
        
        return Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put(AsResource.AUTO_SCALING_GROUP, "AsGroupDetail");
                put(AsResource.LAUNCH_CONFIGURATION, "AsLaunchDetail");
                put(CfResource.CLOUDFORMATION_STACK, "CfStackDetail");
                put(CsResource.CLOUDSEARCH_DOMAIN, "CsDomainDetail");
                put(DbResource.DYNAMODB_TABLE, "DbTableDetail");
                put(Ec2Resource.EC2_INSTANCE, "EC2InstanceDetail");
                put(ElbResoure.ELB_NAME, "ElbDetail");
                put(IamResource.IAM_GROUP, "IamGroupDetail");
                put(IamResource.IAM_ROLE, "IamRoleDetail");
                put(IamResource.IAM_USER, "IamUserDetail");
                put(KinesisResource.STREAM, "KinesisStreamDetail");
            }
        });
    }
    
    public static void showDialog(Component parent, ResourceDetailRequest detailRequest) {
        
        exceptionThrown = false;
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new ResourceDetailDialog(frame, detailRequest);
        
        if(!exceptionThrown) {
            dialog.setVisible(true);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private ResourceDetailDialog(Frame frame, ResourceDetailRequest detailRequest) {
     
        super(frame, "Resource Details - " + detailRequest.getResourceName(), true);
        
        this.setMinimumSize(new Dimension(800,600));
        this.setMaximumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(800,600));
        
        String fqcp = PACKAGE + handledResourceTypes().get(detailRequest.getResourceType());
        
        try {
            Class c = Class.forName(fqcp);
            Constructor constructor = c.getConstructor(ResourceDetailRequest.class);
            ResourceDetail detail = (ResourceDetail) constructor.newInstance(detailRequest);
            
            Container contentPane = getContentPane();
            String response = detail.retrieveDetails(detailRequest);

            if (response == null) {
                contentPane.add(detail.getPanel());
                
            } else {
                response = response.replaceAll("; ", "\n");

                JOptionPane.showMessageDialog(CloudTrailViewer.frame,
                    response,
                    "AWS Error",
                    JOptionPane.ERROR_MESSAGE
                );

                exceptionThrown = true;
            }

            pack();
            setLocationRelativeTo(frame); 
            
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.log(Level.WARNING, "Exception showing Dialog", ex);
            exceptionThrown = true;
        }
    }
}
