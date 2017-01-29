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

    private boolean canceled = false;

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

        if (!canceled) {
            return fileListController.getSelectedItems();
        } else {
            return null;
        }
    }

    private void getAccounts() {

        List<AwsAccount> accounts = accountDao.getAllAccountsWithBucket();
        for (AwsAccount account : accounts) {
            setCurrentAccount(account);
        }
    }

    private static void setCurrentAccount(AwsAccount account) {
        currentAccount = account;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// FXML
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void cancel() {

        canceled = true;
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
