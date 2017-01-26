package io.haskins.java.cloudtrailviewer.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class DialogActionTests {

    private DialogAction da;

    @Before
    public void init() {
        da = new DialogAction();
    }

    @Test
    public void defaultConstructorTest() {

        assertEquals(da.getActionCode(), DialogAction.ACTION_CANCEL);
        assertNull(da.getActionPayload());
    }

    @Test
    public void constructorTest() {

        DashboardWidget test = new DashboardWidget();

        DialogAction dat = new DialogAction(DialogAction.ACTION_OK, test);

        assertEquals(dat.getActionCode(), DialogAction.ACTION_OK);
        assertSame(dat.getActionPayload(), test);
    }

    @Test
    public void setActionCodeTest() {

        int action_code = DialogAction.ACTION_OK;

        da.setActionCode(action_code);
        assertEquals(da.getActionCode(), DialogAction.ACTION_OK);

    }

    @Test
    public void setActionPayloadTest() {

        DashboardWidget test = new DashboardWidget();

        da.setActionPayload(test);
        assertSame(da.getActionPayload(), test);

    }
}
