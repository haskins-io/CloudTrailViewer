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
public class Service {

    private static Service instance = null;

    private final Map<String, List<String>> services = new HashMap<>();

    public static Service getInstance() {

        if (instance == null) {
            instance = new Service();
        }

        return instance;
    }

    public String getFriendlyName(String name) {
        return null;
    }

    public List<String> getServices() {

        Set keys = services.keySet();
        
        List<String> list;
        if (keys.isEmpty()) {

            list = new ArrayList<>();
            
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("service_apis").getFile());
            if (file.isDirectory()) {

                File[] files = file.listFiles();
                for (File theFile : files) {

                    String filename = theFile.getName();
                    int pos = filename.indexOf(".");

                    String serviceName = filename.substring(0, pos);
                    list.add(serviceName);
                }
            }
            
        } else {
            list = new ArrayList(keys);
        }
        
        Collections.sort(list);

        return list;
    }

    public List<String> getApiCallsForService(String serviceName) {

        List<String> apis;

        if (services.containsKey(serviceName)) {
            apis = services.get(serviceName);

        } else {

            String filename = "service_apis/" + serviceName + ".txt";

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
            
            services.put(serviceName, apis);
        }

        return apis;
    }

    private Service() {

    }
}
