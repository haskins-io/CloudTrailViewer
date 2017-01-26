package io.haskins.java.cloudtrailviewer.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service that handles Geo resolving of IP addresses to City / Country / Continent / Long and Lat
 *
 * Created by markhaskins on 05/01/2017.
 */
@Service
class GeoService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private static final String GEO_FILE = "geodata/GeoLite2-City.mmdb";
    private static final Pattern REGEX_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    private DatabaseReader reader = null;


    public GeoService() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            reader = new DatabaseReader.Builder(classLoader.getResourceAsStream(GEO_FILE)).build();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Failed to load GeoIp Database", ex);
        }
    }

    void populateGeoData(Event event) {

        if (reader != null && isIp(event.getSourceIPAddress())) {

            try {
                InetAddress ipAddress = InetAddress.getByName(event.getSourceIPAddress());

                CityResponse response = getCityResponse(ipAddress);

                event.setContinent(getContinent(response));
                event.setCountry(getCountry(response));

                String city = getCity(response);
                if (city == null) {
                    city = ipAddress.getHostAddress();
                }
                event.setCity(city);

                String latLng = getLatLong(response.getLocation());
                event.setLatLng(latLng);

            } catch (IOException | GeoIp2Exception ex) {
                LOGGER.log(Level.WARNING, "Failed to convert SourceIpAddress to Geo data", ex);
            }
        }
    }

    CityResponse getCityResponse(InetAddress ipAddress) throws GeoIp2Exception, IOException {
        return reader.city(ipAddress);
    }

    String getCity(CityResponse cityResponse) {
        return cityResponse.getCity().getName();
    }

    String getCountry(CityResponse cityResponse) {
        return cityResponse.getCountry().getName();
    }

    String getContinent(CityResponse cityResponse) {
        return cityResponse.getContinent().getName();
    }

    private String getLatLong(Location location) {
        return location.getLatitude() + "," + location.getLongitude();
    }

    private boolean isIp(String eventSource) {

        Matcher m = REGEX_PATTERN.matcher(eventSource);
        return m.find();
    }
}
