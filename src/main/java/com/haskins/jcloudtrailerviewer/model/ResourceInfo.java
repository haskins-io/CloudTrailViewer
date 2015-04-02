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
package com.haskins.jcloudtrailerviewer.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public class ResourceInfo {
    
    private final List<String> types = new ArrayList<>();
    private final List<String> names = new ArrayList<>();

    /**
     * @return the type
     */
    public String getTypes() {
        return getResponse(types);
    }

    /**
     * @param type the type to set
     */
    public void addType(String type) {
        
        String theType = type;
        
        if (type.contains("Name")) {
            theType = type.replace("Name", "");
        } else if(type.contains("Names")) {
            theType = type.replace("Names", "");
        }
        
        this.types.add(theType);
    }

    /**
     * @return the name
     */
    public String getNames() {
        
        return getResponse(names);
    }

    /**
     * @param name the name to set
     */
    public void addName(String name) {
        this.names.add(name);
    }
    
    private String getResponse(List<String> aList) {
        
        String resourceNames;
        
        if (aList.size()>= 3) {
            
            String first = aList.get(0);
            int rest = aList.size() - 1;
            
            resourceNames = first + " and " + rest + " others";
            
        } else {
            resourceNames = aList.toString();
            resourceNames = resourceNames.substring(1, resourceNames.length() -1);
        }
        
        return resourceNames;
    }
}
