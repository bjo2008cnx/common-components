package com.github.common.components.cache.client.factory;

import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

@Slf4j
public class RedisSimplePool {

  private JedisPool jedisPool;

  public RedisSimplePool(RedisClientInfo redisClient, GenericObjectPoolConfig poolConfig) {
    if (StringUtil.isEmpty(redisClient.getPassword())) {
      jedisPool =
          new JedisPool(
              poolConfig,
              redisClient.getHost(),
              redisClient.getPort(),
              Protocol.DEFAULT_TIMEOUT,
              null,
              redisClient.getInstanceid());
    } else {
      jedisPool =
          new JedisPool(
              poolConfig,
              redisClient.getHost(),
              redisClient.getPort(),
              Protocol.DEFAULT_TIMEOUT,
              redisClient.getPassword(),
              redisClient.getInstanceid());
    }
  }

  public Jedis getResource() {
    return jedisPool.getResource();
  }

  public void returnResource(Jedis jedis) {
    jedisPool.returnResource(jedis);
  }

  public void destoryAllResources() {
    try {
      jedisPool.destroy();
    } catch (Exception e) {
      // log.error("redisPool destory error:", e);
    }
  }
}
