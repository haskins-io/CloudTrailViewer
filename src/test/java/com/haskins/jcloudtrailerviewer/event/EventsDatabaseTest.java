/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class EventsDatabaseTest {
    
    /**
     * Test of clear method, of class EventsDatabase.
     */
    @Test
    public void testClear() {
        
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        events.add(event);
        
        EventsDatabase instance = new EventsDatabase();
        instance.newEvents(events);
        
        instance.clear();
        
        int expResult = 0;
        int result = instance.size();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class EventsDatabase.
     */
    @Test
    public void testSize() {

        Event event = new Event();
        List<Event> events = new ArrayList<>();
        events.add(event);
        
        EventsDatabase instance = new EventsDatabase();
        instance.newEvents(events);
        
        int expResult = 1;
        int result = instance.size();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getRecordByIndex method, of class EventsDatabase.
     */
    @Test
    public void testGetRecordByIndex() {
                
        Event event1 = new Event();
        event1.setEventName("Event One");
        Event event2 = new Event();
        event2.setEventName("Event Two");
        Event event3 = new Event();
        event3.setEventName("Event Three");
        
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
        
        EventsDatabase instance = new EventsDatabase();
        instance.newEvents(events);
        
        Event expResult = event2;
        Event result = instance.getRecordByIndex(1);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getEvents method, of class EventsDatabase.
     */
    @Test
    public void testGetEvents() {
        
        Event event1 = new Event();
        event1.setEventName("Event One");
        Event event2 = new Event();
        event2.setEventName("Event Two");
        Event event3 = new Event();
        event3.setEventName("Event Three");
        
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
        
        EventsDatabase instance = new EventsDatabase();
        instance.newEvents(events);
        
        List<Event> expResult = events;
        List<Event> result = instance.getEvents();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventsPerService method, of class EventsDatabase.
     */
    @Test
    public void testGetEventsPerService() {
        
        Event event1 = new Event();
        event1.setEventName("Event One");
        event1.setEventSource("ec2.amazonaws.com");
        
        Event event2 = new Event();
        event2.setEventName("Event Two");
        event2.setEventSource("iam.amazonaws.com");
        
        Event event3 = new Event();
        event3.setEventName("Event Three");
        event3.setEventSource("ec2.amazonaws.com");
        
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
                
        EventsDatabase instance = new EventsDatabase();
        instance.newEvents(events);
        
        // pause for a second as a thread is spawn to calculate this data
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        Map<String, Integer> result = instance.getEventsPerService();

        int ec2Expected = 2;
        int iamExpected = 1;
        
        int ec2Count = result.get("ec2");
        int iamCount = result.get("iam");
        
        assertEquals(ec2Expected, ec2Count);
        assertEquals(iamExpected, iamCount);
    }

    /**
     * Test of newEvents method, of class EventsDatabase.
     */
    @Test
    public void testNewEvents() {
        
        EventsDatabase instance = new EventsDatabase();
        
        Event event1 = new Event();
        event1.setEventName("Event One");
        Event event2 = new Event();
        
        List<Event> events1 = new ArrayList<>();
        events1.add(event1);
        
        instance.newEvents(events1);
        
        event2.setEventName("Event Two");
        Event event3 = new Event();
        event3.setEventName("Event Three");
        
        List<Event> events2 = new ArrayList<>();
        events2.add(event2);
        events2.add(event3);
        
        instance.newEvents(events2);
        
        int expResult = 3;
        int result = instance.size();
        
        assertEquals(expResult, result);
    }
    
}
