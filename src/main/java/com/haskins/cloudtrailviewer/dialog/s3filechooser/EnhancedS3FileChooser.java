/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2016  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.dialog.s3filechooser;

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.dao.AccountDao;
import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *  Enhanced version of the S3FileChooser dialog that provides Filtering options.
 * 
 * @author mark.haskins
 */
public class EnhancedS3FileChooser extends JDialog implements ActionListener, S3FileListListener {
    
    public static final int MODE_OPEN = 0;
    public static final int MODE_SCAN = 1;
    
    private static final String ACTION_LOAD = "ActionALoad";
    
    private static final DefaultComboBoxModel ACCOUNT_LIST = new DefaultComboBoxModel();
    private static final Map<String, AwsAccount> ACCOUNT_MAP = new HashMap<>();
    private static AwsAccount currentAccount = null;
    
    private static int current_mode = 0;
    
    private static EnhancedS3FileChooser dialog;
    
    private final JButton btnLoad = new JButton("Load");
    
    private static S3FileList fileList;
    
    /**
     * Shows the Dialog.
     *
     * @param parent The Frame to which the dialog will be associated
     * @param mode Mode dialog should operate
     * 
     * @return a List of String that are S3 bucket keys.
     */
    public static List<String> showDialog(Component parent, int mode) {

        current_mode = mode;
        
        getAccounts();
        
        fileList = new S3FileList(current_mode, currentAccount);
                
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new EnhancedS3FileChooser(frame);
        
        //if (fileList.init())  {
            dialog.setVisible(true);
        //}
        
        return fileList.getSelectedFiles();
    }
        
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
                
        if (e.getActionCommand().equalsIgnoreCase(ACTION_LOAD)) {
            
            StringBuilder query = new StringBuilder();
            query.append("UPDATE aws_credentials SET aws_prefix =");
            query.append(" '").append(fileList.getPrefix()).append("'");
            query.append(" WHERE id = ").append(currentAccount.getId());
            DbManager.getInstance().doInsertUpdate(query.toString());

        }
        
        fileList.dialogClosing();
        dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // S3FileListListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void listItemSelected(boolean isValid) {
        
        // requires an addition check : If in scan mode is a filter set
        if (isValid) {
            btnLoad.setEnabled(true);
        } else {
            btnLoad.setEnabled(false);
        }
    }

    @Override
    public void selectionComplete() {
        dialog.setVisible(false);
    }

    @Override
    public void exceptionCaught(Exception e) {
        
        String errorMessage = e.getMessage();
        errorMessage = errorMessage.replaceAll("; ", ";\n");

        JOptionPane.showMessageDialog(CloudTrailViewer.frame,
                errorMessage,
                "AWS Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private EnhancedS3FileChooser(Frame frame) {
        
        super(frame, "S3 File Browser", true);
        
        setLayout(new BorderLayout());
        
        JComboBox accountCombo = new JComboBox(ACCOUNT_LIST);
        
        FilterPanel filterPanel = new FilterPanel();
        filterPanel.setPreferredSize(new Dimension(400,400));
                
        fileList.registerListener(this);
        
        JPanel mainArea;
        if (current_mode == EnhancedS3FileChooser.MODE_SCAN) {
            mainArea = new JPanel(new GridLayout(1,2));
            mainArea.add(filterPanel);
            mainArea.add(fileList);
            
        } else {
            mainArea = new JPanel(new BorderLayout());
            mainArea.add(fileList);
        }
        
        btnLoad.setActionCommand(ACTION_LOAD);
        btnLoad.addActionListener(this);
        btnLoad.setEnabled(false);
        
        getRootPane().setDefaultButton(btnLoad);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(btnCancel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnLoad);
        
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(accountCombo, BorderLayout.PAGE_START);
        contentPane.add(mainArea, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);

        //Initialize values.
        pack();
        setLocationRelativeTo(frame);
    }
    
    private static void getAccounts() {

        List<AwsAccount> accounts = AccountDao.getAllAccountsWithBucket();
        for (AwsAccount account : accounts) {

            String name = account.getName();

            ACCOUNT_MAP.put(name, account);
            ACCOUNT_LIST.addElement(name);
            currentAccount = account;

            String setActive = "UPDATE aws_credentials SET active = 1 WHERE ID = " + currentAccount.getId();
            DbManager.getInstance().doInsertUpdate(setActive);
        }
    }

}
