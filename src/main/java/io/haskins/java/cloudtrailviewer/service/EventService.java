/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.misc.TermStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * Service responsible for handling CloudTrail events.
 * <p>
 * Created by markhaskins on 04/01/2017.
 */
@Service
public class EventService extends LuceneDataService {


    public TermStats[] getTop(int top, String series) throws Exception {
        return LuceneUtils.getTopFromLucence(Event.TYPE, top, series);
    }

    String getLucenceDir() {
        return LuceneUtils.CLOUDTRAIL_DIR;
    }

    @Autowired
    public EventService(AccountService accountDao, GeoService geoService,
            StatusBarController statusBarController, AwsService awsService) {

        logger = Logger.getLogger("EventService");

        this.accountDao = accountDao;
        this.geoService = geoService;
        this.awsService = awsService;

        this.statusBarController = statusBarController;
    }

    public void processRecords(List<String> records, CompositeFilter filter, int requestType) {
        process(records, null, filter, requestType);
    }

    @Override
    void createDocument(Matcher matcher) { /* Not needed */ }

    void createDocument(Event e) {

        Document document = new Document();

        document.add(new StringField("timestamp", String.valueOf(e.getTimestamp()) , Field.Store.YES));
        document.add(new StringField("dateTime",  e.getEventTime() , Field.Store.YES));

        document.add(new StringField("eventTime", e.getEventTime() , Field.Store.YES));
        document.add(new StringField("eventVersion", e.getEventVersion() , Field.Store.YES));
        document.add(new StringField("eventSource", e.getEventSource() , Field.Store.YES));
        document.add(new StringField("eventName", e.getEventName() , Field.Store.YES));
        document.add(new StringField("awsRegion", e.getAwsRegion() , Field.Store.YES));
        document.add(new StringField("userAgent", e.getUserAgent() , Field.Store.YES));
        document.add(new StringField("errorCode", e.getErrorCode() , Field.Store.YES));
        document.add(new StringField("errorMessage", e.getErrorMessage() , Field.Store.YES));
        document.add(new StringField("sourceIPAddress", e.getSourceIPAddress() , Field.Store.YES));
        document.add(new StringField("requestID", e.getRequestId() , Field.Store.YES));
        document.add(new StringField("eventID", e.getEventId() , Field.Store.YES));
        document.add(new StringField("eventType", e.getEventType() , Field.Store.YES));
        document.add(new StringField("apiVersion", e.getApiVersion() , Field.Store.YES));
        document.add(new StringField("readOnly", e.getReadOnly() , Field.Store.YES));
        document.add(new StringField("recipientAccountId", e.getRecipientAccountId() , Field.Store.YES));
        document.add(new StringField("sharedEventID", e.getSharedEventID() , Field.Store.YES));
        document.add(new StringField("vpcEndpointId", e.getVpcEndpointId(), Field.Store.YES));

        if (e.getUserIdentity() != null) {
            document.add(new StringField("userIdentity.type", e.getUserIdentity().getType() , Field.Store.YES));
            document.add(new StringField("userIdentity.principalId", e.getUserIdentity().getPrincipalId() , Field.Store.YES));
            document.add(new StringField("userIdentity.arn", e.getUserIdentity().getArn() , Field.Store.YES));
            document.add(new StringField("userIdentity.accountId", e.getUserIdentity().getAccountId() , Field.Store.YES));
            document.add(new StringField("userIdentity.accessKeyId", e.getUserIdentity().getAccessKeyId() , Field.Store.YES));
            document.add(new StringField("userIdentity.userName", e.getUserIdentity().getUserName() , Field.Store.YES));
            document.add(new StringField("userIdentity.invokedBy", e.getUserIdentity().getInvokedBy(), Field.Store.YES));

            if (e.getUserIdentity().getSessionContext() != null && e.getUserIdentity().getSessionContext().getSessionIssuer() != null) {
                document.add(new StringField("userIdentity.sessionContext.sessionIssuer.type", e.getUserIdentity().getSessionContext().getSessionIssuer().getType() , Field.Store.YES));
                document.add(new StringField("userIdentity.sessionContext.sessionIssuer.principalId", e.getUserIdentity().getSessionContext().getSessionIssuer().getPrincipalId() , Field.Store.YES));
                document.add(new StringField("userIdentity.sessionContext.sessionIssuer.arn", e.getUserIdentity().getSessionContext().getSessionIssuer().getArn() , Field.Store.YES));
                document.add(new StringField("userIdentity.sessionContext.sessionIssuer.accountId", e.getUserIdentity().getSessionContext().getSessionIssuer().getAccountId() , Field.Store.YES));
                document.add(new StringField("userIdentity.sessionContext.sessionIssuer.userName", e.getUserIdentity().getSessionContext().getSessionIssuer().getUserName(), Field.Store.YES));
            }
        }

        geoService.populateGeoData(document, "cloudtrail");

        for (DataServiceListener l : listeners) {
            l.newEvent(document);
        }

        documents.add(document);
    }

    List<? extends AwsData> getDataDb() {
        return null;
    }


}
