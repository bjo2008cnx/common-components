package com.github.common.components.cache.client.factory;

import com.github.common.components.cache.client.impl.RedisCacheClient;
import lombok.Data;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.Protocol;

@Data
public class RedisClientFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

  private int timeout = Protocol.DEFAULT_TIMEOUT;

  private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

  private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

  private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

  private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

  private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

  private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;

  private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

  private long timeBetweenEvictionRunsMillis =
      GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

  private int numTestsPerEvictionRun = GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

  private long minEvictableIdleTimeMillis =
      GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

  private long softMinEvictableIdleTimeMillis =
      GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

  private boolean lifo = GenericObjectPool.DEFAULT_LIFO;

  private byte whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;

  private RedisClientInfo redisClientInfo;

  private RedisSimplePool redisPool;

  private RedisCacheClient redisClient;

  @Override
  public void afterPropertiesSet() throws Exception {
    createJedisPool(redisClientInfo);
  }

  private void createJedisPool(RedisClientInfo redisClientInfo) {
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    if (this.maxIdle >= 0) {
      poolConfig.setMaxIdle(this.maxIdle);
    }
    poolConfig.setMaxWaitMillis(this.maxWait);
    poolConfig.setTestOnBorrow(this.testOnBorrow);
    poolConfig.setMinIdle(this.minIdle);
    poolConfig.setMaxTotal(this.maxActive);
    poolConfig.setTestOnReturn(this.testOnReturn);
    poolConfig.setTestWhileIdle(this.testWhileIdle);
    poolConfig.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
    poolConfig.setNumTestsPerEvictionRun(this.numTestsPerEvictionRun);
    poolConfig.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
    poolConfig.setSoftMinEvictableIdleTimeMillis(this.softMinEvictableIdleTimeMillis);
    poolConfig.setLifo(this.lifo);

    redisPool = new RedisSimplePool(redisClientInfo, poolConfig);
    redisClient = new RedisCacheClient(redisPool);
  }

  @Override
  public void destroy() throws Exception {
    redisPool.destoryAllResources();
    redisClientInfo = null;
  }

  @Override
  public Object getObject() throws Exception {
    return redisClient;
  }

  @Override
  public Class getObjectType() {
    return RedisCacheClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
