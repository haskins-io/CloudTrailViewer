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

package com.haskins.cloudtrailviewer.sidebar.resourcemetadata;

import com.haskins.cloudtrailviewer.model.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class that provides a unique Chart menu for the CreateDbInstance event
 * 
 * @author mark.haskins
 */
public class CreateDbInstanceMetaData implements ResourceMetaData {
    
    private List<String> dBInstanceClass = new ArrayList<String>();
    private List<String> engine = new ArrayList<String>();
    private List<String> allocatedStorage = new ArrayList<String>();
    
    private static final String[] MENU_ITEMS = new String[] {
        "Instance Class", 
        "Engine",
        "Storage"
    };
    
    @Override
    public void populate(Event event) {
        
        Map requestParams = event.getRequestParameters();
                
        dBInstanceClass.add((String)requestParams.get("dBInstanceClass"));
        engine.add((String)requestParams.get("engine"));
        allocatedStorage.add((String)requestParams.get("allocatedStorage"));
    }
    
    @Override
    public String[] getMenuItems() {
        return Arrays.copyOf(MENU_ITEMS, MENU_ITEMS.length);
    }
    
    @Override
    public List<String> getValuesForMenuItem(String menuItem) {

        List<String> value = new ArrayList<String>();
        
        if (menuItem.equalsIgnoreCase("Instance Class")) {
            value = this.dBInstanceClass;
            
        } else if (menuItem.equalsIgnoreCase("Engine")) {
            value = this.engine;
            
        } else if (menuItem.equalsIgnoreCase("Storage")) {
            value = this.allocatedStorage;
        }
        
        return value;
    }
}
