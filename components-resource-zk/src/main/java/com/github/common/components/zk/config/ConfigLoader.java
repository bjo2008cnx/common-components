package com.github.common.components.zk.config;

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
   * 从apollo加载配置
   */
  public static ZkConfig load(String namespace) {
    //校验空值
    AssertUtil.assertNotEmpty(namespace, "apollo namespace");

    ZkConfig config = ApolloUtil.populate(namespace, ZkConfig.class);
    AssertUtil.assertNotEmpty(config, "apollo zookeeper config ");

    return config;
  }
}