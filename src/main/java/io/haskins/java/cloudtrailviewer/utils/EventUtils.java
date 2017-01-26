package io.haskins.java.cloudtrailviewer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by markhaskins on 05/01/2017.
 */
public class EventUtils  {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private final static DateFormatter DATE_FORMATTER = new DateFormatter();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Takes EventTime value from Event, converts it to a long and adds it back
     * onto the event
     * @param event Event to be proceessed
     */
    public static void addTimestamp(Event event) {
        event.setTimestamp(getTimestamp(event.getEventTime()));
    }

    /**
     * Converts passed String dateTime to long
     * @param dateString
     * @return
     */
    private static long getTimestamp(String dateString) {

        long millis = 0;

        try {
            millis = DATE_FORMATTER.convertStringToLong(dateString);
        } catch (ParseException ex) {
            LOGGER.log(Level.WARNING, "Problem convering String Date to long", ex);
        }

        return millis;
    }

    /**
     * Takes the passed Events and converts it to a JSON document and adds it to
     * the passed Event.
     * @param event
     */
    public static void addRawJson(Event event) {
        event.setRawJSON(GSON.toJson(event));
    }

    public static String getEventProperty(String property, Object event) {

        String requiredValue;

        if (property.contains(".")) {

            int pos = property.indexOf('.');
            String field = property.substring(0, pos);

            Object subClass = callMethod(field, event);
            if (subClass != null) {
                property = property.substring(pos + 1);
                return getEventProperty(property, subClass);
            } else {
                return null;
            }

        } else {

            requiredValue = (String) callMethod(property, event);
        }

        return requiredValue;
    }

    private static Object callMethod(String property, Object reflectionClass) {

        Object result;

        String camelCaseProperty = property.substring(0, 1).toUpperCase() + property.substring(1);

        try {
            String getProperty = "get" + camelCaseProperty;
            Method method = reflectionClass.getClass().getMethod(getProperty);
            result = method.invoke(reflectionClass);
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            result = null;
            LOGGER.log(Level.WARNING, "Problem using reflect to get property from object", ex);
        }

        return result;
    }
}
