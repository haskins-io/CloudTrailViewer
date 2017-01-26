package io.haskins.java.cloudtrailviewer.model.aws;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class AwsAliasTests {

    @Test
    public void constructorTest() {

        String acctNum = "123456789012";
        String acctAlias = "Dev";

        AwsAlias alias = new AwsAlias(acctNum, acctAlias);

        assertEquals(alias.getAccountNumber(), acctNum);
        assertEquals(alias.getAccountAlias(), acctAlias);

    }
}
