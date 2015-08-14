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
package com.haskins.cloudtrailviewer.model.resourcemetadata;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class RunInstanceMetaData implements ResourceMetaData {
    
    private String imageId;
    private String instanceType;
    private String az;
    
    private static final String[] MENU_ITEMS = new String[] {
        "Ami Id", 
        "Instance Type",
        "Availability Zone"
    };
    
    @Override
    public void populate(Event event) {
        
        Map requestParams = event.getRequestParameters();
        
        Map instancesSet = (Map)requestParams.get("instancesSet");
        List<Map> items = (List)instancesSet.get("items");
        Map item = items.get(0);
        imageId = (String)item.get("imageId");
        
        instanceType = (String)requestParams.get("instanceType");
        az = (String)requestParams.get("availabilityZone");
    }
    
    @Override
    public String[] getMenuItems() {
        return RunInstanceMetaData.MENU_ITEMS;
    }
    
    @Override
    public String getValueForMenuItem(String menuItem) {
        
        String value = "";
        
        if (menuItem.equalsIgnoreCase("Ami Id")) {
            value = this.imageId;
            
        } else if (menuItem.equalsIgnoreCase("Instance Type")) {
            value = this.instanceType;
            
        } else if (menuItem.equalsIgnoreCase("Availability Zone")) {
            value = this.az;
        }
        
        return value;
    }
}
