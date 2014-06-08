package com.haskins.aws.jcloudtrailviewer.models;

import java.util.Map;
import org.codehaus.jackson.annotate.JsonProperty;

public class UserIdentity {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("principalId")
    private String principalId;
    
    @JsonProperty("arn")
    private String arn;
    
    @JsonProperty("accountId")
    private String accountId;
    
    @JsonProperty("userName")
    private String userName;
    
    
    // optional
    @JsonProperty("accessKeyId")
    private String accessKeyId;
    
    @JsonProperty("sessionContext")
    private Map sessionContext;
    
    @JsonProperty("invokedBy")
    private String invokedBy;
    

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the principalId
     */
    public String getPrincipalId() {
        return principalId;
    }

    /**
     * @param principalId the principalId to set
     */
    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    /**
     * @return the arn
     */
    public String getArn() {
        return arn;
    }

    /**
     * @param arn the arn to set
     */
    public void setArn(String arn) {
        this.arn = arn;
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

    /**
     * @return the accessKeyId
     */
    public String getAccessKeyId() {
        return accessKeyId;
    }

    /**
     * @param accessKeyId the accessKeyId to set
     */
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the invokedBy
     */
    public String getInvokedBy() {
        return invokedBy;
    }

    /**
     * @param invokedBy the invokedBy to set
     */
    public void setInvokedBy(String invokedBy) {
        this.invokedBy = invokedBy;
    }

    /**
     * @return the sessionContext
     */
    public Map getSessionContext() {
        return sessionContext;
    }

    /**
     * @param sessionContext the sessionContext to set
     */
    public void setSessionContext(Map sessionContext) {
        this.sessionContext = sessionContext;
    }
}
