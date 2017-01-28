package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.observable.S3ListModel;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Created by markhaskins on 27/01/2017.
 */
class RemoteFileHandler extends FileHandler {

    private AwsAccount currentAccount = null;
    private String prefix = "";


    RemoteFileHandler(ListView<S3ListModel> listView, AwsAccount awsAccount, FileListControllerListener listener) {

        this.listView = listView;
        this.fileListControllerListener = listener;
        this.currentAccount = awsAccount;

        setUpMouseListener();

        reloadContents();
    }

    public List<String> getSelectedItems() {

        addSelectedKeys();

        return this.selected_keys;
    }

    void handleDoubleClickEvent() {

        String selected = listView.getSelectionModel().getSelectedItem().getName();

        if (selected.equalsIgnoreCase(MOVE_BACK)) {

            int lastSlash = prefix.lastIndexOf('/');
            String tmpPrefix = prefix.substring(0, lastSlash);

            prefix = "";
            if (tmpPrefix.contains("/")) {

                lastSlash = tmpPrefix.lastIndexOf('/') + 1;
                prefix = tmpPrefix.substring(0, lastSlash);
            }

            updateAccountPrefix(prefix);
            reloadContents();

        } else {

            int firstSlash = selected.indexOf('/');
            if (firstSlash == 0) {
                selected = selected.substring(1, selected.length());
            }

            int lastSlash = selected.lastIndexOf('/') + 1;
            if (lastSlash == selected.length()) {

                prefix = prefix + selected;
                updateAccountPrefix(prefix);
                reloadContents();
            } else {

                addSelectedKeys();
                updateAccountPrefix(prefix);

                fileListControllerListener.selectionComplete();
            }
        }
    }

    private void reloadContents() {

        listView.getItems().clear();

        ObjectListing objectListing = s3ListObjects(prefix, "/");

        addDirectories(objectListing);
        addFileKeys(objectListing);
    }

    private AmazonS3 getS3Client() {

        AWSCredentials credentials = new ProfileCredentialsProvider(currentAccount.getProfile()).getCredentials();
        return new AmazonS3Client(credentials);
    }

    private void updateAccountPrefix(String newPrefix) {
        currentAccount.setPrefix(newPrefix);
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

    private void addSelectedKeys() {

        if (listView.getItems() != null && !listView.getItems().isEmpty()) {

            String selected = prefix + listView.getSelectionModel().getSelectedItem().getName();

            // if the dialog is being used as part of a scan operation and a folder
            // is selected then we need to discover the files in the folder and
            // add them
            if (scanning && selected.endsWith("/")) {
                addFolderFiles(selected);

            } else {

                List<S3ListModel> selectedItems = listView.getItems();
                for (S3ListModel key : selectedItems) {
                    selected_keys.add(prefix + key.getName());
                }
            }
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

    private void addDirectories(ObjectListing objectListing) {

        List<String> directories = objectListing.getCommonPrefixes();
        for (String directory : directories) {

            String dir = stripPrefix(directory);
            String alias = dir;

            S3ListModel model = new S3ListModel(dir, alias, S3ListModel.FILE_DIR);
            listView.getItems().add(model);
        }
    }

    private void addFileKeys(ObjectListing objectListing) {

        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (final S3ObjectSummary objectSummary : objectSummaries) {

            String file = stripPrefix(objectSummary.getKey());

            S3ListModel model = new S3ListModel(file, file, S3ListModel.FILE_DOC);
            listView.getItems().add(model);
        }
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
}
