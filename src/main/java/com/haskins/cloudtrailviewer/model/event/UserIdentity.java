/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.model.event;

import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;

import com.google.gson.annotations.SerializedName;


public class UserIdentity implements Serializable {

    private static final long serialVersionUID = -4325205520029296556L;
    
    private String type;
    private String arn;

    @SerializedName(value="userName", alternate={"username"}) private String userName;
    @SerializedName(value="principalId", alternate={"principalid"}) private String principalId;
    @SerializedName(value="accountId", alternate={"accountid"}) private String accountId;
    @SerializedName(value="accessKeyId", alternate={"accesskeyid"}) private String accessKeyId;
    @SerializedName(value="sessionContext", alternate={"sessioncontext"}) private SessionContext sessionContext;
    @SerializedName(value="invokedBy", alternate={"invokedby"}) private String invokedBy;
    @SerializedName(value="webIdFederationData", alternate={"webidfederationdata"}) private String webIdFederationData;
    

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
    private String getAccountId() {
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
    private String getAccessKeyId() {
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
    public SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
     * @param sessionContext the sessionContext to set
     */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * @return the webIdFederationData
     */
    private String getWebIdFederationData() {
        return webIdFederationData;
    }

    /**
     * @param webIdFederationData the webIdFederationData to set
     */
    public void setWebIdFederationData(String webIdFederationData) {
        this.webIdFederationData = webIdFederationData;
    }
    
    public DefaultMutableTreeNode getTree() {
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("User Identity");
        
        if (getType() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Type");
            node.add(new DefaultMutableTreeNode(getType()));
            root.add(node);
        }
        
        if (getPrincipalId() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Principal Id");
            node.add(new DefaultMutableTreeNode(getPrincipalId()));
            root.add(node);
        }
        
        if (getArn() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Arn");
            node.add(new DefaultMutableTreeNode(getArn()));
            root.add(node);
        }
        
        if (getAccountId() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Account Id");
            node.add(new DefaultMutableTreeNode(getAccountId()));
            root.add(node);
        }
                        
        if (getUserName() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Username");
            node.add(new DefaultMutableTreeNode(getUserName()));
            root.add(node);
        }
        
        if (getInvokedBy() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Invoked By");
            node.add(new DefaultMutableTreeNode(getInvokedBy()));
            root.add(node);
        }
        
        if (getWebIdFederationData() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Web Id Federation Data");
            node.add(new DefaultMutableTreeNode(getWebIdFederationData()));
            root.add(node);
        }
        
        if (getSessionContext() != null) {
            root.add(getSessionContext().getTree());
        }
        
        return root;
    }
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getType() != null) { modelData.append(getType()).append(", "); }
        if (getPrincipalId() != null) { modelData.append(getPrincipalId()).append(", "); }
        if (getArn() != null) { modelData.append(getArn()).append(", "); }
        if (getAccountId() != null) { modelData.append(getAccountId()).append(", "); }
        if (getAccessKeyId() != null) { modelData.append(getAccessKeyId()).append(", "); }
        if (getUserName() != null) { modelData.append(getUserName()).append(", "); }
        if (getInvokedBy() != null) { modelData.append(getInvokedBy()).append(", "); }
        if (getSessionContext() != null) { modelData.append(getSessionContext().toString()).append(", "); }
        if (getWebIdFederationData() != null) { modelData.append(getWebIdFederationData()).append(", "); }
        
        return modelData.toString();
    }
}
