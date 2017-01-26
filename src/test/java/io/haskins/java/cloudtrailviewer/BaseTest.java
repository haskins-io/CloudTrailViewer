package io.haskins.java.cloudtrailviewer;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

/**
 * Abstract class that contains useful common test functionality
 *
 * Created by markhaskins on 06/01/2017.
 */
public abstract class BaseTest {

    public static void initToolkit() {

        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });
    }

    protected Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    protected Object getValueOfField(Object clazz, Field field) throws IllegalAccessException {

        field.setAccessible(true);
        return field.get(clazz);
    }

    protected DashboardWidget getTestWidget() {

        DashboardWidget w = new DashboardWidget();

        w.setWidget("TableWidget");
        w.setTitle("TableWidget Title");
        w.setType("Top");

        w.setStyle("Stacked");
        w.setOrientation("Vertical");

        w.setCategoryField("EventSource");
        w.setSeriesField("City");

        w.setXPos(1);
        w.setYPos(2);

        w.setWidth(640);
        w.setHeight(320);

        return w;
    }

    protected Event getCoreTestEvent() {

        Event e = new Event();

        e.setEventTime("2015-12-24T06:30:55Z");
        e.setEventVersion("1.03");
        e.setUserIdentity(null);
        e.setEventSource("ec2.amazonaws.com");
        e.setEventName("TerminateInstances");
        e.setAwsRegion("eu-west-1");
        e.setUserAgent("autoscaling.amazonaws.com");
        e.setErrorCode("");
        e.setErrorMessage("");
        e.setRequestParameters(null);
        e.setResponseElements(null);
        e.setAdditionalEventData(null);
        e.setSourceIPAddress("autoscaling.amazonaws.com");
        e.setRequestId("00034f13-a123-456a-78e9-e309dcd392e8");
        e.setEventId("8e9da4f8-123e-4567-8901-c018560827d6");
        e.setEventType("AwsApiCall");
        e.setApiVersion("");
        e.setReadOnly("");
        e.setRecipientAccountId("123456789012");
        e.setServiceEventDetails(null);
        e.setSharedEventID("");
        e.setVpcEndpointId("");

        EventUtils.addRawJson(e);

        return e;
    }
}
