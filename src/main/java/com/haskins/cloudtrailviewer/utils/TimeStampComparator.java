/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.utils;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Comparator;

/**
 *
 * @author mark
 */
public class TimeStampComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
       
        int comparisonResult = 0;
        
        Event event1 = (Event)o1;
        Event event2 = (Event)o2;
        
        if(event1.getTimestamp() < event2.getTimestamp())
        {
            comparisonResult = -1;
        }
        else if(event1.getTimestamp() > event2.getTimestamp())
        {
            comparisonResult = 1;
        }
        
        return comparisonResult;
    }
    
}
