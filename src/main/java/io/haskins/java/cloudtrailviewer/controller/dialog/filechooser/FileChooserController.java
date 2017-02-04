/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

/**
 * Class that provides a custom dialog for loadinf files either locally or remotely.
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FileChooserController implements FileListControllerListener {

    @FXML private FileListController fileListController;

    @FXML private Button cancel;
    @FXML private Button load;

    private AccountService accountDao;
    private static AwsAccount currentAccount = null;

    private Stage dialogStage;

    private boolean canceled = false;

    public void init(Stage dialogStage, AccountService accountDao) {

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
