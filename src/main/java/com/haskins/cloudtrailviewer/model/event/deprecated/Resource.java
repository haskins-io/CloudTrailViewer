
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

package com.haskins.cloudtrailviewer.model.event.deprecated;

/**
 * 
 * @author mark.haskins
 */
public class Resource {
    
    private String ARN = "";
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
