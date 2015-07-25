package com.haskins.cloudtrailviewer.model.resource;

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
