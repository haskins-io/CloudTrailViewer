package io.haskins.java.cloudtrailviewer.filter;

import io.haskins.java.cloudtrailviewer.BaseTest;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class to testing generic Filter functionality
 *
 * Created by markhaskins on 06/01/2017.
 */
public class AllFilterTests extends BaseTest {

    private Filter testFilter;

    @Before
    public void init() throws IOException {
        testFilter = new AllFilter();
    }

    @Test
    public void setNeedle() {

        String testValue = "TestNeedle";
        testFilter.setNeedle(testValue);

        Class<?> testClass = testFilter.getClass();

        try {

            Field f = getField(testClass, "needle");
            f.setAccessible(true);
            f.get(testFilter);

            String value = (String)getValueOfField(testFilter, f);

            assertEquals(testValue, value);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNeedle() {

        String testValue = "TestNeedle";
        Class<?> testClass = testFilter.getClass();

        try {

            Field f = getField(testClass, "needle");
            f.setAccessible(true);
            f.set(testFilter, testValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertEquals(testValue, testFilter.getNeedle());
    }

    @Test
    public void isNeedleSet() {

        String testValue = "TestNeedle";
        testFilter.setNeedle(testValue);

        assertTrue(testFilter.isNeedleSet());
    }

    @Test
    public void passesFilter() {

        String testValue = "autoscaling";
        testFilter.setNeedle(testValue);

        assertTrue(testFilter.passesFilter(getCoreTestEvent()));
    }
}
