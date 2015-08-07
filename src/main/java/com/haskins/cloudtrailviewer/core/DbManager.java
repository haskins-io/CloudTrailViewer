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

import com.haskins.cloudtrailviewer.model.CurrentDbVersion;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.sql.Connection;
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

    private DbManager() { }

    /**
     * Returns an instance of the DbManager.
     * @return 
     */
    public static DbManager getInstance() {

        if (instance == null) {
            instance = new DbManager();
        }

        return instance;
    }

    /**
     * Checks if there any any updates to the database required and applies them.
     */
    public void sync() {

        CurrentDbVersion currentVersion = new CurrentDbVersion();
        
        Connection dbTest = getDbConnection();
        if (dbTest == null) {
            
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
            
        } else {
            currentVersion.setDbVersion(getCurrentDbVersion());
        }
        
        Connection conn = getDbConnection();
        if (conn != null) {
            
            Migrations.createVersion1(conn, currentVersion);
            Migrations.createVersion2(conn, currentVersion);
            Migrations.createVersion3(conn, currentVersion);
            Migrations.createVersion4(conn, currentVersion);
        }
    }

    /**
     * Executes the query and then returns the results as a List of ResultSetRow
     * objects. All SQL objects (Connect, Statement and ResultSet) will be closed
     * before the result is returned.
     * @param query SQL statement to run.
     * @return a List of results. If there were no results, or there was a problem
     * then the list will be empty.
     */
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

    /**
     * Executes the passed statement and returns a single integer as the result.
     * @param query SQL statement to execute
     * @param columnName Column name that contains the result
     * @return The result of the query or -1 if no result was found or there was
     * a problem.
     */
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

    /**
     * Executes the passed statement and returns a single String as the result.
     * @param query SQL statement to execute
     * @param columnName Column name that contains the result
     * @return The result of the query or -NULL if no result was found or there was
     * a problem.
     */
    public String executeStringStatement(String query, String columnName) {

        String retVal = null;

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);) {

            if (rs.next()) {
                retVal = rs.getString(columnName);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * Executes an UPDATE or INSERT statement.
     * @param query SQL statement to run
     * @return returns -1
     */
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

    public void doExecute(String query) {

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();) {
            stmt.execute(query);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getDbConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(getDbUrl(), new Properties());
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    public String getDbUrl() {

        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.cloudtrailviewer/prefs.db";

        StringBuilder url = new StringBuilder();
        url.append("jdbc:derby:");
        url.append(systemDir);

        return url.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private int getCurrentDbVersion() {

        int currentVersion = 0;

        String query = "SELECT db_version FROM db_properties WHERE id = 1";
        int retVal = executeIntStatement(query, "db_version");

        if (retVal != -1) {
            currentVersion = retVal;
        }

        return currentVersion;
    }
}
