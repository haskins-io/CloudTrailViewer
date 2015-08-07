/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.utils;

import java.io.File;

/**
 *
 * @author mark.haskins
 */
public class ResourceTest {
    
    public static void main(String[] args) {
        
        ClassLoader classLoader = ResourceTest.class.getClassLoader();
        File file = new File(classLoader.getResource("service_apis").getFile());
        if (file.isDirectory()) {
            
            File[] files = file.listFiles();
            for (File theFile : files) {
                
                String filename = theFile.getName();
                int pos = filename.indexOf(".");
                
                String serviceName = filename.substring(0, pos);
                System.out.println(serviceName);
            }
        }
    }
}
