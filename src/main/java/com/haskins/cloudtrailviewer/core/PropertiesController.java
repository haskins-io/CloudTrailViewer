/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.dialog.AwsAccount;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mark
 */
public class PropertiesController {
    
    private static PropertiesController instance = null;
    
    private final Preferences prefs = Preferences.userRoot().node("TrailSense");
    
    public static PropertiesController getInstance() {

        if (instance == null) {
            instance = new PropertiesController();
        }

        return instance;
    }
    
    public void setProperty(String key, String value) {
        prefs.put(key, value);
    }
    
    public String getProperty(String key) {
        return prefs.get(key, "");
    }
    
    public boolean checkS3Credentials() {
        
        boolean validS3Credentials = false;
        
        String keyRegex = "(?<![A-Z0-9])[A-Z0-9]{20}(?![A-Z0-9])";
        String secretRegex = "(?<![A-Za-z0-9/+=])[A-Za-z0-9/+=]{40}(?![A-Za-z0-9/+=])";
     
        Pattern keyPattern = Pattern.compile(keyRegex);
        Pattern secretPattern = Pattern.compile(secretRegex);
        
        boolean keyOK = false;
        if (getProperty(AwsAccount.S3_KEY_PROPERTY) != null) {
            Matcher keyMatcher = keyPattern.matcher(getProperty(AwsAccount.S3_KEY_PROPERTY));
            if (keyMatcher.matches()) {
                keyOK = true;
            }  
        }
        
        boolean secretOK = false;
        if (getProperty(AwsAccount.S3_SECRET_PROPERTY) != null) {
            Matcher secretMatcher = secretPattern.matcher(getProperty(AwsAccount.S3_SECRET_PROPERTY));
            if (secretMatcher.matches()) {
                secretOK = true;
            }
        }

        boolean bucketProvided = false;
        if (getProperty(AwsAccount.S3_BUCKET_PROPERTY) != null && getProperty(AwsAccount.S3_BUCKET_PROPERTY).length() >= 3) {
            bucketProvided = true;
        }
        
        if (keyOK && secretOK && bucketProvided) {
            validS3Credentials = true;
        }
        
        return validS3Credentials;
    }
    
}
