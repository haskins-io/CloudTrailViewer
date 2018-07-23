/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class GeoService {

    private final static Logger LOGGER = Logger.getLogger("GeoService");

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

    public void populateGeoData(AwsData data) {

        if (reader == null) {
            return;
        }

        String ip = "";

        if (data instanceof Event) {
            ip = ((Event)data).getSourceIPAddress();

        } else if (data instanceof ElbLog) {
            ip = ((ElbLog)data).getClientAddress();

        } else if (data instanceof VpcFlowLog) {
            ip = ((VpcFlowLog)data).getSrcaddr();

        } else {
            return;
        }

        if (!isIp(ip)) {
            return;
        }

        InetAddress ipAddress;
        CityResponse response;
        try {
            ipAddress = InetAddress.getByName(ip);
            response = getCityResponse(ipAddress);

        } catch (IOException | GeoIp2Exception e) {
            return;
        }

        String latLng = getLatLong(response.getLocation());

        data.setContinent(getContinent(response));
        data.setCountry(getCountry(response));
        data.setCity(getCity(response, ipAddress));
        data.setLatLng(latLng);
    }

    CityResponse getCityResponse(InetAddress ipAddress) throws GeoIp2Exception, IOException {
        return reader.city(ipAddress);
    }

    String getCity(CityResponse cityResponse, InetAddress ipAddress) {

        String city = cityResponse.getCity().getName();
        if (city == null) {
            city = ipAddress.getHostAddress();
        }

        return city;
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
