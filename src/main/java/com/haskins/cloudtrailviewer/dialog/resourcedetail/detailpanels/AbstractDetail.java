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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetail;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public abstract class AbstractDetail extends JPanel implements ResourceDetail {
    
    private final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    private static final long serialVersionUID = -7936100241490334631L;
    
    private final ResourceDetailRequest detailRequest;
    final AWSCredentials credentials;
    
    final DefaultTableModel primaryTableModel = new DefaultTableModel();
    final DefaultTableModel tagsTableModel = new DefaultTableModel();
    
    private final JTable primaryTable = new JTable(primaryTableModel);
    final JScrollPane primaryScrollPane = new JScrollPane(primaryTable);
    
    private final JTable tagsTable = new JTable(tagsTableModel);
    final JScrollPane tagsScrollPane = new JScrollPane(tagsTable);
    
    AbstractDetail(ResourceDetailRequest detailRequest) {
        
        this.setLayout(new BorderLayout());
        
        this.detailRequest = detailRequest;
        
        this. credentials= new BasicAWSCredentials(
            detailRequest.getAccount().getKey(),
            detailRequest.getAccount().getSecret()
        );
        
        primaryTableModel.addColumn("Property");
        primaryTableModel.addColumn("Value");
        
        tagsTableModel.addColumn("Key");
        tagsTableModel.addColumn("Value");
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }
    
    /**
     * returns a string representation of the date passed.
     * @param date
     * @return 
     */
    String getDateString(Date date) {
        return SIMPLE_DATE_FORMATTER.format(date.getTime());
    }
}
