package io.haskins.java.cloudtrailviewer.model;

import com.google.gson.annotations.SerializedName;

public class AwsData {

    private long timestamp;
    @SerializedName(value="eventTime", alternate={"eventtime"}) private String eventTime;

    private String continent = null;
    private String country = null;
    private String city = null;
    private String latLng = null;

    public void setTimestamp(long millis) {
        this.timestamp = millis;
    }
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return the eventTime
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }


    public void setContinent(String continent) {
        this.continent = continent;
    }
    public String getContinent() {
        return this.continent;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return this.country;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return this.city;
    }

    public void setLatLng(String LatLong) {
        this.latLng = LatLong;
    }
    protected String getLatLng() {
        return this.latLng;
    }
}
