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

package com.haskins.cloudtrailviewer.dialog;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.haskins.cloudtrailviewer.core.PreferencesController;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 * Shows a File Choose style dialog that loads files from an S3 bucket.
 * @author mark
 */
public class S3FileChooser extends JDialog implements ActionListener {
    
    private static final String MOVE_BACK = "..";
    
    private static S3FileChooser dialog;
    private static final List<String> selectedKeys = new ArrayList<>();
    
    private final DefaultListModel<S3ListModel> s3ListModel = new DefaultListModel();
    private final JList s3List;
    
    private static String prefix = "";
    
    private JLabel loadingLabel = new JLabel("Loading ...");
    
    /**
     * Shows the Dialog.
     * @param parent The Frame to which the dialog will be associated
     * @return a List of String that are S3 bucket keys.
     */
    public static List<String> showDialog(Component parent) {
        
        selectedKeys.clear();
        
        String s3_location = PreferencesController.getInstance().getProperty("S3_Location");
        if (s3_location != null) {
            prefix = s3_location;
        }
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new S3FileChooser(frame);
        dialog.setVisible(true);
        
        return selectedKeys;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("Load".equals(e.getActionCommand())) {
            
            addSelectedKeys();
        }
        
        PreferencesController.getInstance().setProperty("S3_Location", prefix);
        S3FileChooser.dialog.setVisible(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private S3FileChooser(Frame frame) {

        super(frame, "S3 File Browser", true);
                
        final JButton btnLoad = new JButton("Load");
        btnLoad.setActionCommand("Load");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        s3ListModel.clear();
        s3List = new JList(s3ListModel);
        s3List.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                
                if (mouseEvent.getClickCount() == 2) {
                    
                    handleDoubleClickEvent();
                    
                } else if (mouseEvent.getClickCount() == 1) {
                    
                    String selected = ((S3ListModel)s3List.getSelectedValue()).getPath();
                    
                    if (selected.contains("/")) {
                        btnLoad.setEnabled(false);
                    } 
                    else
                    {
                        btnLoad.setEnabled(true);
                    }
                }
            }
        });
        
        s3List.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        s3List.setLayoutOrientation(JList.VERTICAL);
        s3List.setVisibleRowCount(-1);
        s3List.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                S3ListModel model = (S3ListModel)value;
                
                if (model.getFileType() == S3ListModel.FILE_DIR) {
                    label.setIcon(UIManager.getIcon("FileView.directoryIcon"));
                } else if (model.getFileType() == S3ListModel.FILE_DOC){
                    label.setIcon(UIManager.getIcon("FileView.fileIcon"));
                }
                
                label.setText(model.getPath());
                
                return label;
            }
        });
        
        JScrollPane listScroller = new JScrollPane(s3List);
        listScroller.setPreferredSize(new Dimension(400,480));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
 
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(loadingLabel);
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnLoad);
 
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(getToolbar(), BorderLayout.PAGE_START);
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
 
        //Initialize values.
        pack();
        setLocationRelativeTo(frame);      
        
        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {
                reloadContents();
                return null;
            };
        };

       worker.execute();
    }

    private void handleDoubleClickEvent() {
        
        String selected = ((S3ListModel)s3List.getSelectedValue()).getPath();
        
        if (selected.equalsIgnoreCase(MOVE_BACK)) {
           
            // update prefix and reload
            int lastSlash = prefix.lastIndexOf("/");
            String tmpPrefix = prefix.substring(0,lastSlash);
            
            if (tmpPrefix.contains("/")) {
                
                lastSlash = tmpPrefix.lastIndexOf("/") + 1;
                prefix = tmpPrefix.substring(0,lastSlash);
                
            } else {
                prefix = "";
            }

            reloadContents();
            
        } else {
        
            int firstSlash = selected.indexOf("/");
            if (firstSlash == 0) {
                selected = selected.substring(1, selected.length());
            }

            int lastSlash = selected.lastIndexOf("/") + 1;
            if (lastSlash == selected.length()) {

                prefix = prefix + selected;
                reloadContents();
            }
            else {
                addSelectedKeys();  
                
                PreferencesController.getInstance().setProperty("S3_Location", prefix);
                S3FileChooser.dialog.setVisible(false);
            }
        }
    }
    
    private void reloadContents() {

        loadingLabel.setVisible(true);
        this.s3ListModel.clear();

        String bucketName = PreferencesController.getInstance().getProperty("aws.bucket");

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");

        AWSCredentials credentials= new BasicAWSCredentials(
            PreferencesController.getInstance().getProperty("aws.key"),
            PreferencesController.getInstance().getProperty("aws.secret")
        );

        AmazonS3 s3Client = new AmazonS3Client(credentials);

        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

        // Add .. if not at root
        if (prefix.trim().length() != 0) {
            S3ListModel model = new S3ListModel(MOVE_BACK, "", S3ListModel.FILE_BACK);
            this.s3ListModel.addElement(model);
        }
        
        // these are directories
        List<String> directories = objectListing.getCommonPrefixes();
        for (String directory : directories) {

            String dir = stripPrefix(directory);
            int lastSlash = dir.lastIndexOf("/");
            String strippeDir = dir.substring(0,lastSlash);
            
            String alias = null;
            if (isAccountNumber(strippeDir)) {
                // look up to see if there is an alias
            }
            
            S3ListModel model = new S3ListModel(dir, alias, S3ListModel.FILE_DIR);
            this.s3ListModel.addElement(model);
        }

        // these are files
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (final S3ObjectSummary objectSummary : objectSummaries) {

            S3ListModel model = new S3ListModel(stripPrefix(objectSummary.getKey()), "", S3ListModel.FILE_DOC);
            this.s3ListModel.addElement(model);
        }
        
        loadingLabel.setVisible(false);
    }

    private String stripPrefix(String key) {

        String stripped = key;

        if (S3FileChooser.prefix.trim().length() > 0) {

            int prefixLength = S3FileChooser.prefix.length() - 1;
            stripped = key.substring(prefixLength, key.length());

            int firstSlash = stripped.indexOf("/");
            if (firstSlash == 0) {
                stripped = stripped.substring(1, stripped.length());
            }
        }

        return stripped;
    }
    
    private void addSelectedKeys() {
        
        List<S3ListModel> selectedItems = s3List.getSelectedValuesList();
        for (S3ListModel key : selectedItems) {

            selectedKeys.add(S3FileChooser.prefix + key.getPath());
        }
    }
    
    private JToolBar getToolbar() {
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        JButton btnHome = new JButton();
        ToolBarUtils.addImageToButton(btnHome, "Home-32.png", "Home", "Home");
        btnHome.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                prefix = "";
                reloadContents();
            }
        }); 
        
        toolbar.add(btnHome);
        
        return toolbar;
    }
    
    private boolean isAccountNumber(String accountnumber) {
        
        boolean isAccountNumber = false;
        
        if (accountnumber.length() == 12) {
            
            try {
                Integer.parseInt(accountnumber);
                isAccountNumber = true;
            } catch (Exception e) { }
        }
        
        return isAccountNumber;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Model class
    ////////////////////////////////////////////////////////////////////////////
    class S3ListModel {
        
        public static final int FILE_BACK = 0;
        public static final int FILE_DIR = 1;
        public static final int FILE_DOC = 2;
        
        private final String path;
        private final String alias;
        private final int fileType;
        
        S3ListModel(String path, String alias, int fileType) {
            this.path = path;
            this.alias = alias;
            this.fileType = fileType;
        }
        
        public String getPath() {
            return this.path;
        }
        public String getAlias() {
            return this.alias;
        }
        
        public int getFileType() {
            return this.fileType;
        }
    }
}
