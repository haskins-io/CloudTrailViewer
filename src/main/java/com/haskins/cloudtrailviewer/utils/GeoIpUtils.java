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

import com.haskins.cloudtrailviewer.model.event.Event;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mark.haskins
 */
public class GeoIpUtils {
    
    private static final String GEO_FILE = "GeoLite2-City.mmdb";
    private static final Pattern REGEX_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    
    private DatabaseReader reader = null;
    
    private Map<String, String> latlngs = new HashMap<>();
        
    private GeoIpUtils() {
        
        try {
            ClassLoader classLoader = GeoIpUtils.class.getClassLoader();
            File database = new File(classLoader.getResource(GEO_FILE).getFile());
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException ex) {
            System.out.println("Failed to load GeoIp Database");
        } catch (Exception ex) {
            System.out.println("Failed to load GeoIp Database");
        }
    }

    /**
     * Returns an instance of the class
     * @return 
     */
    public static GeoIpUtils getInstance() {
        return GeoIpUtilsHolder.INSTANCE;
    }
    
    public void populateGeoData(Event event) {
                
        if (reader != null && isIp(event.getSourceIPAddress())) {
            
            try {
                InetAddress ipAddress = InetAddress.getByName(event.getSourceIPAddress());
                
                CityResponse response = reader.city(ipAddress);
                
                Country co = response.getCountry();
                City ci = response.getCity();
                
                Location location = response.getLocation();
                String latLng = location.getLatitude() + "," + location.getLongitude();

                event.setCountry(co.getName());
                String city = ci.getName();
                if (city == null) {
                    city = ipAddress.getHostAddress();
                }
                event.setCity(city);
                event.setLatLng(latLng);
                
                if (!latlngs.containsKey(latLng)) {
                    latlngs.put(latLng, city);
                }
                
            } catch (IOException | GeoIp2Exception ex) {
                Logger.getLogger(GeoIpUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Map<String, String> getCoords() {
        return latlngs;
    }
    
    public void clear() {
        latlngs.clear();
    }
    
    private boolean isIp(String eventSource) {
        
        Matcher m = REGEX_PATTERN.matcher(eventSource);
        return m.find();
    }
    
    private static class GeoIpUtilsHolder {
        public static final GeoIpUtils INSTANCE = new GeoIpUtils();
    }
}
