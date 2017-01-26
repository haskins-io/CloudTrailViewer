package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.dao.ResultSetRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO class that handles AWS Account database interacation
 *
 * Created by markhaskins on 05/01/2017.
 */
@Service
class AccountDao {

    private final DatabaseService databaseService;

    @Autowired
    public AccountDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Returns an AwsAccount object for the given account number
     *
     * @param acctNum AWS account number
     * @return Will return NULL if not found.
     */
    public AwsAccount getAccountByAcctNum(String acctNum) {

        AwsAccount account = null;

        String query = "SELECT * FROM aws_credentials WHERE aws_acct LIKE '" + acctNum + "'";
        List<ResultSetRow> rows = databaseService.executeCursorStatement(query);
        if (rows.size() == 1) {

            ResultSetRow row = rows.get(0);
            account = getAccountFromResultSetRow(row);
        }

        return account;
    }

    /**
     * Returns an AwsAccount object for the given account name
     *
     * @param name use defined name of AWS Account
     * @return Will return NULL if not found.
     */
    public AwsAccount getAccountByName(String name) {

        AwsAccount account = null;

        String query = "SELECT * FROM aws_credentials WHERE aws_name LIKE '" + name + "'";
        List<ResultSetRow> rows = databaseService.executeCursorStatement(query);
        if (rows.size() == 1) {

            ResultSetRow row = rows.get(0);
            account = getAccountFromResultSetRow(row);
        }

        return account;
    }

    /**
     * Delete the account with the name
     *
     * @param name name of account to delete
     */
    public void deleteAccountByName(String name) {

        String query = "DELETE FROM aws_credentials WHERE aws_name = '" + name + "'";
        databaseService.doExecute(query);
    }

    /**
     * returns all Account that have a bucket associated with them
     *
     * @return Collection of AWS Accounts that have a bucket configured
     */
    public List<AwsAccount> getAllAccountsWithBucket() {

        return executeQuery("SELECT * FROM aws_credentials WHERE LENGTH(aws_bucket) > 1");
    }

    /**
     * returns all accounts if they are active.
     *
     * @param onlyActive Flag to indicate if Active or Inactive AWS Accounts should be returned
     * @return Collection of AWS Accounts objects
     */
    public List<AwsAccount> getAllAccounts(boolean onlyActive) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM aws_credentials");

        if (onlyActive) {
            query.append(" WHERE active = 1");
        }

        return executeQuery(query.toString());
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private List<AwsAccount> executeQuery(String query) {

        List<AwsAccount> accounts = new ArrayList<>();

        List<ResultSetRow> rows = databaseService.executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            accounts.add(getAccountFromResultSetRow(row));
        }

        return accounts;
    }

    private AwsAccount getAccountFromResultSetRow(ResultSetRow row) {

        return new AwsAccount(
            (Integer) row.get("id"),
            (String) row.get("aws_name"),
            (String) row.get("aws_acct"),
            (String) row.get("aws_bucket"),
            (String) row.get("aws_key"),
            (String) row.get("aws_secret"),
            (String) row.get("aws_prefix")
        );
    }
}

