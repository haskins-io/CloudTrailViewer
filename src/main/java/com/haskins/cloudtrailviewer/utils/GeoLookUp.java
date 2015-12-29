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
public class GeoLookUp {
    
    private static final String GEO_FILE = "GeoLite2-City.mmdb";
    private static final Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    
    private static GeoLookUp instance = null;
    
    private DatabaseReader reader = null;
    
    private Map<String, Integer> latlngs = new HashMap<>();
        
    private GeoLookUp() {
        
        try {
            ClassLoader classLoader = GeoLookUp.class.getClassLoader();
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
    public static GeoLookUp getInstance() {

        if (instance == null) {
            instance = new GeoLookUp();  
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
                event.setCity(ci.getName());
                
                String latLng = location.getLatitude() + "," + location.getLongitude();
                
                if (!latlngs.containsKey(latLng)) {
                    latlngs.put(latLng, 1);
                }
                
                event.setLatLng(latLng);
                
            } catch (IOException | GeoIp2Exception ex) {
                Logger.getLogger(GeoLookUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Map<String, Integer> getCoords() {
        return latlngs;
    }
    
    private boolean isIp(String eventSource) {
        
        Matcher m = p.matcher(eventSource);
        return m.find();
    }
}
