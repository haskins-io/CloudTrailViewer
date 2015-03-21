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

/**
 *
 * @author mark
 */
public class PropertiesSingleton {

    private static final String CONFIG_FILE = "config/config.cfg";
    
    private static PropertiesSingleton instance = null;

    private final Properties configProp = new Properties();

    private boolean configLoaded = false;

    /**
     * Returns an instance of the PropertiesSingleton class
     * @return 
     */
    public static PropertiesSingleton getInstance() {

        if (instance == null) {

            instance = new PropertiesSingleton();
            instance.loadConfigFile();
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

    /**
     * Returns a boolean if the config file has been loaded.
     * @return 
     */
    public boolean configLoaded() {
        return this.configLoaded;
    }

    private void loadConfigFile() {

        try {
            String path = "./" + CONFIG_FILE;
            FileInputStream fis = new FileInputStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            configProp.load(in);
            configLoaded = true;
        } catch (Exception e1) {
            
            Logger.getLogger(PropertiesSingleton.class.getName()).log(Level.WARNING, "No config file found");
            
            /**
             * This is here so I can find the file when running with within netbeans
             */
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream(CONFIG_FILE);

            try {
                configProp.load(in);
                configLoaded = true;
            } catch (Exception e2) {
                Logger.getLogger(PropertiesSingleton.class.getName()).log(Level.WARNING, "Still no config file found");
            }
        }
    }
}
