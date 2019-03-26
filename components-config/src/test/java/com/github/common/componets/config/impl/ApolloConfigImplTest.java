package com.github.common.componets.config.impl;

import com.github.common.componets.config.CommonConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

/**
 * apollo config测试
 */
@Ignore
public class ApolloConfigImplTest {
    private CommonConfig commonConfig;

    @Before
    public void setUp() {
        commonConfig = new ApolloConfigImpl("TEST1.common_datasource");
        Set<String> names = commonConfig.getPropertyNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Test
    public void getString() throws Exception {
        String userName = commonConfig.getString("username", "");
        Assert.assertEquals("common_w", userName);
    }

    //    @Test
    public void getInt() throws Exception {
        int timeout = commonConfig.getInt("kafka.poll.timeout.ms", 0);
        Assert.assertTrue(timeout == 100);
    }

}