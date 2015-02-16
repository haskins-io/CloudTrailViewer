package com.haskins.jcloudtrailerviewer.util;

/**
 *
 * @author mark
 */
public class JvmUtils {
    
    public static long getMemoryUsed() {
        
        System.gc();
        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        
        return usedMB;
    }
    
}
