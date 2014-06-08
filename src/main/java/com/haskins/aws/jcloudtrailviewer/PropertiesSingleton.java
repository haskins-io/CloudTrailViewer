package com.haskins.aws.jcloudtrailviewer;

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
    
    private void loadConfigFile() {
                
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream in = classloader.getResourceAsStream("config/config.cfg");
        
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        int i = 1;
    }
}
