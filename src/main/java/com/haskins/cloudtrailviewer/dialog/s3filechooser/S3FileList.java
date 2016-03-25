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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.thirdparty.com.bric.swing.NavigationListener;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.awt.BorderLayout;
import java.awt.Component;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

/**
 *
 * @author mark.haskins
 */
class S3FileList extends JPanel implements MouseListener, NavigationListener {
    
    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final String MOVE_BACK = "..";
    private static final long serialVersionUID = 4250579966344934358L;
    
    private final List<String> selected_keys = new ArrayList<>();
    
    private final DefaultListModel<S3ListModel> s3ListModel = new DefaultListModel();
    private final JList s3List;
    
    private final List<S3FileListListener> listeners = new ArrayList<>();
    
    private final S3FileListNavigationPanel navigationPanel;
    
    private final Map<String, String> alias_map = new HashMap<>();
    
    private AwsAccount currentAccount = null;
    
    private String prefix = "";
    
    private boolean scanning = false;
        
    /**
     * Default Constuctor
     * @param mode Mode class should work in
     * @param awsAccount Initial AWS Account to use
     */
    S3FileList(int mode, AwsAccount awsAccount) {
        
        if (mode == EnhancedS3FileChooser.MODE_SCAN) {
            scanning = true;
        }
        
        setAccount(awsAccount);
        
        s3ListModel.clear();
        s3List = new JList(s3ListModel);
        
        navigationPanel = new S3FileListNavigationPanel(this);
        
        getAliases();
        
        buildUI();  
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the AWS Account that the class will use to read files from
     * @param newAccount new Account
     */
    private void setAccount(AwsAccount newAccount) {
        this.currentAccount = newAccount;
        this.prefix = newAccount.getPrefix();
    }
        
    /**
     * Adds a listener to the class
     * @param l class to register
     */
    void registerListener(S3FileListListener l) {
        listeners.add(l);
    }
    
    /**
     * Initialises the Class, and attempts to load the first batch of files from S3
     * @return Return True if sucessful otherwise false
     */
    boolean init() {
        
        boolean initOK = false;
        
        try {
            reloadContents();
            initOK = true;
        } catch (Exception e) {
            
            for (S3FileListListener l : listeners) {
                l.exceptionCaught(e);
            }
        }
        
        return initOK;
    }
    
    /**
     * Should be called when the dialog is about to close.
     */
    void dialogClosing() {
        addSelectedKeys();
    }
        
    /**
     * Returns the Files that are currently selected in the List
     * @return Collection of filenames as strings
     */
    List<String> getSelectedFiles() {
        return selected_keys;
    }
    
    /**
     * returns the current S3 Prefix
     * @return 
     */
    String getPrefix() {
        return this.prefix;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // MouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void mouseClicked(MouseEvent e) {
        
        if (e.getClickCount() == 2) {

            handleDoubleClickEvent();

        } else if (e.getClickCount() == 1) {

            String selected = ((S3ListModel) s3List.getSelectedValue()).getPath();

            if (!scanning && selected.contains("/")) {
                notifyListenersOfSelection(false);
            } 
            else {
                notifyListenersOfSelection(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    ////////////////////////////////////////////////////////////////////////////
    // NavigationListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean elementsSelected(ListSelectionType type, Object... elements) {

        if (type == NavigationListener.ListSelectionType.SINGLE_CLICK) {

            StringBuilder path = new StringBuilder();

            String selected = (String) elements[0];

            String[] path_elements = navigationPanel.getBreadCrumbPath();
            for (String element : path_elements) {

                if (!element.equalsIgnoreCase(selected)) {
                    path.append(element).append("/");
                } else {
                    path.append(element).append("/");
                    break;
                }
            }

            prefix = path.toString();
            reloadContents();
        }

        return true; 
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
       
        s3List.addMouseListener(this);
        s3List.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        s3List.setLayoutOrientation(JList.VERTICAL);
        s3List.setVisibleRowCount(-1);
        s3List.setCellRenderer(new S3CellRenderer());
        
        JScrollPane listScroller = new JScrollPane(s3List);
        listScroller.setPreferredSize(new Dimension(450, 480));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        JPanel listPane = new JPanel(new BorderLayout());
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        listPane.add(listScroller, BorderLayout.CENTER);
        listPane.add(navigationPanel, BorderLayout.PAGE_END);        
        
        this.add(listPane);
    }
    
    private void notifyListenersOfSelection(boolean isValid) {
        
        for (S3FileListListener l : listeners) {
            l.listItemSelected(isValid);
        }
    }
    
    private void handleDoubleClickEvent() {

        String selected = ((S3ListModel) s3List.getSelectedValue()).getPath();

        if (selected.equalsIgnoreCase(MOVE_BACK)) {

            int lastSlash = prefix.lastIndexOf('/');
            String tmpPrefix = prefix.substring(0, lastSlash);

            if (tmpPrefix.contains("/")) {

                lastSlash = tmpPrefix.lastIndexOf('/') + 1;
                prefix = tmpPrefix.substring(0, lastSlash);

            } else {
                prefix = "";
            }

            reloadContents();

        } else {

            int firstSlash = selected.indexOf('/');
            if (firstSlash == 0) {
                selected = selected.substring(1, selected.length());
            }

            int lastSlash = selected.lastIndexOf('/') + 1;
            if (lastSlash == selected.length()) {

                prefix = prefix + selected;
                reloadContents();
            } else {
                
                addSelectedKeys();
                currentAccount.setPrefix(prefix);
                
                for (S3FileListListener l : listeners) {
                    l.selectionComplete();
                }
            }
        }
    }
    
    private void reloadContents() {

        navigationPanel.setBreadCrumb(prefix.split("/"));

        this.revalidate();

        this.s3ListModel.clear();

        ObjectListing objectListing = s3ListObjects(prefix, "/");

        // Add .. if not at root
        if (prefix.trim().length() != 0) {
            S3ListModel model = new S3ListModel(MOVE_BACK, MOVE_BACK, S3ListModel.FILE_BACK);
            this.s3ListModel.addElement(model);
        }

        // these are directories
        List<String> directories = objectListing.getCommonPrefixes();
        for (String directory : directories) {

            String dir = stripPrefix(directory);
            int lastSlash = dir.lastIndexOf('/');
            String strippeDir = dir.substring(0, lastSlash);

            String alias = dir;
            if (isAccountNumber(strippeDir)) {
                if (alias_map.containsKey(strippeDir)) {
                    alias = alias_map.get(strippeDir);
                }
            }

            S3ListModel model = new S3ListModel(dir, alias, S3ListModel.FILE_DIR);
            this.s3ListModel.addElement(model);
        }

        addFileKeys(objectListing);

        this.revalidate();
    }
    
    private void addSelectedKeys() {

        if (s3List.getSelectedValue() != null) {
            
            String selected = prefix + ((S3ListModel) s3List.getSelectedValue()).getPath();

            // if the dialog is being used as part of a scan operation and a folder
            // is selected then we need to discover the files in the folder and
            // add them
            if (scanning && selected.endsWith("/")) {
                addFolderFiles(selected);

            } else {

                List<S3ListModel> selectedItems = s3List.getSelectedValuesList();
                for (S3ListModel key : selectedItems) {
                    selected_keys.add(prefix + key.getPath());
                }  
            }
        }
    }
    
    private ObjectListing s3ListObjects(String pathPrefix, String delimiter) {

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(currentAccount.getBucket());
        listObjectsRequest.setPrefix(pathPrefix);
        
        if (delimiter != null) {
            listObjectsRequest.setDelimiter(delimiter);
        }
        
        AmazonS3 s3Client = getS3Client();

        return s3Client.listObjects(listObjectsRequest);
    }
    
    private String stripPrefix(String key) {

        String stripped = key;

        if (prefix.trim().length() > 0) {

            int prefixLength = prefix.length() - 1;
            stripped = key.substring(prefixLength, key.length());

            int firstSlash = stripped.indexOf('/');
            if (firstSlash == 0) {
                stripped = stripped.substring(1, stripped.length());
            }
        }

        return stripped;
    }
    
    private void addFileKeys(ObjectListing objectListing) {
        
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (final S3ObjectSummary objectSummary : objectSummaries) {

            String file = stripPrefix(objectSummary.getKey());

            S3ListModel model = new S3ListModel(file, file, S3ListModel.FILE_DOC);
            this.s3ListModel.addElement(model);
        }
    }
    
    private void addFolderFiles(String path) {
        
        AmazonS3 s3Client = getS3Client();
        
        ObjectListing current = s3Client.listObjects(currentAccount.getBucket(), path);
        List<S3ObjectSummary> objectSummaries = current.getObjectSummaries();

        for (final S3ObjectSummary objectSummary : objectSummaries) {
            String file = objectSummary.getKey();
            selected_keys.add(file);
        }
        
        while (current.isTruncated()) {
            
            current = s3Client.listNextBatchOfObjects(current);
            objectSummaries = current.getObjectSummaries();
            
            for (final S3ObjectSummary objectSummary : objectSummaries) {
                String file = objectSummary.getKey();
                selected_keys.add(file);
            }   
        }
    }
    
    private boolean isAccountNumber(String accountnumber) {

        boolean isAccountNumber = false;

        if (accountnumber.length() == 12) {

            try {
                Long.parseLong(accountnumber);
                isAccountNumber = true;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to check of [" + accountnumber + "] is a number", e);
            }
        }

        return isAccountNumber;
    }
    
    private AmazonS3 getS3Client() {
        
        AWSCredentials credentials = new BasicAWSCredentials(
            currentAccount.getKey(),
            currentAccount.getSecret()
        );

        return new AmazonS3Client(credentials);
    }
    
    private void getAliases() {

        alias_map.clear();

        String query = "SELECT aws_account, aws_alias FROM aws_alias";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {

            String aws_acct = (String) row.get("aws_account");
            String aws_alias = (String) row.get("aws_alias");

            alias_map.put(aws_acct, aws_alias);
        }
    }
}

class S3CellRenderer extends DefaultListCellRenderer {
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        S3ListModel model = (S3ListModel) value;

        if (model.getFileType() == S3ListModel.FILE_DIR) {
            label.setIcon(UIManager.getIcon("FileView.directoryIcon"));
            
        } else if (model.getFileType() == S3ListModel.FILE_DOC) {
            label.setIcon(UIManager.getIcon("FileView.fileIcon"));
        }

        label.setText(model.getAlias());

        return label;
    }
}
