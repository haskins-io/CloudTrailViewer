package com.haskins.jcloudtrailerviewer.panel;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mark
 */
public class S3FileChooserDialog extends JDialog implements ActionListener {
    
    private static S3FileChooserDialog dialog;
    private static final List<String> selectedKeys = new ArrayList<>();
    
    private final DefaultListModel<String> s3ListModel = new DefaultListModel();
    private final JList s3List;
    
    private String prefix = "";
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("Load".equals(e.getActionCommand())) {
            
            List<String> selectedItems = s3List.getSelectedValuesList();
            for (String key : selectedItems) {
                
                selectedKeys.add(this.prefix + key);
            }
        }
        
        S3FileChooserDialog.dialog.setVisible(false);
    }

    public static List<String> showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new S3FileChooserDialog(frame);
        
        dialog.setVisible(true);
        
        return selectedKeys;
    }

    private S3FileChooserDialog(Frame frame) {

        super(frame, "S3 File Browser", true);
                
        final JButton btnLoad = new JButton("Load");
        btnLoad.setActionCommand("Load");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        s3List = new JList(s3ListModel);
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
        
        s3List.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        s3List.setLayoutOrientation(JList.VERTICAL);
        s3List.setVisibleRowCount(-1);
        
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
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnLoad);
 
        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
 
        //Initialize values.
        reloadContents();
        pack();
        setLocationRelativeTo(frame);  
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
            S3FileChooserDialog.dialog.setVisible(false);
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
