package io.haskins.java.cloudtrailviewer.service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Unit test class for testing GeoService functionality
 *
 * Created by markhaskins on 05/01/2017.
 */
public class GeoServicesTests {

    private GeoService geoService;

    // This IP address belongs to AWS and should be constant
    private static final String IP_ADDRESS = "54.77.209.32";

    private static final String CITY = "Dublin";
    private static final String COUNTRY = "Ireland";
    private static final String CONTINENT = "Europe";

    @Before
    public void init() {
        geoService = new GeoService();
    }

    @Test
    public void retrieveCityFromIp() throws IOException {

        InetAddress ipAddress = InetAddress.getByName(IP_ADDRESS);

        try {
            CityResponse cityResponse = geoService.getCityResponse(ipAddress);
            String city = geoService.getCity(cityResponse);

            assertEquals(city, CITY);

        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void retrieveCountryFromIp() throws IOException {

        InetAddress ipAddress = InetAddress.getByName(IP_ADDRESS);

        try {
            CityResponse cityResponse = geoService.getCityResponse(ipAddress);
            String country = geoService.getCountry(cityResponse);

            assertEquals(country, COUNTRY);

        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void retrieveContinentFromIp() throws IOException {

        InetAddress ipAddress = InetAddress.getByName(IP_ADDRESS);

        try {
            CityResponse cityResponse = geoService.getCityResponse(ipAddress);
            String continent = geoService.getContinent(cityResponse);

            assertEquals(continent, CONTINENT);

        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
    }
}
