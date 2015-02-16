package com.haskins.jcloudtrailerviewer.model;

/**
 *
 * @author mark
 */
public class EventDetail {
    
    private String label = "";
    private String detail = "";
    
    public EventDetail(String label, String detail) {
     
        this.label = label;
        this.detail = detail;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getDetail() {
        return detail;
    }
}

