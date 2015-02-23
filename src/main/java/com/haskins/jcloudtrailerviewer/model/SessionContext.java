package com.haskins.jcloudtrailerviewer.model;

import java.util.Map;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author mark.haskins
 */
public class SessionContext {
    
    @JsonProperty("attributes")
    private Map attributes;

    @JsonProperty("sessionIssuer")
    private SessionIssuer sessionIssuer;
    
    /**
     * @return the attributes
     */
    public Map getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }
    
    /**
     * @return the sessionIssuer
     */
    public SessionIssuer getSessionIssuer() {
        return sessionIssuer;
    }

    /**
     * @param sessionIssuer the sessionIssuer to set
     */
    public void setSessionIssuer(SessionIssuer sessionIssuer) {
        this.sessionIssuer = sessionIssuer;
    }
}
