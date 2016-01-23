/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.requestInfo;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class ResourceLookup {
        
    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static Map<String, Request> getServices() {
        
        return Collections.unmodifiableMap(new HashMap<String, Request>() {
            {
                put("autoscaling.amazonaws.com", new AsResource());
                put("cloudformation.amazonaws.com", new CfResource());
                put("cloudsearch.amazonaws.com", new CsResource());
                put("dynamodb.amazonaws.com", new DbResource());
                put("elasticbeanstalk.amazonaws.com", new EbResource());
                put("ec2.amazonaws.com", new Ec2Resource());
                put("elasticache.amazonaws.com", new EcResource());
                put("elasticloadbalancing.amazonaws.com", new ElbResoure());
                put("iam.amazonaws.com", new IamResource());
                put("kinesis.amazonaws.com", new KinesisResource());
                put("kms.amazonaws.com", new KmsResource());
                put("rds.amazonaws.com", new RdsResource());
                put("sns.amazonaws.com", new SnsResource());
                put("sqs.amazonaws.com", new SqsResource());
                put("swf.amazonaws.com", new SwfResource());
                put("s3.amazonaws.com", new SSSRequest());
            }
        });
    }
    
    public static void getResourceInfo(Event event, RequestInfo resources) {
        
        String source = event.getEventSource();
        
        if (getServices().containsKey(source)) {
            
            try {
                Request r = getServices().get(source);
                r.populateRequestInfo(event, resources);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Problem loading resource information", e);
            }

        }
    }
}
