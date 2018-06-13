package io.haskins.java.cloudtrailviewer.utils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class for testing GeoService functionality
 *
 * Created by markhaskins on 06/01/2017.
 */
public class DateFormatterTests {

    private final DateFormatter formatter = new DateFormatter();

    @Test
    public void convertStringToLongTest() {

        long expected = 1483264800000L;

        try {
            long result = formatter.convertStringToLong("2017-01-01T10:00:00Z");
            assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
