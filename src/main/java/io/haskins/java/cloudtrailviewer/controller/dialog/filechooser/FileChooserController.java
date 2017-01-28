package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.service.AccountDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by markhaskins on 27/01/2017.
 */
public class FileChooserController implements FileListControllerListener {

    @FXML private FileListController fileListController;

    @FXML private Button cancel;
    @FXML private Button load;

    private AccountDao accountDao;
    private static AwsAccount currentAccount = null;

    private Stage dialogStage;

    public void init(Stage dialogStage, AccountDao accountDao) {

        this.dialogStage = dialogStage;

        if (accountDao != null) {
            this.accountDao = accountDao;
            getAccounts();

            fileListController.init(currentAccount, this);
        } else {
            fileListController.init(this);
        }
    }

    public List<String> getSelectedItems() {
        return fileListController.getSelectedItems();
    }

    private void getAccounts() {
        setCurrentAccount(new AwsAccount());
    }

    private static void setCurrentAccount(AwsAccount account) {
        currentAccount = account;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// FXML
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void cancel() {
        dialogStage.close();
    }

    public void load() {
        fileListController.getSelectedItems();
        selectionComplete();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// FileListControllerListener
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void listItemSelected(boolean isValid) {

        if (isValid) {
            load.setDisable(false);
        } else {
            load.setDisable(true);
        }
    }

    @Override
    public void selectionComplete() {
        dialogStage.close();
    }

    @Override
    public void exceptionCaught(Exception e) {
        dialogStage.close();
    }
}
