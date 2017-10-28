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

import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
@Service
public class EventTableService {

    private List<EventTableServiceListener> eventListeners = new ArrayList<>();
    private List<EventTableServiceListener> vpcListeners = new ArrayList<>();
    private List<EventTableServiceListener> elbListeners = new ArrayList<>();

    public void addListener(EventTableServiceListener l, String type) {

        switch (type) {
            case "cloudtrail":
                eventListeners.add(l);
                break;
            case "vpclogs":
                vpcListeners.add(l);
                break;
            case "elblogs":
                elbListeners.add(l);
                break;
        }
    }

    public void setTableEvents(TopDocs results, String type) {

        List<EventTableServiceListener> listeners = null;

        switch (type) {
            case "cloudtrail":
                listeners = eventListeners;
                break;
            case "vpclogs":
                listeners = vpcListeners;
                break;
            case "elblogs":
                listeners = elbListeners;
                break;
        }

        if (listeners != null) {
            for (EventTableServiceListener l : listeners) {
                l.setEvents(results);
            }
        }
    }
}
