
package com.haskins.cloudtrailviewer.table;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.text.SimpleDateFormat;

/**
 *
 * @author mark
 */
public class TableUtils {
    
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        
    /**
     * Returns the username for the event.
     * @param event Event to check
     * @return
     */
    public static String getUserName(Event event) {
        
        String username;
        
        if (event.getUserIdentity().getUserName() != null &&  event.getUserIdentity().getUserName().length() > 0) {
            
            String arn = event.getUserIdentity().getArn();
            int lastSlash = arn.lastIndexOf(":");
            username = arn.substring(lastSlash + 1);
            
        } else {
            String arn = event.getUserIdentity().getArn();
            int lastSlash = arn.lastIndexOf("/");
            username = arn.substring(lastSlash + 1);
        }
        
        return username;
    }
    
    public static String getService(Event event) {
        
        String source = event.getEventSource();
        int periodPos = source.indexOf(".");
        
        return source.substring(0,periodPos);
    }
    
    public static String getFormatedDateTime(long millis) {
        return sdf2.format(millis);
    }
}
