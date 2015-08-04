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

import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark.haskins
 */
public class DbManager {

    private static DbManager instance = null;

    private DbManager() {

        Connection conn = getDbConnection();
        if (conn == null) {
            createVersion1(0);
        }
    }

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

    public List<ResultSetRow> executeCursorStatement(String query) {

        List rows = new ArrayList();

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            while (rs.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }

                rows.add(new ResultSetRow(row));
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public int executeIntStatement(String query, String columnName) {

        int retVal = -1;

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            if (rs.next()) {
                retVal = rs.getInt(columnName);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public String executeStringStatement(String query) {

        String retVal = null;

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            if (rs.next()) {
                retVal = rs.getString(0);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public int doInsertUpdate(String query) {

        int updated = -1;

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
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
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean doesTableExists(Connection conn, String tablename) {

        boolean doesTableExist = false;

        String[] types = new String[]{"TABLE"};

        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet resultset = dbm.getTables(null, null, null, types);
            while (resultset.next()) {

                if (resultset.getString(3).equalsIgnoreCase(tablename)) {
                    doesTableExist = true;
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doesTableExist;
    }

    private Connection getDbConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(getDbUrl(), new Properties());
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    private String getDbUrl() {

        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.cloudtrailviewer/prefs.db";

        StringBuilder url = new StringBuilder();
        url.append("jdbc:derby:");
        url.append(systemDir);

        return url.toString();
    }

    private int getCurrentDbVersion() {

        int currentVersion = 0;

        String query = "SELECT db_version FROM db_properties WHERE id = 1";
        int retVal = executeIntStatement(query, "db_version");

        if (retVal != -1) {
            currentVersion = retVal;
        }

        return currentVersion;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Migrations
    ////////////////////////////////////////////////////////////////////////////
    private int createVersion1(int currentVersion) {

        if (currentVersion < 1) {

            System.out.println("Creating Database");
            
            String url = getDbUrl();
            Properties properties = new Properties();
            properties.put("create", "true");

            try {
                DriverManager.getConnection(url, properties);
            }
            catch (SQLException ex1) {
                Logger.getLogger(PreferencesController.class.getName()).log(Level.SEVERE, null, ex1);
            }

            Connection conn = getDbConnection();

            if (conn != null) {

                // Primary Preferences table
                if (!doesTableExists(conn, "ctv_preferences")) {

                    StringBuilder createPrefTable = new StringBuilder();
                    createPrefTable.append("CREATE TABLE ctv_preferences ( ");
                    createPrefTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                    createPrefTable.append("ctv_key VARCHAR(100), ");
                    createPrefTable.append("ctv_value LONG VARCHAR )");

                    doExecute(createPrefTable.toString());
                }

                // AWS credentials table
                if (!doesTableExists(conn, "aws_credentials")) {

                    StringBuilder createCredentialsTable = new StringBuilder();
                    createCredentialsTable.append("CREATE TABLE aws_credentials ( ");
                    createCredentialsTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                    createCredentialsTable.append("aws_name VARCHAR(50), ");
                    createCredentialsTable.append("aws_bucket VARCHAR(65), ");
                    createCredentialsTable.append("aws_key VARCHAR(30), ");
                    createCredentialsTable.append("aws_secret VARCHAR(50), ");
                    createCredentialsTable.append("aws_prefix LONG VARCHAR )");

                    doExecute(createCredentialsTable.toString());
                }
                
                // AWS credentials table
                if (!doesTableExists(conn, "aws_alias")) {

                    StringBuilder createCredentialsTable = new StringBuilder();
                    createCredentialsTable.append("CREATE TABLE aws_alias ( ");
                    createCredentialsTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                    createCredentialsTable.append("aws_account VARCHAR(12), ");
                    createCredentialsTable.append("aws_alias VARCHAR(50) )");

                    doExecute(createCredentialsTable.toString());
                }

                // Version table
                if (!doesTableExists(conn, "db_properties")) {

                    StringBuilder createVersionTable = new StringBuilder();
                    createVersionTable.append("CREATE TABLE db_properties ( ");
                    createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                    createVersionTable.append("db_version INT )");

                    doExecute(createVersionTable.toString());

                    String insertQuery = "INSERT INTO db_properties (db_version) VALUES 1";
                    doInsertUpdate(insertQuery);
                }

                currentVersion++;
            }
        }

        return currentVersion;
    }
}
