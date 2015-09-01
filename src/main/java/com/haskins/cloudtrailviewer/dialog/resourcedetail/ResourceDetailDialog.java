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

import com.haskins.cloudtrailviewer.model.AwsAccount;
import java.awt.BorderLayout;
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
    
    private static ResourceDetailDialog dialog;
    
    public static List<String> handledResourceTypes = Arrays.asList(
            "Elastic LoadBalancer",
            "AutoScaling Group",
            "EC2 Instance"
    );
    
    public static void showDialog(Component parent, String resourceType, String resourceName, AwsAccount awsAccount) {
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new ResourceDetailDialog(frame, resourceType, resourceName, awsAccount);
        dialog.setVisible(true);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private ResourceDetailDialog(Frame frame, String resourceType, String resourceName, AwsAccount awsAccount) {
     
        super(frame, "Resource Details", true);
        
        this.setMinimumSize(new Dimension(800,600));
        this.setMaximumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(800,600));
        
        ResourceDetail detail;
        if (resourceType.equalsIgnoreCase("EC2 Instance")) {
            detail = new EC2Detail();
        } else {
            detail = new UnhandledDetail();
        }
        
        Container contentPane = getContentPane();
        if (detail.retrieveDetails(awsAccount, resourceName)) {
            contentPane.add(detail.getPanel());
        } else {
            contentPane.add(new DetailError(), BorderLayout.CENTER);
        }
        
        pack();
        setLocationRelativeTo(frame); 
    }
}
