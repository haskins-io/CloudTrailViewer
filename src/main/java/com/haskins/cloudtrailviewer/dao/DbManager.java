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

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private DbManager() { }

    /**
     * Returns an instance of the DbManager.
     * @return 
     */
    public static DbManager getInstance() {
        return DbManagerHolder.INSTANCE;
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
                LOGGER.log(Level.WARNING, "Unable to get connection to database", ex1);
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
            Migrations.createVersion5(conn, currentVersion);
            Migrations.createVersion6(conn, currentVersion);
            Migrations.createVersion7(conn, currentVersion);
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
            ResultSet rs = stmt.executeQuery(query)) {

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
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
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
    private int executeIntStatement(String query, String columnName) {

        int retVal = -1;

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                retVal = rs.getInt(columnName);
            }

        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
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
            ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                retVal = rs.getString(columnName);
            }

        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
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

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
        }

        return updated;
    }

    /**
     * performs an Execute command
     * @param query  SQL statement to run
     */
    public void doExecute(String query) {

        Connection conn = getDbConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
        }
    }

    /**
     * get a valid DB Connection
     * @return 
     */
    private Connection getDbConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(getDbUrl(), new Properties());
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Couldn't execute statement", e);
        }

        return conn;
    }

    /**
     * Returns the URL of that database
     * @return 
     */
    private String getDbUrl() {

        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.cloudtrailviewer/prefs.db";

        StringBuilder url = new StringBuilder();
        url.append("jdbc:derby:");
        url.append(systemDir);

        return url.toString();
    }

    /**
     * Returns the current version of the database
     * @return version number of database
     */
    public int getCurrentDbVersion() {

        int currentVersion = 0;

        String query = "SELECT db_version FROM db_properties WHERE id = 1";
        int retVal = executeIntStatement(query, "db_version");

        if (retVal != -1) {
            currentVersion = retVal;
        }

        return currentVersion;
    }
    
    private static class DbManagerHolder {
        static final DbManager INSTANCE = new DbManager();
    }
}
