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

package io.haskins.java.cloudtrailviewer.controller.dialog.preferences;

import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class AccountPanelController {

    @FXML private TableView<AwsAccount> tableView;

    private AccountService accountDao;

    public void init(AccountService accountDao) {

        this.accountDao = accountDao;

        tableView.setEditable(true);
        tableView.getItems().clear();

        ObservableList<AwsAccount> data = tableView.getItems();

        List<AwsAccount> accounts = accountDao.getAllAccounts(false);
        for (AwsAccount account : accounts) {
            data.add(account);
        }
    }

    @FXML
    private void add() {
        tableView.getItems().add(0, new AwsAccount());
    }

    @FXML
    private void remove() {
        tableView.getItems().remove(tableView.getSelectionModel().getSelectedItem());
    }
}
