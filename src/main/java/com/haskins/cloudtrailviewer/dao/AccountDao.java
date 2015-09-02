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

package com.haskins.cloudtrailviewer.dao;

import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public class AccountDao {
    
    public static AwsAccount getAccountByAcctNum(String acctNum) {
        
        AwsAccount account = null;
        String query = "SELECT * FROM aws_credentials WHERE aws_acct LIKE '" + acctNum + "'";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        if (rows.size() == 1) {
            
            ResultSetRow row = rows.get(0);
            account = populateAccount(row);
        }
        
        return account;
    }
    
    private static AwsAccount populateAccount(ResultSetRow row) {
        
        AwsAccount account = new AwsAccount(
                (Integer) row.get("id"),
                (String) row.get("aws_name"),
                (String) row.get("aws_acct"),
                (String) row.get("aws_bucket"),
                (String) row.get("aws_key"),
                (String) row.get("aws_secret"),
                (String) row.get("aws_prefix")
        );
        
        return account;
    }
    
}
