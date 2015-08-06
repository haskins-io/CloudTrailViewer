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
import com.haskins.cloudtrailviewer.dialog.preferences.AwsAccountDialog;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 *
 * @author mark.haskins
 */
public class AwsAccountPanel extends JPanel implements ActionListener {

    private final DefaultListModel defaultListModel = new DefaultListModel();  
    private final JList list = new JList(defaultListModel);
    
    private int selected = -1;
    
    public AwsAccountPanel() {
        
        buildUI();
        
        String query = "SELECT aws_name FROM aws_credentials";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            
            String aws_name = (String)row.get("aws_name");
            
            defaultListModel.addElement(aws_name);
        }
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
                
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selected = list.getSelectedIndex();
            }
        });
        
        JScrollPane tablecrollPane = new JScrollPane(list);
        tablecrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        final JButton btnNew = new JButton();
        ToolBarUtils.addImageToButton(btnNew, "Add.png", "Add", "Add Alias");
        btnNew.setActionCommand("New");
        btnNew.addActionListener(this);
        
        final JButton btnDelete = new JButton();
        ToolBarUtils.addImageToButton(btnDelete, "Minus.png", "Remove", "Remove Alias");
        btnDelete.setActionCommand("Delete");
        btnDelete.addActionListener(this);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(Color.white);
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        Border border = buttonPane.getBorder();
        Border margin = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        buttonPane.setBorder(new CompoundBorder(border, margin));
        
        buttonPane.add(btnNew);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnDelete);
        
        JLabel title = new JLabel("AWS Accounts");
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        this.add(titlePanel, BorderLayout.PAGE_START);
        this.add(tablecrollPane, BorderLayout.CENTER); 
        this.add(buttonPane, BorderLayout.PAGE_END); 
    }
        ////////////////////////////////////////////////////////////////////////////
    // ActionListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equalsIgnoreCase("New")) {
            
            AwsAccount account = AwsAccountDialog.showDialog(this);
            if (account != null) {
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO aws_credentials");
                query.append(" (aws_name, aws_bucket, aws_key, aws_secret, aws_prefix)");
                query.append(" VALUES (");
                query.append("'").append(account.getName()).append("'").append(",");
                query.append("'").append(account.getBucket()).append("'").append(",");
                query.append("'").append(account.getKey()).append("'").append(",");
                query.append("'").append(account.getSecret()).append("'").append(",");
                query.append("''"); // insert intial prefix
                query.append(")");
                
                DbManager.getInstance().doInsertUpdate(query.toString());
                defaultListModel.addElement(account.getName());
            }
            
        } else {
            
            if (selected != -1) {
                
                String name = list.getSelectedValue().toString();
                
                String query = "DELETE FROM aws_credentials WHERE aws_name = '" +  name+ "'";
                DbManager.getInstance().doInsertUpdate(query);
                
                defaultListModel.remove(selected);
                selected = -1;
            }
        }
    }
}
