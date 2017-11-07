package io.haskins.java.cloudtrailviewer.model.aws;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class AwsAccountTests {

    @Test
    public void constructorTest() {

        int id = 1;
        String name = "Test";
        String acctNum = "123456789012";
        String acctAlias = "TestAccount";
        String ctBucket = "ct-bucket";
        String elbBucket = "elb-bucket";
        String vpcBucket = "vpc-bucket";
        String key = "Key";
        String secret = "Secret";
        String prefix = "/root/";

        AwsAccount act = new AwsAccount(
                id,
                name,
                acctNum,
                acctAlias,
                ctBucket,
                elbBucket,
                vpcBucket,
                key,
                secret,
                prefix);

        assertEquals(act.getId(), id);
        assertEquals(act.getName(), name);
        assertEquals(act.getAcctNumber(), acctNum);
        assertEquals(act.getCtBucket(), ctBucket);
        assertEquals(act.getElbBucket(), elbBucket);
        assertEquals(act.getVpcBucket(), vpcBucket);
        assertEquals(act.getKey(), key);
        assertEquals(act.getSecret(), secret);
        assertEquals(act.getPrefix(), prefix);

    }

}
