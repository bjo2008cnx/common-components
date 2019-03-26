package com.github.common.components.cache.client.impl;

import redis.clients.jedis.Jedis;

public interface JedisCallBack<T> {

  T doBiz(Jedis j);

  String getOperationName();
}
