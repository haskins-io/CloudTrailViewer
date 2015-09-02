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
import com.haskins.cloudtrailviewer.requestInfo.Ec2Resource;
import com.haskins.cloudtrailviewer.requestInfo.ElbResoure;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Arrays;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mark.haskins
 */
public class ResourceDetailDialog extends JDialog {
    
    private static boolean exceptionThrown = false;
    
    private static ResourceDetailDialog dialog;
    
    public static List<String> handledResourceTypes = Arrays.asList(
            ElbResoure.ELB_NAME,
            AsResource.AUTO_SCALING_GROUP,
            Ec2Resource.EC2_INSTANCE
    );
    
    public static void showDialog(Component parent, ResourceDetailRequest detailRequest) {
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
     
        super(frame, "Resource Details", true);
        
        this.setMinimumSize(new Dimension(800,600));
        this.setMaximumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(800,600));
        
        ResourceDetail detail;
        if (detailRequest.getResourceType().equalsIgnoreCase(Ec2Resource.EC2_INSTANCE)) {
            detail = new EC2Detail();
        } else {
            detail = new UnhandledDetail();
        }
        
        Container contentPane = getContentPane();
        String response = detail.retrieveDetails(detailRequest);
        
        if (response == null) {
            contentPane.add(detail.getPanel());
        } else {
                JOptionPane.showMessageDialog(CloudTrailViewer.frame,
                response,
                "AWS Error",
                JOptionPane.ERROR_MESSAGE
            );
                
            exceptionThrown = true;
        }
        
        pack();
        setLocationRelativeTo(frame); 
    }
}
