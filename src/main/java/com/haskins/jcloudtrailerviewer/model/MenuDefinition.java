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

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author mark.haskins
 */
public class MenuDefinition {
    
    @JsonProperty("Menu")
    private String menu;
    
    @JsonProperty("SubMenu")
    private String subMenu;
    
    @JsonProperty("Name")
    private String name;
        
    @JsonProperty("Actions")
    private List<String> actions;
    
    @JsonProperty("Property")
    private String property;
    
    @JsonProperty("Contains")
    private String contains;
    
    /**
     * @return the menu
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * @return the subMenu
     */
    public String getSubMenu() {
        return subMenu;
    }

    /**
     * @param subMenu the subMenu to set
     */
    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the actions
     */
    public List<String> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(List<String> actions) {
        this.actions = actions;
    }
    
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the contains
     */
    public String getContains() {
        return contains;
    }

    /**
     * @param contains the contains to set
     */
    public void setContains(String contains) {
        this.contains = contains;
    }
}
