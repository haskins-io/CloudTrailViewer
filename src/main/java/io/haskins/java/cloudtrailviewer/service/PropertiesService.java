package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by markhaskins on 11/02/2017.
 */
@Service
public class PropertiesService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private static final String PROP_FILE = "cloudtrailviewer.properties";

    private final Properties prop = new Properties();

    public PropertiesService() {

        ClassLoader cl = CloudTrailViewer.class.getClassLoader();
        try (InputStream input = cl.getResourceAsStream(PROP_FILE)) {

            if(input==null){
                System.out.println("Sorry, unable to find " + PROP_FILE);
                return;
            }

            prop.load(input);

        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Unable to load Properties from file", ioe);
        }

    }

    /**
     * returns a property
     * @param key Unique Id of the property
     * @return Value of Key
     */
    public String getProperty(String key) {

        return prop.getProperty(key);
    }
}
