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
    private static final Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    
    private static GeoIpUtils instance = null;
    
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

        if (instance == null) {
            instance = new GeoIpUtils();  
        }

        return instance;
    }
    
    public void populateGeoData(Event event) {
                
        if (reader != null && isIp(event.getSourceIPAddress())) {
            
            try {
                InetAddress ipAddress = InetAddress.getByName(event.getSourceIPAddress());
                
                CityResponse response = reader.city(ipAddress);
                
                Country co = response.getCountry();
                City ci = response.getCity();
                
                Location location = response.getLocation();

                event.setCountry(co.getName());
                
                String city = ci.getName();
                if (city == null) {
                    city = ipAddress.getHostAddress();
                }
                event.setCity(city);
                
                String latLng = location.getLatitude() + "," + location.getLongitude();
                
                if (!latlngs.containsKey(latLng)) {
                    latlngs.put(latLng, city);
                }
                
                event.setLatLng(latLng);
                
            } catch (IOException | GeoIp2Exception ex) {
                Logger.getLogger(GeoIpUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Map<String, String> getCoords() {
        return latlngs;
    }
    
    private boolean isIp(String eventSource) {
        
        Matcher m = p.matcher(eventSource);
        return m.find();
    }
}
