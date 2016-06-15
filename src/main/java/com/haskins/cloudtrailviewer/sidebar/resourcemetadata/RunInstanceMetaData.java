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
 * Class that provides a unique Chart menu for the RunInstance event
 * 
 * @author mark.haskins
 */
public class RunInstanceMetaData implements ResourceMetaData {
    
    private List<String> imageId = new ArrayList<String>();
    private List<String> instanceType = new ArrayList<String>();
    private List<String> az = new ArrayList<String>();
    
    private static final String[] MENU_ITEMS = new String[] {
        "Ami Id", 
        "Instance Type",
        "Availability Zone"
    };
    
    @Override
    public void populate(Event event) {

        Map responseParams = event.getResponseElements();

        if (responseParams != null && responseParams.containsKey("instancesSet")) {

            Map instancesSet = (Map)responseParams.get("instancesSet");
            List<Map> items = (List)instancesSet.get("items");

            for (Map item : items) {

                imageId.add((String)item.get("imageId"));
                instanceType.add((String)item.get("instanceType"));

                Map placement = (Map)item.get("placement");
                az.add((String)placement.get("availabilityZone"));
            }
        }

    }
    
    @Override
    public String[] getMenuItems() {
        return Arrays.copyOf(MENU_ITEMS, MENU_ITEMS.length);
    }
    
    @Override
    public List<String> getValuesForMenuItem(String menuItem) {

        List<String> values = new ArrayList<String>();

        if (menuItem.equalsIgnoreCase("Ami Id")) {
            values = this.imageId;
            
        } else if (menuItem.equalsIgnoreCase("Instance Type")) {
            values= this.instanceType;
            
        } else if (menuItem.equalsIgnoreCase("Availability Zone")) {
            values = this.az;
        }

        return values;
    }
}
