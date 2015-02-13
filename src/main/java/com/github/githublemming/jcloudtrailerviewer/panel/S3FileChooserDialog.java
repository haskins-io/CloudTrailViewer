package com.github.githublemming.jcloudtrailerviewer.panel;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.github.githublemming.jcloudtrailerviewer.PropertiesSingleton;
import java.awt.BorderLayout;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class S3FileChooserDialog extends JDialog implements ActionListener {

    public static final int STATUS_CLOSED = 1;
    public static final int STATUS_FILES_SECLECTED = 2;
    
    private final DefaultListModel s3ListModel = new DefaultListModel();
    private final JList s3List = new JList(s3ListModel);
    
    private final JButton btnClose = new JButton("Close");
    private final JButton btnLoad = new JButton("Load");

    private String prefix = "";
    
    private final List<String> selectedKeys = new ArrayList<>();
    
    public S3FileChooserDialog() {

        buildUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource().equals(btnLoad)) {
            
            List<String> selectedItems = s3List.getSelectedValuesList();
            for (String key : selectedItems) {
                
                selectedKeys.add(this.prefix + key);
            }
        }
        
        this.setVisible(false);
    }

    public List<String> showDialog() {

        selectedKeys.clear();
        
        reloadContents();

        this.setVisible(true);
        
        return selectedKeys;
    }

    private void buildUI() {

        this.setTitle("S3 File Browser");

        this.setModalityType(APPLICATION_MODAL);
        this.setLayout(new BorderLayout());

        s3List.setPreferredSize(new Dimension(400, 480));
        s3List.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                
                if (mouseEvent.getClickCount() == 2) {
                    
                    handleDoubleClickEvent();
                    
                } else if (mouseEvent.getClickCount() == 1) {
                    
                    // enable / disable the Open button
                    String selected = s3List.getSelectedValue().toString();
                    
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
        
        btnClose.addActionListener(this);
        btnLoad.addActionListener(this);
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnClose);
        btnPanel.add(btnLoad);

        this.add(s3List, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        this.setPreferredSize(new Dimension(640, 480));
        this.pack();
    }

    private void handleDoubleClickEvent() {
        
        String selected = s3List.getSelectedValue().toString();
        
        // remove any slashes at the beginning
        int firstSlash = selected.indexOf("/");
        if (firstSlash == 0) {
            selected = selected.substring(1, selected.length());
        }
        
        // remove any trailing slashes
        int lastSlash = selected.lastIndexOf("/") + 1;
        if (lastSlash == selected.length()) {
            
            prefix = prefix + selected;
            reloadContents();
        }
        else {
            // it must be a file so we'll close the dialog
            this.setVisible(false);
        }
    }
    
    private void reloadContents() {

        List<String> tmp = new ArrayList<>();
        this.s3ListModel.clear();

        String bucketName = PropertiesSingleton.getInstance().getProperty("Bucket");

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");

        AWSCredentials credentials= new BasicAWSCredentials(
            PropertiesSingleton.getInstance().getProperty("Key"),
            PropertiesSingleton.getInstance().getProperty("Secret")
        );

        AmazonS3 s3Client = new AmazonS3Client(credentials);

        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);

        // these are directories
        List<String> directories = objectListing.getCommonPrefixes();
        for (String directory : directories) {

            tmp.add(stripPrefix(directory));
        }

        // these are files
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (final S3ObjectSummary objectSummary : objectSummaries) {

            tmp.add(stripPrefix(objectSummary.getKey()));
        }

        for (String key : tmp) {
            this.s3ListModel.addElement(key);
        }
    }

    private String stripPrefix(String key) {

        String stripped = key;

        if (this.prefix.trim().length() > 0) {

            int prefixLength = this.prefix.length() - 1;
            stripped = key.substring(prefixLength, key.length());

            int firstSlash = stripped.indexOf("/");
            if (firstSlash == 0) {
                stripped = stripped.substring(1, stripped.length());
            }
        }

        return stripped;
    }

}
