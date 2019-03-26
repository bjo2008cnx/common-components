package com.github.common.components.zk.config;

import lombok.Data;

/**
 * ZkConfig
 *
 * @author
 * @date 2018/9/29
 */
@Data
public class ZkConfig {

  /**
   * 服务器地址
   */
  private String zkServers;

  /**
   * 命名空间
   */
  private String namespace;

  /**
   * 会话超时时间
   */
  private int sessionTimeoutMs;

  /**
   * 连接超时时间
   */
  private int connectionTimeoutMs;

  /**
   * 重试策略
   */
  private String retryPolicy;
}