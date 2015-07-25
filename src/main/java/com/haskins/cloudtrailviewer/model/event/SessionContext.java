package com.haskins.cloudtrailviewer.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;


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
    
    @JsonIgnore
    public DefaultMutableTreeNode getTree() {
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Session Context");
        
        if (getSessionIssuer() != null) {
            treeNode.add(sessionIssuer.getTree());
        }
        
        return treeNode;
    }
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getAttributes() != null) { modelData.append(getAttributes().toString()).append(", "); }
        if (getSessionIssuer() != null) { modelData.append(getSessionIssuer().toString()).append(", "); }
        
        return modelData.toString();
    }
}
