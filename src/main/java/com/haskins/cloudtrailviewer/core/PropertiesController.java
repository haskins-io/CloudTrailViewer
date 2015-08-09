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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class that stores and retrieves properties from OS provided Preferences implementation.
 * 
 * The class is a Singleton
 * 
 * @author mark
 */
public class PropertiesController {
            
    private static final String PROP_FILE = "cloudtrailviewer.properties";
    
    private static PropertiesController instance = null;
        
    private Properties prop = new Properties();
        
    private PropertiesController() {
        
        ClassLoader cl = PropertiesController.class.getClassLoader();
        try (InputStream input = cl.getResourceAsStream(PROP_FILE);) {
            
            if(input==null){
                    System.out.println("Sorry, unable to find " + PROP_FILE);
                return;
            }

            prop.load(input);
            
        } catch (IOException ioe) {
            
        }
    }
    
    /**
     * Returns an instance of the class
     * @return 
     */
    public static PropertiesController getInstance() {

        if (instance == null) {
            instance = new PropertiesController();  
        }

        return instance;
    }
        
    /**
     * returns a property
     * @param key Unique Id of the property
     * @return 
     */
    public String getProperty(String key) {
        
        return prop.getProperty(key);
        
    }
}
