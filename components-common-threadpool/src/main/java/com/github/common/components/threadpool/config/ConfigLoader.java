package com.github.common.components.threadpool.config;

import com.github.common.components.util.lang.AssertUtil;
import com.github.common.componets.config.util.ApolloUtil;

/**
 * 配置加载器
 *
 * @author
 * @date 2018/9/28
 */
public class ConfigLoader {

  /**
   * 默认队列大小
   */
  public static final int DEFAULT_QUEUE_SIZE = 10_000;

  /**
   * 从apollo加载配置
   */
  public static ThreadPoolConfig load(String namespace) {
    //校验空值
    AssertUtil.assertNotEmpty(namespace, "apollo namespace");

    ThreadPoolConfig config = ApolloUtil.populate(namespace, ThreadPoolConfig.class);
    AssertUtil.assertNotEmpty(config, "apollo ThreadPoolConfig ");

    //设置默认值
    config.setIsDeamon(config.getIsDeamon() == null ? Boolean.FALSE : config.getIsDeamon());
    config.setQueueSize(config.getQueueSize() == null ? DEFAULT_QUEUE_SIZE : config.getQueueSize());
    return config;
  }
}