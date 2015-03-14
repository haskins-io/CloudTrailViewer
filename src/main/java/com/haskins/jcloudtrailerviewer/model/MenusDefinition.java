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
public class MenusDefinition {
    
    @JsonProperty("Menus")
    private List<MenuDefinition> menus;

    /**
     * @return the menus
     */
    public List<MenuDefinition> getMenus() {
        return menus;
    }

    /**
     * @param menus the menus to set
     */
    public void setMenus(List<MenuDefinition> menus) {
        this.menus = menus;
    }
}
