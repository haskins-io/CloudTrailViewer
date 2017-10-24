package io.haskins.java.cloudtrailviewer.model;

import com.google.gson.annotations.SerializedName;

public class AwsData {

    private long timestamp;
    @SerializedName(value="eventTime", alternate={"eventtime"}) private String eventTime;

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
}
