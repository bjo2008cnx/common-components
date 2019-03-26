package com.github.common.components.auth.client;

import com.github.common.components.auth.common.WebSignUtil;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by lenovo on 2018/8/11.
 */
public class WebSignUtilTest extends TestCase {
    public void testSign() throws Exception {
        String sign = WebSignUtil.sign("appId.1", "3245LxT", null, String.valueOf(System.currentTimeMillis()));
        System.out.println(sign);
        Assert.assertNotNull(sign.length()>0);
    }

}