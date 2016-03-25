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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * Utility Class for Extracting available API events per service from official
 * AWS NodeJs SDK.
 * 
 * @author mark
 */
class ApiExtractor {
    
    public static void main(String[] args) {
        
        String source = "/Users/mark.haskins/Temp/apis";
        String destination = "/Users/mark.haskins/Temp/output/";
        
        Map<String, String> serviceNames = new TreeMap<>();
        Map<String, List<String>> serviceAPIs = new TreeMap<>();
        
        ObjectMapper m = new ObjectMapper();
        
        File folder = new File(source);
        File[] listOfFiles = folder.listFiles();
        for (File f : listOfFiles) {
            
            String filename = f.getName();
            
            if (filename.contains("min")) {
                                
                try {
                    JsonNode rootNode = m.readTree(f.getAbsoluteFile());
                    JsonNode operationsNode = rootNode.get("operations");
                    JsonNode metaData = rootNode.get("metadata");
                    
                    JsonNode endpointNode = metaData.get("endpointPrefix");
                    String endpoint = endpointNode.asText();
                    JsonNode serviceNameNode = metaData.get("serviceFullName");
                    String serviceName = serviceNameNode.asText();
                    
                    serviceNames.put(endpoint, serviceName);
                    
                    List apis = serviceAPIs.get(endpoint);
                    if (apis == null) {
                        apis = new ArrayList();
                        serviceAPIs.put(endpoint, apis);
                    }
                    
                    Iterator<String> it = operationsNode.fieldNames();
                    while (it.hasNext()) {
                        
                        String api = it.next();
                        if (!apis.contains(api)) {
                            apis.add(api);
                        }
                        
                    }
                }
                catch (IOException ex) {
                    
                }
            }
        }
        
        /**
         * Create Service API files
         */
        for (Map.Entry<String, List<String>> entry : serviceAPIs.entrySet()) {
            
            String path = destination + entry.getKey() + ".txt";
            
            try (FileWriter fw = new FileWriter(path)) {
                
                for (String api : entry.getValue()) {
                    fw.write(api + "\r\n");
                }   
            } catch(IOException ioe) {
                
            }
        }
                
        /**
         * 
         */
        String path = destination + "service_names.txt";
        try (FileWriter fw = new FileWriter(path)) {

            Set<String> keys2 = serviceNames.keySet();
            for (String endpoint : keys2) {

                String friendly = serviceNames.get(endpoint);
                fw.write(endpoint + " : " + friendly + "\r\n");
            }
  
        } catch(IOException ioe) {
            
        }
        
    }
    
}