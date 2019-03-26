package com.github.common.components.limiter.provider;

import com.github.common.components.limiter.config.ConfigLoader;
import com.github.common.components.limiter.config.LimiterConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** Created by lenovo on 2018/9/15. */
public class GuavaGlobalLimiterTest {
  private GlobalLimiter limiter;
  private LimiterConfig config;

  @Before
  public void setUp() throws Exception {
    // 从文件中加载
    config = ConfigLoader.loadByClassPath("limiter.properties");
    limiter = new GuavaGlobalLimiter(config);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void acquire() throws Exception {
    boolean result = limiter.acquire(1);
    Assert.assertTrue(result);
  }

  @Test
  public void isLimiterEnabled() throws Exception {
    Assert.assertTrue(config.getIsLimiterEnabled());
  }

  @Test
  public void acquire1() throws Exception {}
}
