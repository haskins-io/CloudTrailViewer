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
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
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

    void populateGeoData(Document document, String type) {

        if (reader == null) {
            return;
        }

        String ip = "";
        switch (type) {
            case "cloudtrail":
                ip = document.getField("sourceIPAddress").stringValue();
                break;
            case "vpclogs":
                ip = document.getField("srcaddr").stringValue();
                break;
            case "elblogs":
                ip = document.getField("clientAddress").stringValue();
                break;
        }

        GeoData source = getGeoData(ip);
        if (source != null) {
            document.add(new StringField("srcContinent", source.getContinent() , Field.Store.YES));
            document.add(new StringField("srcCountry", source.getCountry() , Field.Store.YES));
            document.add(new StringField("srcCity", source.getCity() , Field.Store.YES));
            document.add(new StringField("srcLatLng", source.getLatLng() , Field.Store.YES));
        }

        ip = "";
        switch (type) {
            case "vpclogs":
                ip = document.getField("dstaddr").stringValue();
                break;
            case "elblogs":
                ip = document.getField("backendAddress").stringValue();
                break;
        }

        GeoData destination = getGeoData(ip);
        if (destination != null) {
            document.add(new StringField("dstContinent", destination.getContinent() , Field.Store.YES));
            document.add(new StringField("dstCountry", destination.getCountry() , Field.Store.YES));
            document.add(new StringField("dstCity", destination.getCity() , Field.Store.YES));
            document.add(new StringField("dstLatLng", destination.getLatLng() , Field.Store.YES));
        }

    }

    private GeoData getGeoData(String ip) {

        if (!isIp(ip)) {
            return null;
        }

        InetAddress ipAddress;
        CityResponse response;
        try {
            ipAddress = InetAddress.getByName(ip);
            response = getCityResponse(ipAddress);

        } catch (IOException | GeoIp2Exception e) {
            return null;
        }

        String latLng = getLatLong(response.getLocation());
        GeoData data = new GeoData();

        data.setContinent(getContinent(response));
        data.setCountry(getCountry(response));
        data.setCity(getCity(response, ipAddress));
        data.setLatLng(latLng);

        return data;
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

    class GeoData {

        private String city = "";
        private String country = "";
        private String continent = "";
        private String latLng = "";

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getContinent() {
            return continent;
        }

        public void setContinent(String continent) {
            this.continent = continent;
        }

        public String getLatLng() {
            return latLng;
        }

        public void setLatLng(String lanLog) {
            this.latLng = lanLog;
        }
    }
}
