package com.github.common.components.limiter.config;

import com.github.common.components.util.base.PopulateUtil;
import com.github.common.components.util.io.PropertiesUtil;
import com.github.common.components.util.lang.AssertUtil;
import com.github.common.componets.config.util.ApolloUtil;

import java.util.Properties;

/**
 * 配置加载器
 *
 * @author
 * @date 2018/9/15
 */
public class ConfigLoader {

  public static final String LIMITER = "limiter_";

  /**
   * 加载限流配置
   *
   * @param namespace
   * @return
   */
  public static LimiterConfig loadByApollo(String namespace) {
    // 从apollo拉取配置
    LimiterConfig config = ApolloUtil.populate(namespace, LIMITER, LimiterConfig.class);
    // 校验参数是否正确配置
    validate(config);

    return config;
  }

  /**
   * 从classpath加载配置
   *
   * @param fileName
   * @return
   */
  public static LimiterConfig loadByClassPath(String fileName) {
    Properties properties = PropertiesUtil.loadByClassPath(fileName);
    LimiterConfig config = PopulateUtil.map2Obj(properties, "limiter_", LimiterConfig.class);
    // 校验参数是否正确配置
    validate(config);
    return config;
  }

  static void validate(LimiterConfig config) {
    // 校验参数
    AssertUtil.assertNotEmpty(config.getIsLimiterEnabled(), "limiter_is_limiter_enabled");
    AssertUtil.assertNotEmpty(config.getPermitsPerSecond(), "limiter_permits_per_second");
    AssertUtil.assertNotEmpty(config.getAcquireTimeoutMs(), "limiter_acquire_timeout_ms");
  }
}
