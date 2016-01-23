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
package com.haskins.cloudtrailviewer.model;

import java.io.Serializable;

/**
 *
 * @author mark
 */
public class Help implements Serializable {

    private static final long serialVersionUID = -2257108935128162644L;
    
    private final String helpTitle;
    private final String helpFilename;
    
    public Help(String title, String filename) {
        
        this.helpTitle = title;
        this.helpFilename = filename;
    }

    public String getHelpTitle() {
        return helpTitle;
    }

    public String getHelpFilename() {
        return helpFilename;
    }
}
