/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Utility Class for Extracting available API events per service from official
 * AWS NodeJs SDK.
 * 
 * @author mark
 */
public class ApiExtractor {
    
    public static void main(String[] args) {
        
        Map<String, List<String>> services = new HashMap<>();
        
        ObjectMapper m = new ObjectMapper();
        
        File folder = new File("/Users/mark/Projects/Java/NodeJsApiExtractor/src/main/resources/apis");
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles) {
            
            String filename = f.getName();
            
            if (filename.contains("min")) {
                
                int pos = filename.indexOf("-");
                
                String servicename = filename.substring(0,pos);
                
                List apis = services.get(servicename);
                if (apis == null) {
                    apis = new ArrayList();
                    services.put(servicename, apis);
                }
                
                try {
                    JsonNode rootNode = m.readTree(f.getAbsoluteFile());
                    JsonNode operationsNode = rootNode.get("operations");
                    
                    Iterator<String> it = operationsNode.fieldNames();
                    while (it.hasNext()) {
                        
                        String api = it.next();
                        if (!apis.contains(api)) {
                            apis.add(api);
                        }
                        
                    }
                }
                catch (IOException ex) {
                    Logger.getLogger(ApiExtractor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        Set<String> keys = services.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()) {
            
            String key = it.next();
            
            System.out.print("===========================================");
            System.out.print(key);
            System.out.println("===========================================");
            
            List<String> apis = services.get(key);
            for (String api : apis) {
                System.out.println(api);
            }
           
        }
    }
    
}