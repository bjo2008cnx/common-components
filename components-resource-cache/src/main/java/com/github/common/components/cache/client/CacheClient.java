package com.github.common.components.cache.client;

/**
 * CacheClient
 *
 * @date 2018/6/30
 */
public interface CacheClient {
  Boolean exists(int dbIndex, String key);

  String get(int dbIndex, String key);

  Object getObject(int dbIndex, String key);

  String set(int dbIndex, String key, Object object);

  Long del(int dbIndex, String key);

  String set(int dbIndex, String key, int ttl, Object object);

  String set(String key, Object value);

  Object getObject(String key);

  Long expire(String key, int ms);

  Long del(String key);

  Boolean exists(String key);

  String get(String key);
}
