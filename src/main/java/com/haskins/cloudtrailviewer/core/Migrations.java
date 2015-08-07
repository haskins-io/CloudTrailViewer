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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark.haskins
 */
public class Migrations {

    public static void createVersion1(Connection conn, Integer currentVersion) {

        if (currentVersion < 1) {

            if (!doesTableExists(conn, "ctv_preferences")) {

                StringBuilder createPrefTable = new StringBuilder();
                createPrefTable.append("CREATE TABLE ctv_preferences ( ");
                createPrefTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createPrefTable.append("ctv_key VARCHAR(100), ");
                createPrefTable.append("ctv_value LONG VARCHAR )");

                DbManager.getInstance().doExecute(createPrefTable.toString());
            }

            if (!doesTableExists(conn, "aws_credentials")) {

                StringBuilder createCredentialsTable = new StringBuilder();
                createCredentialsTable.append("CREATE TABLE aws_credentials ( ");
                createCredentialsTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createCredentialsTable.append("aws_name VARCHAR(50), ");
                createCredentialsTable.append("aws_bucket VARCHAR(65), ");
                createCredentialsTable.append("aws_key VARCHAR(30), ");
                createCredentialsTable.append("aws_secret VARCHAR(50), ");
                createCredentialsTable.append("aws_prefix LONG VARCHAR )");

                DbManager.getInstance().doExecute(createCredentialsTable.toString());
            }

            if (!doesTableExists(conn, "aws_alias")) {

                StringBuilder createCredentialsTable = new StringBuilder();
                createCredentialsTable.append("CREATE TABLE aws_alias ( ");
                createCredentialsTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createCredentialsTable.append("aws_account VARCHAR(12), ");
                createCredentialsTable.append("aws_alias VARCHAR(50) )");

                DbManager.getInstance().doExecute(createCredentialsTable.toString());
            }

            if (!doesTableExists(conn, "db_properties")) {

                StringBuilder createVersionTable = new StringBuilder();
                createVersionTable.append("CREATE TABLE db_properties ( ");
                createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
                createVersionTable.append("db_version INT )");

                DbManager.getInstance().doExecute(createVersionTable.toString());

                String insertQuery = "INSERT INTO db_properties (db_version) VALUES 1";
                DbManager.getInstance().doInsertUpdate(insertQuery);
            }

            currentVersion = 1;
        }
    }
    
    public static void createVersion2(Connection conn, Integer currentVersion) {
        
        if (currentVersion < 2) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("CREATE TABLE aws_security ( ");
            createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
            createVersionTable.append("api_call VARCHAR(50) )");

            DbManager.getInstance().doExecute(createVersionTable.toString());
            
//            StringBuilder query = new StringBuilder();
//            query.append("INSERT INTO aws_security VALUES");
//            query.append(" (''), ");
//            
//            query.append(" ('')");
//            DbManager.getInstance().doInsertUpdate(query.toString());
            
            
            String insertQuery = "UPDATE db_properties SET db_version = 2 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);

            currentVersion = 2;
        }
    }
    
    public static void createVersion3(Connection conn, Integer currentVersion) {
        
        if (currentVersion < 3) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("ALTER TABLE aws_credentials ");
            createVersionTable.append("ADD COLUMN active SMALLINT");

            DbManager.getInstance().doExecute(createVersionTable.toString());
            
            currentVersion = 3;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Utility methods
    ////////////////////////////////////////////////////////////////////////////
    private static boolean doesTableExists(Connection conn, String tablename) {

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
}
