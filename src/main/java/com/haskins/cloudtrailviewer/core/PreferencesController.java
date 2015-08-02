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

package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.dialog.AwsAccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that stores and retrieves properties from OS provided Preferences implementation.
 * 
 * The class is a Singleton
 * 
 * @author mark
 */
public class PreferencesController {
        
    private boolean haveDbConnection = false;
    private boolean newDb = false;
    
    private static final String DB_USER = "ctv_user";
    private static final String DB_PASS = "spey4PHa";
    
    private static PreferencesController instance = null;
    
    private Connection dbConnection;
    
    private final Preferences prefs = Preferences.userRoot().node("CloudTrailViewer");
    
    private final Map<String, String> propertiesCache = new HashMap<>();
    
    public PreferencesController() {
        
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            
            getDbConnection();
            
            if (haveDbConnection && newDb) {
                createPreferencesTable();
                createAccountTable();
            }

        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(CloudTrailViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns an instance of the class
     * @return 
     */
    public static PreferencesController getInstance() {

        if (instance == null) {
            instance = new PreferencesController();  
        }

        return instance;
    }
    
    /**
     * Saves a Preference
     * @param key Unique Id of the property
     * @param value property to save
     */
    public void setProperty(String key, String value) {
        
        propertiesCache.put(key, value);
        prefs.put(key, value);
    }
    
    /**
     * returns a property
     * @param key Unique Id of the property
     * @return 
     */
    public String getProperty(String key) {
        
        if (propertiesCache.containsKey(key)) {
            return propertiesCache.get(key);
        } else {
            
            String value = prefs.get(key, "");
            propertiesCache.put(key, value);
            return value;
        }
    }
    
    /**
     * Utility method that checks if provided AWS API credentials and bucket name
     * are valid
     * @return 
     */
    public boolean checkS3Credentials() {
        
        boolean validS3Credentials = false;
        
        String keyRegex = "(?<![A-Z0-9])[A-Z0-9]{20}(?![A-Z0-9])";
        String secretRegex = "(?<![A-Za-z0-9/+=])[A-Za-z0-9/+=]{40}(?![A-Za-z0-9/+=])";
     
        Pattern keyPattern = Pattern.compile(keyRegex);
        Pattern secretPattern = Pattern.compile(secretRegex);
        
        boolean keyOK = false;
        if (getProperty(AwsAccount.S3_KEY_PROPERTY) != null) {
            Matcher keyMatcher = keyPattern.matcher(getProperty(AwsAccount.S3_KEY_PROPERTY));
            if (keyMatcher.matches()) {
                keyOK = true;
            }  
        }
        
        boolean secretOK = false;
        if (getProperty(AwsAccount.S3_SECRET_PROPERTY) != null) {
            Matcher secretMatcher = secretPattern.matcher(getProperty(AwsAccount.S3_SECRET_PROPERTY));
            if (secretMatcher.matches()) {
                secretOK = true;
            }
        }

        boolean bucketProvided = false;
        if (getProperty(AwsAccount.S3_BUCKET_PROPERTY) != null && getProperty(AwsAccount.S3_BUCKET_PROPERTY).length() >= 3) {
            bucketProvided = true;
        }
        
        if (keyOK && secretOK && bucketProvided) {
            validS3Credentials = true;
        }
        
        return validS3Credentials;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////    
    private void getDbConnection() {

        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.cloudtrailviewer";
        
        StringBuilder url = new StringBuilder();
        url.append("jdbc:derby:");
        url.append(systemDir);
        url.append(";user=");
        url.append(DB_USER);
        url.append(";password=");
        url.append(DB_PASS);
               
        try {
            dbConnection = DriverManager.getConnection(url.toString());
            haveDbConnection = true;
        }
        catch (SQLException ex) {

            url.append(";create=true");
            try {
                dbConnection = DriverManager.getConnection(url.toString());
                haveDbConnection = true;
                newDb = false;
            }
            catch (SQLException ex1) {
                Logger.getLogger(PreferencesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    private void createPreferencesTable() {
        
        StringBuilder createPrefTable = new StringBuilder();
        createPrefTable.append("CREATE TABLE ctv_preferences( ");
        createPrefTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
        createPrefTable.append("ctv_key VARCHAR(100), ");
        createPrefTable.append("ctv_vallue TEXT)");
        
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(createPrefTable.toString());
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void createAccountTable() {
        
        StringBuilder createAccountTable = new StringBuilder();
        createAccountTable.append("CREATE TABLE aws_account( ");
        createAccountTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
        createAccountTable.append("aws_name VARCHAR(50), ");
        createAccountTable.append("aws_bucket VARCHAR(65), ");
        createAccountTable.append("aws_key VARCHAR(30), ");
        createAccountTable.append("aws_secret VARCHAR(50), ");
        createAccountTable.append("aws_prefix TEXT, ");
        
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(createAccountTable.toString());
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
