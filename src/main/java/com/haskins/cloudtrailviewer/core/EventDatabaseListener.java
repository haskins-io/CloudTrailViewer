/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public interface EventDatabaseListener {
    
    public void eventAdded(Event event);
}
