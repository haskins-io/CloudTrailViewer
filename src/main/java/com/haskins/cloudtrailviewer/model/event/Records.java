package com.haskins.cloudtrailviewer.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Records {
    
    @JsonProperty("Records")
    private List<Event> logEvents;

    /**
     * @return the logEvents
     */
    public List<Event> getLogEvents() {
        return logEvents;
    }

    /**
     * @param logEvents the logEvents to set
     */
    public void setLogEvents(List<Event> logEvents) {
        this.logEvents = logEvents;
    }
}
