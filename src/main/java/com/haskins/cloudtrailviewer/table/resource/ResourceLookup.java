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

package com.haskins.cloudtrailviewer.table.resource;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public class ResourceLookup {
    
    private final static AsResource autoScaling = new AsResource();
    private final static CfResource cloudFormation = new CfResource();
    private final static CsResource cloudSearch = new CsResource();
    private final static DbResource dynamoDb = new DbResource();
    private final static EbResource elasticBeanststalk = new EbResource();
    private final static Ec2Resource cloudCompute = new Ec2Resource();
    private final static EcResource elasticCache = new EcResource();
    private final static ElbResoure loadBalancing = new ElbResoure();
    private final static IamResource iam = new IamResource();
    private final static KinesisResource kinesis = new KinesisResource();
    private final static KmsResource kms = new KmsResource();
    private final static RdsResource relationalDb = new RdsResource();
    private final static SnsResource notificationService = new SnsResource();
    private final static SqsResource simpleQueue = new SqsResource();
    private final static SwfResource simpleWorkflow = new SwfResource();
    
    public static void getResourceInfo(Event event, RequestInfo resources) {
        
        String source = event.getEventSource();
        
        try {
            if (source.equalsIgnoreCase("autoscaling.amazonaws.com")) {
                autoScaling.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("cloudformation.amazonaws.com")) {
                cloudFormation.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("cloudsearch.amazonaws.com")) {
                cloudSearch.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("elasticbeanstalk.amazonaws.com")) {
                elasticBeanststalk.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("ec2.amazonaws.com")) {
                cloudCompute.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("elasticloadbalancing.amazonaws.com")) {
                loadBalancing.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("rds.amazonaws.com")) {
                relationalDb.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("sns.amazonaws.com")) {
                notificationService.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("dynamodb.amazonaws.com")) {
                dynamoDb.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("iam.amazonaws.com")) {
                iam.populateRequestInfo(event, resources);

            }else if (source.equalsIgnoreCase("elasticache.amazonaws.com")) {
                elasticCache.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("kinesis.amazonaws.com")) {
                kinesis.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("kms.amazonaws.com")) {
                kms.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("sqs.amazonaws.com")) {
                simpleQueue.populateRequestInfo(event, resources);

            } else if (source.equalsIgnoreCase("swf.amazonaws.com")) {
                simpleWorkflow.populateRequestInfo(event, resources);
            }
        } catch (Exception e) {
            // need to create a custom exception for here
        }

    }
}
