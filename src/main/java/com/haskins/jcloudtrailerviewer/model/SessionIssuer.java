/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.model;

import org.codehaus.jackson.annotate.JsonProperty;

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
