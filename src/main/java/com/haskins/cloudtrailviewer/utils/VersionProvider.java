/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.haskins.cloudtrailviewer.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author mark.haskins
 */
public class VersionProvider {
 
    private static final Logger LOGGER = Logger.getLogger(VersionProvider.class.getName());
 
    private static final VersionProvider INSTANCE = new VersionProvider();
    private String version;
 
    private VersionProvider() {
        
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle("cloudtrailviewer.properties");
            version = rb.getString("application.version");
        } catch (MissingResourceException e) {
            LOGGER.warning("Resource bundle 'cloudtrailviewer.properties' was not found or error while reading current version.");
        }
    }
 
    public static String getVersion() {
        return INSTANCE.version;
    }
}