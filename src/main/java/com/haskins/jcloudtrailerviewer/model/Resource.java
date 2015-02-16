package com.haskins.jcloudtrailerviewer.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author mark.haskins
 */
public class Resource {
    
    @JsonProperty("ARN")
    private String ARN = "";
    
    @JsonProperty("accountId")
    private String accountId = "";

    /**
     * @return the ARN
     */
    public String getARN() {
        return ARN;
    }

    /**
     * @param ARN the ARN to set
     */
    public void setARN(String ARN) {
        this.ARN = ARN;
    }

    /**
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
