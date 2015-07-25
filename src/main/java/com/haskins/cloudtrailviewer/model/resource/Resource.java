package com.haskins.cloudtrailviewer.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;



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
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getARN() != null) { modelData.append(getARN()).append(", "); }
        if (getAccountId() != null) { modelData.append(getAccountId()).append(", "); }
        
        return modelData.toString();
    }
}
