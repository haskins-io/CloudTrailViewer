package com.haskins.cloudtrailviewer.utils;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import java.io.IOException;
import java.net.InetAddress;
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
        
    private GeoLookUp() {
        
        try {
            ClassLoader classLoader = GeoLookUp.class.getClassLoader();
//            File database = new File(classLoader.getResource(GEO_FILE).getFile());
            
            reader = new DatabaseReader.Builder(classLoader.getResourceAsStream(GEO_FILE)).build();
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
                event.setLatLng(location.getLatitude() + "," + location.getLongitude());
                
            } catch (IOException | GeoIp2Exception ex) {
                Logger.getLogger(GeoLookUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean isIp(String eventSource) {
        
        Matcher m = p.matcher(eventSource);
        return m.find();
        
    }
    
//    public static void main(String[] args) {
//        
//        try {
//            ClassLoader classLoader = GeoTest.class.getClassLoader();
//            File database = new File(classLoader.getResource("GeoLite2-City.mmdb").getFile());
//            
//            // This creates the DatabaseReader object, which should be reused across
//            // lookups.
//            DatabaseReader reader = new DatabaseReader.Builder(database).build();
//            
//            InetAddress ipAddress = InetAddress.getByName("128.101.101.101");
//            
//            // Replace "city" with the appropriate method for your database, e.g.,
//            // "country".
//            CityResponse response = reader.city(ipAddress);
//            
//            Country country = response.getCountry();
//            System.out.println(country.getIsoCode());            // 'US'
//            System.out.println(country.getName());               // 'United States'
//            System.out.println(country.getNames().get("zh-CN")); // '美国'
//            
//            Subdivision subdivision = response.getMostSpecificSubdivision();
//            System.out.println(subdivision.getName());    // 'Minnesota'
//            System.out.println(subdivision.getIsoCode()); // 'MN'
//            
//            City city = response.getCity();
//            System.out.println(city.getName()); // 'Minneapolis'
//            
//            Postal postal = response.getPostal();
//            System.out.println(postal.getCode()); // '55455'
//            
//            Location location = response.getLocation();
//            System.out.println(location.getLatitude());  // 44.9733
//            System.out.println(location.getLongitude()); // -93.2323
//            
//        } catch (IOException ex) {
//            Logger.getLogger(GeoTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (GeoIp2Exception ex) {
//            Logger.getLogger(GeoTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
