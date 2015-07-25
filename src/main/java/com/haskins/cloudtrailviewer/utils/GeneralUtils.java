/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.utils;

import java.io.IOException;
import java.util.Locale;

/**
 *
 * @author mark
 */
public class GeneralUtils {
    
    public enum OS {
        WINDOWS, UNIX, POSIX_UNIX, MAC, OTHER
    }
    
    private static OS os = OS.OTHER; 
    static {
        try {
            
            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }
            
            osName = osName.toLowerCase(Locale.ENGLISH);
            if (osName.contains("windows")) {
                os = OS.WINDOWS;
                
            } else if (osName.contains("linux") 
                    || osName.contains("mpe/ix")
                    || osName.contains("freebsd")
                    || osName.contains("irix")
                    || osName.contains("digital unix")
                    || osName.contains("unix")) {
                os = OS.UNIX;
                
            } else if (osName.contains("mac os x")) {
                os = OS.MAC;
                
            } else if (osName.contains("sun os")
                    || osName.contains("sunos")
                    || osName.contains("solaris")) {
                os = OS.POSIX_UNIX;
                
            } else if (osName.contains("hp-ux")
                    || osName.contains("aix")) {
                os = OS.POSIX_UNIX;
                
            } else {
                os = OS.OTHER;
            }
 
        } catch (Exception ex) {
            os = OS.OTHER;
        }
    }
     
    public static OS getOs() {
        return os;
    }
    
    public static boolean isMac() {
        boolean isMac = false;
        
        if (GeneralUtils.getOs().equals(GeneralUtils.OS.MAC)) {
            isMac = true;
        }
        
        return isMac;
    }
}