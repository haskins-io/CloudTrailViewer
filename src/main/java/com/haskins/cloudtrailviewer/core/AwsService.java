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
package com.haskins.cloudtrailviewer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Singleton class that provides access to AWS Service names and AWS Service APIs
 * 
 * @author mark.haskins
 */
public class AwsService {

    private final Map<String, String> serviceNamesToEndpoints = new HashMap<>();
    private final Map<String, String> serviceEndpointsToNames = new HashMap<>();
    
    private final Map<String, List<String>> serviceAPIs = new HashMap<>();

    /**
     * Returns an instance of the class
     * @return 
     */
    public static AwsService getInstance() {
        return AwsServiceHolder.INSTANCE;
    }
    
    private AwsService() {

        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStreamReader io = new InputStreamReader(classLoader.getResourceAsStream("service_apis/service_names.txt"));
        
        try( BufferedReader br = new BufferedReader(io) ) {
            
            String line;
            while ((line = br.readLine()) != null) {
                
                String[] parts = line.split(":");
                
                serviceNamesToEndpoints.put(parts[1].trim(), parts[0].trim());
                serviceEndpointsToNames.put(parts[0].trim(), parts[1].trim());
            }
            
        } catch (IOException ioe) {
            
        }
    }

    /**
     * Returns the friendly name of a service for example autoscaling.amazonaws.com
     * would return AutoScaling
     * @param name service name
     * @return 
     */
    public String getFriendlyName(String name) {
        
        String friendlyName = serviceEndpointsToNames.get(name);
        if (friendlyName == null) {
            friendlyName = name;
        }
        
        return friendlyName;
    }

    /**
     * Returns the names of all available AWS Services
     * @return 
     */
    public List<String> getServices() {
        
        Set keys = serviceNamesToEndpoints.keySet();
        List<String> list = new ArrayList(keys);      
        Collections.sort(list);

        return list;
    }

    /**
     * Returns all Service API calls available for the given service name
     * @param serviceName name of service
     * @return 
     */
    public List<String> getApiCallsForService(String serviceName) {
        
        List<String> apis;

        String service = serviceNamesToEndpoints.get(serviceName);
        
        if (serviceAPIs.containsKey(service)) {
            apis = serviceAPIs.get(service);

        } else {

            String filename = "service_apis/" + service + ".txt";
            
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStreamReader io = new InputStreamReader(classLoader.getResourceAsStream(filename));

            apis = new ArrayList<>();
            
            try( BufferedReader br = new BufferedReader(io) ) {

                String line;
                while ((line = br.readLine()) != null) {

                    apis.add(line);
                }

            } catch (IOException ioe) {

            }
                        
            serviceAPIs.put(service, apis);
        }

        return apis;
    }
    
    private static class AwsServiceHolder {
        public static final AwsService INSTANCE = new AwsService();
    }
}
