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
package com.haskins.jcloudtrailerviewer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mark
 */
public class PropertiesSingleton {

    private static final String CONFIG_FILE = "config/config.cfg";
    
    private static PropertiesSingleton instance = null;

    private final Properties configProp = new Properties();
    
    private boolean validS3Credentials = false;

    /**
     * Returns an instance of the PropertiesSingleton class
     * @return 
     */
    public static PropertiesSingleton getInstance() {

        if (instance == null) {

            instance = new PropertiesSingleton();
            instance.loadConfigFile();
            instance.checkS3Credentials();
        }

        return instance;
    }

    /**
     * adds a property
     * @param key
     * @param value 
     */
    public void setProperty(String key, String value) {
        this.configProp.setProperty(key, value);
    }
    
    /**
     * returns a property based on a key
     * @param key
     * @return 
     */
    public String getProperty(String key) {

        return this.configProp.getProperty(key);
    }

    public boolean validS3Credentials() {
        return validS3Credentials;
    }
    
    private void loadConfigFile() {

        try {
            String path = "./" + CONFIG_FILE;
            FileInputStream fis = new FileInputStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            configProp.load(in);
        } catch (Exception e1) {
            
            Logger.getLogger(PropertiesSingleton.class.getName()).log(Level.WARNING, "No config file found");
            
            /**
             * This is here so I can find the file when running with within netbeans
             */
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream(CONFIG_FILE);

            try {
                configProp.load(in);
            } catch (Exception e2) {
                Logger.getLogger(PropertiesSingleton.class.getName()).log(Level.WARNING, "Still no config file found");
            }
        }
    }
    
    private void checkS3Credentials() {
        
        String keyRegex = "(?<![A-Z0-9])[A-Z0-9]{20}(?![A-Z0-9])";
        String secretRegex = "(?<![A-Za-z0-9/+=])[A-Za-z0-9/+=]{40}(?![A-Za-z0-9/+=])";
     
        Pattern keyPattern = Pattern.compile(keyRegex);
        Pattern secretPattern = Pattern.compile(secretRegex);
        
        boolean keyOK = false;
        if (getProperty("aws.key") != null) {
            Matcher keyMatcher = keyPattern.matcher(getProperty("aws.key"));
            if (keyMatcher.matches()) {
                keyOK = true;
            }  
        }
        
        boolean secretOK = false;
        if (getProperty("aws.secret") != null) {
            Matcher secretMatcher = secretPattern.matcher(getProperty("aws.secret"));
            if (secretMatcher.matches()) {
                secretOK = true;
            }
        }

        boolean bucketProvided = false;
        if (getProperty("aws.secret") != null && getProperty("aws.secret").length() >= 3) {
            bucketProvided = true;
        }
        
        if (keyOK && secretOK && bucketProvided) {
            validS3Credentials = true;
        }
    }
}
