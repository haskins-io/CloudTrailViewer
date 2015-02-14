package com.github.githublemming.jcloudtrailerviewer;

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
