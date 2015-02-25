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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author mark
 */
public class PropertiesSingleton {
    
    private static PropertiesSingleton instance = null;
        
    private final Properties configProp = new Properties();
    
    private boolean configLoaded = false;
    
    public static PropertiesSingleton getInstance() {
     
        if (instance == null) {
            
            instance = new PropertiesSingleton();
            instance.loadConfigFile();
        }
        
        return instance;
    }
    
    public String getProperty(String key) {
        
        return this.configProp.getProperty(key);
    }
    
    public boolean configLoaded() {
        return this.configLoaded;
    }
    
    private void loadConfigFile() {
                
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream in = classloader.getResourceAsStream("config/config.cfg");
            
        try {
            configProp.load(in);
            configLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
