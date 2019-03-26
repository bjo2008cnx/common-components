package com.github.common.components.cache.client.factory;

import lombok.Data;

@Data
public class RedisClientInfo {
  /** host */
  private String host;

  /** port */
  private int port;

  /** instanceid */
  private int instanceid;

  /** password */
  private String password;
}
