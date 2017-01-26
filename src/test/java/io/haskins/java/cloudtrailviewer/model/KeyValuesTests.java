package io.haskins.java.cloudtrailviewer.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class KeyValuesTests {

    private String fieldValue = "Test";
    private int countValue = 10;

    private KeyIntegerValue kv;

    @Before
    public void init() {
        kv = new KeyIntegerValue(fieldValue, countValue);
    }

    @Test
    public void constructorTests() {

        assertEquals(kv.getField(), fieldValue);
        assertEquals(kv.getCount(), countValue);
    }

    @Test
    public void setFieldTest() {

        String fieldValue = "Test2";
        kv.setField(fieldValue);
        assertEquals(kv.getField(), fieldValue);
    }

    @Test
    public void setCountTest() {

        int countValue = 100;
        kv.setCount(countValue);
        assertEquals(kv.getCount(), countValue);
    }
}
