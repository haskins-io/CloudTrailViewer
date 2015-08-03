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

package com.haskins.cloudtrailviewer.db;

import com.haskins.cloudtrailviewer.core.PreferencesController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark.haskins
 */
public class DbManager {
      
    private static DbManager instance = null;
    
    public static final String DB_USER = "ctv_user";
    public static final String DB_PASS = "247spey4PHa203";
            
    public static DbManager getInstance() {

        if (instance == null) {
            instance = new DbManager();  
        }

        return instance;
    }
    
    public void sync() {
                
        int currentVersion = getCurrentDbVersion();
        
        createVersion1(currentVersion);
    }
    
    public ResultSet doSelect(String query) {
        
        Connection conn = getDbConnection();
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);) {
            
            // convert rs to pojo so that connections can be closed.
            
        } catch (SQLException e) {
            
        }
        
        return null;
    }
    
    public int doInsertUpdate(String query) {
        
        int updated = -1;
        
        Connection conn = getDbConnection();
        
        try (Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            
        }
        
        return updated;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void doExecute(String query) {
        
        Connection conn = getDbConnection();
        
        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }
    
    private Connection getDbConnection() {
        
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(getDbUrl());
        }
        catch (SQLException ex) { }
        
        return conn;
    }
    
    private String getDbUrl() {
        
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.cloudtrailviewer/prefs.db";
        
        StringBuilder url = new StringBuilder();
        url.append("jdbc:derby:");
        url.append(systemDir);
        url.append(";user=");
        url.append(DB_USER);
        url.append(";password=");
        url.append(DB_PASS);
        
        return url.toString();
    }
    
    private int getCurrentDbVersion() {
        
        int currentVersion = 0;
                       
        String query = "SELECT db_version FROM db_properties WHERE id = 1";
        Object result = doSelect(query);
        
        if (result != null) {
            
        }
        
        return currentVersion;
    }
        ////////////////////////////////////////////////////////////////////////////
    ///// Migrations
    ////////////////////////////////////////////////////////////////////////////
    private int createVersion1(int currentVersion) {
        
        if (currentVersion < 1) {
            
            Connection conn = null;
            
            String url = getDbUrl() + ";create=true";;
            
            try {
                conn = DriverManager.getConnection(url);
            }
            catch (SQLException ex1) {
                Logger.getLogger(PreferencesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            if (conn != null) {
                                
                // Primary Preferences table
                StringBuilder createPrefTable = new StringBuilder();
                createPrefTable.append("CREATE TABLE ctv_preferences( ");
                createPrefTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createPrefTable.append("ctv_key VARCHAR(100), ");
                createPrefTable.append("ctv_vallue TEXT)");

                doExecute(createPrefTable.toString());

                // AWS credentials table
                StringBuilder createCredentialsTable = new StringBuilder();
                createCredentialsTable.append("CREATE TABLE aws_credentials( ");
                createCredentialsTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createCredentialsTable.append("aws_name VARCHAR(50), ");
                createCredentialsTable.append("aws_bucket VARCHAR(65), ");
                createCredentialsTable.append("aws_key VARCHAR(30), ");
                createCredentialsTable.append("aws_secret VARCHAR(50), ");
                createCredentialsTable.append("aws_prefix TEXT)");

                doExecute(createCredentialsTable.toString());
               

                // Version table
                StringBuilder createVersionTable = new StringBuilder();
                createVersionTable.append("CREATE TABLE db_properties( ");
                createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createVersionTable.append("db_version INT)");

                doExecute(createVersionTable.toString());
                
                String insertQuery = "INSERT INTO db_properties (db_version) VALUES 1";
                doInsertUpdate(insertQuery);
                
                currentVersion++;
            }
        }
        
        return currentVersion;
    }    
}
