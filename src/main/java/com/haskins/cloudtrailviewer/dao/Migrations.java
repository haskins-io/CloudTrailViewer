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

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    final static void createVersion1(Connection conn, CurrentDbVersion currentVersion) {

        if (currentVersion.getDbVersion() < 1) {

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

            currentVersion.setDbVersion(1);
        }
    }
    
    final static void createVersion2(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 2) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("CREATE TABLE aws_security ( ");
            createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
            createVersionTable.append("api_call VARCHAR(50) )");
            DbManager.getInstance().doExecute(createVersionTable.toString());
            
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO aws_security (api_call) VALUES");
            query.append(" ('AuthorizeSecurityGroupEgress'), ");
            query.append(" ('AuthorizeSecurityGroupIngress'), ");
            query.append(" ('PutGroupPolicy'), ");
            query.append(" ('PutRolePolicy'), ");
            query.append(" ('PutUserPolicy') ");
            DbManager.getInstance().doInsertUpdate(query.toString());
            
            String insertQuery = "UPDATE db_properties SET db_version = 2 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);

            currentVersion.setDbVersion(2);
        }
    }
    
    final static void createVersion3(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 3) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("ALTER TABLE aws_credentials ");
            createVersionTable.append("ADD COLUMN active SMALLINT");
            DbManager.getInstance().doExecute(createVersionTable.toString());

            String insertQuery = "UPDATE db_properties SET db_version = 3 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);
            
            currentVersion.setDbVersion(3);
        }
    }
    
    final static void createVersion4(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 4) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("CREATE TABLE aws_resources ( ");
            createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
            createVersionTable.append("api_call VARCHAR(50) )");
            DbManager.getInstance().doExecute(createVersionTable.toString());
            
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO aws_resources (api_call) VALUES");
            query.append(" ('RunInstances'), ");
            query.append(" ('StartInstances'), ");
            query.append(" ('StopInstances'), ");
            query.append(" ('TerminateInstances') ");
            DbManager.getInstance().doInsertUpdate(query.toString());
            
            String insertQuery = "UPDATE db_properties SET db_version = 4 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);

            currentVersion.setDbVersion(4);
        }
    }
    
    final static void createVersion5(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 5) {
        
            StringBuilder truncateResources = new StringBuilder();
            truncateResources.append("TRUNCATE TABLE aws_resources");
            DbManager.getInstance().doExecute(truncateResources.toString());
            
            StringBuilder query = new StringBuilder();
            query.append("INSERT INTO aws_resources (api_call) VALUES");
            query.append(" ('CreateQueue'), ");
            query.append(" ('DeleteQueue'), ");
            query.append(" ('CreateTopic'), ");
            query.append(" ('DeleteTopic'), ");
            query.append(" ('CreateBucket'), ");
            query.append(" ('DeleteBucket'), ");
            query.append(" ('CreateCluster'), ");
            query.append(" ('DeleteCluster'), ");
            query.append(" ('CreateDBInstance'), ");
            query.append(" ('DeleteDBInstance'), ");
            query.append(" ('CreateKey'), ");
            query.append(" ('CreateStream'), ");
            query.append(" ('DeleteStream'), ");
            query.append(" ('CreateLoadBalancer'), ");
            query.append(" ('DeleteLoadBalancer'), ");
            query.append(" ('CreateEnvironment'), ");
            query.append(" ('TerminateEnvironment'), ");
            query.append(" ('CreateTable'), ");
            query.append(" ('DeleteTable'), ");
            query.append(" ('RunInstances'), ");
            query.append(" ('StartInstances'), ");
            query.append(" ('StopInstances'), ");
            query.append(" ('TerminateInstances'), ");
            query.append(" ('CreateDistribution'), ");
            query.append(" ('DeleteDistribution'), ");
            query.append(" ('CreateStack'), ");
            query.append(" ('DeleteStack'), ");
            query.append(" ('CreateAutoScalingGroup'), ");
            query.append(" ('CreateLaunchConfiguration'), ");
            query.append(" ('DeleteAutoScalingGroup'), ");
            query.append(" ('DeleteLaunchConfiguration') ");
            DbManager.getInstance().doInsertUpdate(query.toString());
            
            
            StringBuilder truncateSecurity = new StringBuilder();
            truncateSecurity.append("TRUNCATE TABLE aws_security");
            DbManager.getInstance().doExecute(truncateSecurity.toString());
            
            StringBuilder query2 = new StringBuilder();
            query2.append("INSERT INTO aws_security (api_call) VALUES");
            query2.append(" ('CreateGroup'), ");
            query2.append(" ('CreateRole'), ");
            query2.append(" ('CreateUser'), ");
            query2.append(" ('DeleteGroup'), ");
            query2.append(" ('DeleteRole'), ");
            query2.append(" ('DeleteUser'), ");
            query2.append(" ('AttachGroupPolicy'), ");
            query2.append(" ('AttachRolePolicy'), ");
            query2.append(" ('PutGroupPolicy'), ");
            query2.append(" ('PutRolePolicy'), ");
            query2.append(" ('PutUserPolicy'), ");
            query2.append(" ('VerifyDomainIdentity'), ");
            query2.append(" ('VerifyEmailAddress'), ");
            query2.append(" ('AuthorizeSecurityGroupEgress'), ");
            query2.append(" ('AuthorizeSecurityGroupIngress'), ");
            query2.append(" ('RevokeSecurityGroupEgress'), ");
            query2.append(" ('RevokeSecurityGroupIngress'), ");
            query2.append(" ('AcceptVpcPeeringConnection') ");
            DbManager.getInstance().doInsertUpdate(query2.toString());
            
            String insertQuery = "UPDATE db_properties SET db_version = 5 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);

            currentVersion.setDbVersion(5);
        }        
    }
    
    final static void createVersion6(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 6) {
        
            StringBuilder addAcctColumn = new StringBuilder();
            addAcctColumn.append("ALTER TABLE aws_credentials ");
            addAcctColumn.append("ADD COLUMN aws_acct VARCHAR(15)");
            DbManager.getInstance().doExecute(addAcctColumn.toString());
            
            String insertQuery = "UPDATE db_properties SET db_version = 6 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);
            
            currentVersion.setDbVersion(6);
        }
    }
    
    final static void createVersion7(Connection conn, CurrentDbVersion currentVersion) {
        
        if (currentVersion.getDbVersion() < 7) {
        
            StringBuilder createVersionTable = new StringBuilder();
            createVersionTable.append("CREATE TABLE ctv_ignores ( ");
            createVersionTable.append("ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
            createVersionTable.append("ignore VARCHAR(50) )");
            DbManager.getInstance().doExecute(createVersionTable.toString());
            
            String insertQuery = "UPDATE db_properties SET db_version = 7 WHERE id = 1";
            DbManager.getInstance().doInsertUpdate(insertQuery);
            
            currentVersion.setDbVersion(6);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
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
            LOGGER.log(Level.WARNING, "Problem checking if table [" + tablename + "] exists", ex);
        }

        return doesTableExist;
    }
}
