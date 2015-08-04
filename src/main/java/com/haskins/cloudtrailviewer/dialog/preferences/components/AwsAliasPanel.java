/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.dialog.preferences.components;

import com.haskins.cloudtrailviewer.core.DbManager;
import com.haskins.cloudtrailviewer.dialog.preferences.AwsAliasDialog;
import com.haskins.cloudtrailviewer.dialog.preferences.Preferences;
import com.haskins.cloudtrailviewer.model.AwsAlias;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class AwsAliasPanel extends JPanel implements ActionListener {

    private final DefaultTableModel defaultTableModel = new DefaultTableModel();  
    
    public AwsAliasPanel() {
        
        buildUI();
        
        String query = "SELECT aws_account, aws_alias FROM aws_alias";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            
            String aws_acct = (String)row.get("aws_account");
            String aws_alias = (String)row.get("aws_alias");
            
            defaultTableModel.addRow(new Object[] { aws_acct, aws_alias }); 
        }
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        
        defaultTableModel.addColumn("Account");
        defaultTableModel.addColumn("Alias");
        
        JTable table = new JTable(defaultTableModel);
        JScrollPane tablecrollPane = new JScrollPane(table);
        tablecrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.add(tablecrollPane, BorderLayout.CENTER); 
        
        
        final JButton btnNew = new JButton("New");
        btnNew.setActionCommand("New");
        btnNew.addActionListener(this);
        
        final JButton btnDelete = new JButton("Delete");
        btnDelete.setActionCommand("Delete");
        btnDelete.addActionListener(this);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(btnNew);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnDelete);
        
        this.add(buttonPane, BorderLayout.PAGE_END); 
    }
        ////////////////////////////////////////////////////////////////////////////
    // ActionListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equalsIgnoreCase("New")) {
            
            AwsAlias alias = AwsAliasDialog.showDialog(this);
            if (alias != null) {
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO aws_alias");
                query.append(" (aws_account, aws_alias)");
                query.append(" VALUES");
                query.append(" ('").append(alias.getAccountNumber()).append("', '").append(alias.getAccountAlias()).append("')");
                
                DbManager.getInstance().doInsertUpdate(query.toString());
                
                defaultTableModel.addRow(new Object[] { alias.getAccountNumber(), alias.getAccountAlias() });
            }
            
        } else {
            // defaultTableModel.removeRow(i);
        }
    }
    
}
