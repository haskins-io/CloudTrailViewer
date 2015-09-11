/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.requestInfo;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author mark.haskins
 */
public class SSSRequest extends AbstractRequest implements Request {

    public static final String S3_BUCKET = "Bucket name";
    
    public SSSRequest() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("bucketName", S3_BUCKET);
            }
        }); 
    }
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
                
        getTopLevelResource(S3_BUCKET, "bucketName", event, resources);
        getTopLevelParameters(event, resources, "bucketName");
    }
}