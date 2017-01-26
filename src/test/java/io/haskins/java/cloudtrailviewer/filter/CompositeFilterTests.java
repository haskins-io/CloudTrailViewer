package io.haskins.java.cloudtrailviewer.filter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by markhaskins on 08/01/2017.
 */
public class CompositeFilterTests {

    private CompositeFilter filters = new CompositeFilter();

    @Before
    public void init() throws IOException {
        filters = new CompositeFilter();
    }

    @Test
    public void addFilterTest() {

        filters.addFilter(new AllFilter());
        assertEquals(1, filters.size());
    }

    @Test
    public void removeFilter() {

        Filter test = new AllFilter();

        filters.addFilter(test);
        assertEquals(1, filters.size());

        filters.removeFilter(test);
        assertEquals(0, filters.size());
    }

    @Test
    public void setMode() {

        filters.setMode(CompositeFilter.BITWISE_OR);
        assertEquals(CompositeFilter.BITWISE_OR, filters.getMode());
    }

    @Test
    public void allFiltersConfigured_NO() {

        filters.addFilter(new AllFilter());
        assertFalse(filters.allFiltersConfigured());
    }

    @Test
    public void allFiltersConfigured_YES() {

        Filter test = new AllFilter();
        test.setNeedle("SomeNeeded");

        filters.addFilter(test);
        assertTrue(filters.allFiltersConfigured());
    }
}
