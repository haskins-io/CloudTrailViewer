package io.haskins.java.cloudtrailviewer.model.dao;

import io.haskins.java.cloudtrailviewer.BaseTest;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class CurrentDbVersionTests extends BaseTest {

    @Test
    public void setDbVersionTest() {

        CurrentDbVersion cdv = new CurrentDbVersion();

        int version = 1;

        cdv.setDbVersion(version);

        Class<?> testClass = cdv.getClass();

        try {

            Field f = getField(testClass, "dbVersion");
            f.setAccessible(true);
            f.get(cdv);

            Integer value = (Integer)getValueOfField(cdv, f);

            assertEquals(version, value.intValue());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
