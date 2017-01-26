package io.haskins.java.cloudtrailviewer.model.aws;

/**
 * Model class that defines an alias to an AWS Account
 *
 * Created by markhaskins on 05/01/2017.
 */
class AwsAlias  {

    private final String number;
    private final String alias;

    public AwsAlias(String account_number, String account_alias) {
        this.number = account_number;
        this.alias = account_alias;
    }

    public String getAccountNumber() {
        return this.number;
    }

    public String getAccountAlias() {
        return this.alias;
    }
}
