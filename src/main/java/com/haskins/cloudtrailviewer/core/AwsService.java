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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author mark.haskins
 */
public class AwsService {

    private static AwsService instance = null;

    private final Map<String, String> serviceNamesToEndpoints = new HashMap<>();
    private final Map<String, String> serviceEndpointsToNames = new HashMap<>();
    
    private final Map<String, List<String>> serviceAPIs = new HashMap<>();

    public static AwsService getInstance() {

        if (instance == null) {
            instance = new AwsService();
        }

        return instance;
    }
    
    private AwsService() {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("service_apis/service_names.txt").getFile());
        
        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                
                String line = scanner.nextLine();
                String[] parts = line.split(":");
                
                serviceNamesToEndpoints.put(parts[1].trim(), parts[0].trim());
                serviceEndpointsToNames.put(parts[0].trim(), parts[1].trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFriendlyName(String name) {
        
        String friendlyName = serviceEndpointsToNames.get(name);
        if (friendlyName == null) {
            friendlyName = name;
        }
        
        return friendlyName;
    }

    public List<String> getServices() {
        
        Set keys = serviceNamesToEndpoints.keySet();
        List<String> list = new ArrayList(keys);      
        Collections.sort(list);

        return list;
    }

    public List<String> getApiCallsForService(String serviceName) {

        List<String> apis;

        String service = serviceNamesToEndpoints.get(serviceName);
        
        if (serviceAPIs.containsKey(service)) {
            apis = serviceAPIs.get(service);

        } else {

            String filename = "service_apis/" + service + ".txt";

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());

            apis = new ArrayList<>();
            
            try (Scanner scanner = new Scanner(file)) {

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    apis.add(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            serviceAPIs.put(service, apis);
        }

        return apis;
    }
}
