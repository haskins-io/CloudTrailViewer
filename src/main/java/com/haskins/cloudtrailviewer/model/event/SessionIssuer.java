package com.haskins.cloudtrailviewer.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author mark.haskins
 */
public class SessionIssuer {
    
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
    
    @JsonIgnore
    public DefaultMutableTreeNode getTree() {
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Session Issuer");
        
        String value = "Type : ";
        if (getType() != null) {
            value = value + getType();
        }
        treeNode.add(new DefaultMutableTreeNode(value));
        
        value = "Principal Id : ";
        if (getPrincipalId() != null) {
            value = value + getPrincipalId();
        }
        treeNode.add(new DefaultMutableTreeNode(value));
        
        value = "Arn : ";
        if (getArn() != null) {
            value = value + getArn();
        }
        treeNode.add(new DefaultMutableTreeNode(value));
        
        value = "Account Id : ";
        if (getAccountId() != null) {
            value = value + getAccountId();
        }
        treeNode.add(new DefaultMutableTreeNode(value));
        
        
        value = "Username : ";
        if (getUserName() != null) {
            value = value + getUserName();
        }
        treeNode.add(new DefaultMutableTreeNode(value));
        
        return treeNode;
    }
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getType() != null) { modelData.append(getType()).append(", "); }
        if (getPrincipalId() != null) { modelData.append(getPrincipalId()).append(", "); }
        if (getArn() != null) { modelData.append(getArn()).append(", "); }
        if (getAccountId() != null) { modelData.append(getAccountId()).append(", "); }
        if (getUserName() != null) { modelData.append(getUserName()).append(", "); }
        
        return modelData.toString();
    }
}
