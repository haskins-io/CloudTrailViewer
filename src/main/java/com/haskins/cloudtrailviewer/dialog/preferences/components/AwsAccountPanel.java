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

import com.haskins.cloudtrailviewer.dao.AccountDao;
import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.dialog.preferences.dialogs.AwsAccountDialog;
import com.haskins.cloudtrailviewer.model.AwsAccount;
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
import javax.swing.SwingConstants;
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
        
        List<AwsAccount> accounts = AccountDao.getAllAccounts(false);
        for (AwsAccount account : accounts) {
                        
            defaultListModel.addElement(account.getName());
        }
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
                
        list.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                
                if (mouseEvent.getClickCount() == 2) {
                    String accountName = (String)list.getSelectedValue();
                    updateAccount(accountName);
                    
                } else {
                    selected = list.getSelectedIndex();
                }
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
        title.setHorizontalAlignment(SwingConstants.CENTER);
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
            
            String update = "UPDATE aws_credentials SET active = 0";
            DbManager.getInstance().doInsertUpdate(update);
            
            AwsAccount account = AwsAccountDialog.showDialog(this, null);
            if (account != null) {
                
                StringBuilder query = new StringBuilder();
                query.append("INSERT INTO aws_credentials");
                query.append(" (aws_name, ");
                
                if (account.getAcctNumber() != null && account.getAcctNumber().length() > 0) {
                    query.append(" aws_acct, ");
                }
                
                if (account.getBucket() != null && account.getBucket().length() > 0) {
                    query.append(" aws_bucket, ");
                }
                
                query.append(" aws_key, aws_secret, aws_prefix, active)");
                query.append(" VALUES (");
                query.append("'").append(account.getName()).append("'").append(",");
                
                if (account.getAcctNumber() != null && account.getAcctNumber().length() > 0) {
                    query.append("'").append(account.getAcctNumber()).append("',");
                }
                
                if (account.getBucket() != null && account.getBucket().length() > 0) {
                    query.append("'").append(account.getBucket()).append("'").append(",");
                }
                
                query.append("'").append(account.getKey()).append("'").append(",");
                query.append("'").append(account.getSecret()).append("'").append(",");
                query.append("''").append(",");
                query.append("1");
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
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void updateAccount(String accountName) {
        
        AwsAccount account = AccountDao.getAccountByName(accountName);
        
        if (account != null) {
            
            AwsAccount updatedAccount = AwsAccountDialog.showDialog(this, account);
            if (updatedAccount != null) {
                
                StringBuilder updateQuery = new StringBuilder();
                updateQuery.append("UPDATE aws_credentials SET");
                updateQuery.append(" aws_name = '").append(updatedAccount.getName()).append("',");
                
                if (updatedAccount.getBucket()!= null && updatedAccount.getBucket().length() > 0) {
                    updateQuery.append(" aws_bucket = '").append(updatedAccount.getBucket()).append("',");
                }
                
                if (updatedAccount.getAcctNumber()!= null && updatedAccount.getAcctNumber().length() > 0) {
                    updateQuery.append(" aws_acct = '").append(updatedAccount.getAcctNumber()).append("',");
                }
                
                updateQuery.append(" aws_key = '").append(updatedAccount.getKey()).append("',");
                updateQuery.append(" aws_secret = '").append(updatedAccount.getSecret()).append("'");
                
                updateQuery.append(" WHERE id = ").append(updatedAccount.getId());
                
                DbManager.getInstance().doInsertUpdate(updateQuery.toString());
            }
        }
    }
}
