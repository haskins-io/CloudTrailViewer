package com.haskins.aws.jcloudtrailviewer.models;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

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
