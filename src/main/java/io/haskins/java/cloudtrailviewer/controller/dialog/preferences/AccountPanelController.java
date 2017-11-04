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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.List;

/**
 *
 * controller class for the Account panel.
 *
 * Created by markhaskins on 26/01/2017.
 */
public class AccountPanelController {

    @FXML private TableView<AwsAccount> tableView;

    private final ObservableList<AwsAccount> data = FXCollections.observableArrayList();

    private AccountService accountDao;

    public void init(AccountService accountDao) {

        this.accountDao = accountDao;

        configureTableView();

        List<AwsAccount> accounts = accountDao.getAllAccounts(false);
        data.addAll(accounts);
    }

    private void configureTableView() {

        tableView.setEditable(true);
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AwsAccount, String> nameCol =  new TableColumn<>("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(
            (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                acct.setName(t.getNewValue());

                accountDao.updateAccount(acct);
            });

        TableColumn<AwsAccount, String> acctNumCol =  new TableColumn<>("Acct Number");
        acctNumCol.setMinWidth(100);
        acctNumCol.setCellValueFactory(new PropertyValueFactory<>("acctNumber"));

        acctNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        acctNumCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setAcctNumber(t.getNewValue());

                    accountDao.updateAccount(acct);

                });

        TableColumn<AwsAccount, String> acctAliasCol =  new TableColumn<>("Acct Alias");
        acctAliasCol.setMinWidth(100);
        acctAliasCol.setCellValueFactory(new PropertyValueFactory<>("acctAlias"));

        acctAliasCol.setCellFactory(TextFieldTableCell.forTableColumn());
        acctAliasCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setAcctAlias(t.getNewValue());

                    accountDao.updateAccount(acct);
                });

        TableColumn<AwsAccount, String> bucketCol =  new TableColumn<>("S3 Bucket");
        bucketCol.setMinWidth(100);
        bucketCol.setCellValueFactory(new PropertyValueFactory<>("bucket"));

        bucketCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bucketCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setBucket(t.getNewValue());

                    accountDao.updateAccount(acct);
                });

        TableColumn<AwsAccount, String> keyCol =  new TableColumn<>("AWS Key");
        keyCol.setMinWidth(100);
        keyCol.setCellValueFactory(new PropertyValueFactory<>("key"));

        keyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        keyCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setKey(t.getNewValue());

                    accountDao.updateAccount(acct);
                });

        TableColumn<AwsAccount, String> secretCol =  new TableColumn<>("AWS Secret");
        secretCol.setMinWidth(100);
        secretCol.setCellValueFactory(new PropertyValueFactory<>("secret"));

        secretCol.setCellFactory(TextFieldTableCell.forTableColumn());
        secretCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setSecret(t.getNewValue());

                    accountDao.updateAccount(acct);
                });

        TableColumn<AwsAccount, String> profileCol =  new TableColumn<>("AWS Profile");
        profileCol.setMinWidth(100);
        profileCol.setCellValueFactory(new PropertyValueFactory<>("profile"));

        profileCol.setCellFactory(TextFieldTableCell.forTableColumn());
        profileCol.setOnEditCommit(
                (TableColumn.CellEditEvent<AwsAccount, String> t) -> {

                    AwsAccount acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    acct.setProfile(t.getNewValue());

                    accountDao.updateAccount(acct);
                });

        tableView.getColumns().addAll(nameCol, acctNumCol, acctAliasCol, bucketCol, keyCol, secretCol, profileCol);
    }

    @FXML
    private void add() {
        tableView.getItems().add(0, new AwsAccount());
    }

    @FXML
    private void remove() {

        AwsAccount selected = tableView.getSelectionModel().getSelectedItem();
        tableView.getItems().remove(selected);

        accountDao.deleteAccount(selected);
    }
}
