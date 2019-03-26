package com.github.common.components.cache.client.impl;

import com.github.common.components.cache.client.CacheClient;
import com.github.common.components.cache.client.exception.RedisException;
import com.github.common.components.cache.client.factory.RedisSimplePool;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.SafeEncoder;

@Slf4j
public class RedisCacheClient implements CacheClient, JedisCommands {

  private RedisSimplePool redisPool;

  public RedisCacheClient(RedisSimplePool redisPool) {
    this.redisPool = redisPool;
  }

  public <E> E doOperation(String key, JedisCallBack<E> callback) {
    return this.doOperation(SafeEncoder.encode(key), callback);
  }

  public <E> E doOperation(byte[] key, JedisCallBack<E> callback) {
    Jedis jedis = null;
    try {
      jedis = redisPool.getResource();
      E rs = callback.doBiz(jedis);
      return rs;
    } catch (JedisConnectionException e) {
      throw new RedisException("Faild when execute operation  " + callback.getOperationName(), e);
    } catch (Exception e1) {
      throw new RuntimeException("Do operation failed for key :=" + key, e1);
    } finally {
      if (null != redisPool && null != jedis) {
        redisPool.returnResource(jedis);
      }
    }
  }

  public String setBatch(final int DBIndex, final Map<String, String> values) {
    return doOperation(
        "batch",
        new JedisCallBack<String>() {
          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            Pipeline pipeline = jedis.pipelined();
            Set<String> keys = values.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
              String key = iterator.next();
              pipeline.set(key, values.get(key));
            }
            pipeline.sync();
            return values.toString();
          }

          public String getOperationName() {
            return "setBatch";
          }
        });
  }

  public Map<String, String> getBatch(final int DBIndex, final List<String> keys) {
    return doOperation(
        "batch",
        new JedisCallBack<Map<String, String>>() {

          public Map<String, String> doBiz(Jedis jedis) {
            Map<String, String> result = new HashMap<String, String>();
            Map<String, Response<String>> resultR = new HashMap();
            jedis.select(DBIndex);
            Pipeline pipeline = jedis.pipelined();
            for (int i = 0; i < keys.size(); i++) {
              resultR.put(keys.get(i), pipeline.get(keys.get(i)));
            }
            pipeline.sync();
            Set<String> sets = resultR.keySet();
            Iterator<String> iterator = sets.iterator();
            while (iterator.hasNext()) {
              String key = iterator.next();
              result.put(key, resultR.get(key).get());
            }
            return result;
          }

          public String getOperationName() {
            return "getBatch";
          }
        });
  }

  public String hsetBatch(final int DBIndex, final String key, final Map<String, String> values) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            Pipeline pipeline = jedis.pipelined();
            Set<String> keys = values.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
              String fiedlKey = iterator.next();
              pipeline.hset(key, fiedlKey, values.get(key));
            }
            pipeline.sync();
            return values.toString();
          }

          public String getOperationName() {
            return "hsetBatch";
          }
        });
  }

  public String set(final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.set(key, value);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public String get(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.get(key);
          }

          public String getOperationName() {
            return "get";
          }
        });
  }

  public Boolean exists(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.exists(key);
          }

          public String getOperationName() {
            return "echo";
          }
        });
  }

  public String type(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.type(key);
          }

          public String getOperationName() {
            return "type";
          }
        });
  }

  @Override
  public Long expire(final String key, final int seconds) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.expire(key, seconds);
          }

          public String getOperationName() {
            return "expire";
          }
        });
  }

  public Long expireAt(final String key, final long unixTime) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.expireAt(key, unixTime);
          }

          public String getOperationName() {
            return "expireAt";
          }
        });
  }

  public Long ttl(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.ttl(key);
          }

          public String getOperationName() {
            return "ttl";
          }
        });
  }

  public Boolean setbit(final String key, final long offset, final boolean value) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.setbit(key, offset, value);
          }

          public String getOperationName() {
            return "setbit";
          }
        });
  }

  public Boolean setbit(final String key, final long offset, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.setbit(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
          }

          public String getOperationName() {
            return "setbit";
          }
        });
  }

  public Boolean getbit(final String key, final long offset) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.getbit(key, offset);
          }

          public String getOperationName() {
            return "getbit";
          }
        });
  }

  public Long setrange(final String key, final long offset, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.setrange(key, offset, value);
          }

          public String getOperationName() {
            return "setrange";
          }
        });
  }

  public String getrange(final String key, final long startOffset, final long endOffset) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.getrange(key, startOffset, endOffset);
          }

          public String getOperationName() {
            return "getrange";
          }
        });
  }

  public String getSet(final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.getSet(key, value);
          }

          public String getOperationName() {
            return "getSet";
          }
        });
  }

  public Long setnx(final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.setnx(key, value);
          }

          public String getOperationName() {
            return "setnx";
          }
        });
  }

  public String setex(final String key, final int seconds, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.setex(key, seconds, value);
          }

          public String getOperationName() {
            return "setex";
          }
        });
  }

  public Long decrBy(final String key, final long integer) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.decrBy(key, integer);
          }

          public String getOperationName() {
            return "decrBy";
          }
        });
  }

  public Long decr(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.decr(key);
          }

          public String getOperationName() {
            return "decr";
          }
        });
  }

  public Long incrBy(final String key, final long integer) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.incrBy(key, integer);
          }

          public String getOperationName() {
            return "incrBy";
          }
        });
  }

  public Long incr(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.incr(key);
          }

          public String getOperationName() {
            return "incr";
          }
        });
  }

  public Long append(final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.append(key, value);
          }

          public String getOperationName() {
            return "append";
          }
        });
  }

  public String substr(final String key, final int start, final int end) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.substr(key, start, end);
          }

          public String getOperationName() {
            return "substr";
          }
        });
  }

  public Long hset(final String key, final String field, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.hset(key, field, value);
          }

          public String getOperationName() {
            return "hset";
          }
        });
  }

  public String hget(final String key, final String field) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.hget(key, field);
          }

          public String getOperationName() {
            return "hget";
          }
        });
  }

  public Long hsetnx(final String key, final String field, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.hsetnx(key, field, value);
          }

          public String getOperationName() {
            return "hsetnx";
          }
        });
  }

  public String hmset(final String key, final Map<String, String> hash) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.hmset(key, hash);
          }

          public String getOperationName() {
            return "hmset";
          }
        });
  }

  public List<String> hmget(final String key, final String... fields) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.hmget(key, fields);
          }

          public String getOperationName() {
            return "hmget";
          }
        });
  }

  public Long hincrBy(final String key, final String field, final long value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.hincrBy(key, field, value);
          }

          public String getOperationName() {
            return "hincrBy";
          }
        });
  }

  public Boolean hexists(final String key, final String field) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.hexists(key, field);
          }

          public String getOperationName() {
            return "hexists";
          }
        });
  }

  public Long del(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.del(key);
          }

          public String getOperationName() {
            return "del";
          }
        });
  }

  public Long hdel(final String key, final String... fields) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.hdel(key, fields);
          }

          public String getOperationName() {
            return "hdel";
          }
        });
  }

  public Long hlen(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.hlen(key);
          }

          public String getOperationName() {
            return "hlen";
          }
        });
  }

  public Set<String> hkeys(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.hkeys(key);
          }

          public String getOperationName() {
            return "hkeys";
          }
        });
  }

  public List<String> hvals(final String key) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.hvals(key);
          }

          public String getOperationName() {
            return "hvals";
          }
        });
  }

  public Map<String, String> hgetAll(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Map<String, String>>() {

          public Map<String, String> doBiz(Jedis jedis) {
            return jedis.hgetAll(key);
          }

          public String getOperationName() {
            return "hgetAll";
          }
        });
  }

  public Long rpush(final String key, final String... strings) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.rpush(key, strings);
          }

          public String getOperationName() {
            return "rpush";
          }
        });
  }

  public Long lpush(final String key, final String... strings) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.lpush(key, strings);
          }

          public String getOperationName() {
            return "lpush";
          }
        });
  }

  public Long lpushx(final String key, final String string) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.lpushx(key, string);
          }

          public String getOperationName() {
            return "lpushx";
          }
        });
  }

  public Long strlen(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.strlen(key);
          }

          public String getOperationName() {
            return "strlen";
          }
        });
  }

  public Long move(final String key, final int dbIndex) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.move(key, dbIndex);
          }

          public String getOperationName() {
            return "move";
          }
        });
  }

  public Long rpushx(final String key, final String string) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.rpushx(key, string);
          }

          public String getOperationName() {
            return "rpushx";
          }
        });
  }

  public Long persist(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.persist(key);
          }

          public String getOperationName() {
            return "persist";
          }
        });
  }

  public Long llen(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.llen(key);
          }

          public String getOperationName() {
            return "llen";
          }
        });
  }

  public List<String> lrange(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.lrange(key, start, end);
          }

          public String getOperationName() {
            return "lrange";
          }
        });
  }

  public String ltrim(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.ltrim(key, start, end);
          }

          public String getOperationName() {
            return "ltrim";
          }
        });
  }

  public String lindex(final String key, final long index) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.lindex(key, index);
          }

          public String getOperationName() {
            return "lindex";
          }
        });
  }

  public String lset(final String key, final long index, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.lset(key, index, value);
          }

          public String getOperationName() {
            return "lset";
          }
        });
  }

  public Long lrem(final String key, final long count, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.lrem(key, count, value);
          }

          public String getOperationName() {
            return "lrem";
          }
        });
  }

  public String lpop(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.lpop(key);
          }

          public String getOperationName() {
            return "lpop";
          }
        });
  }

  public String rpop(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.rpop(key);
          }

          public String getOperationName() {
            return "rpop";
          }
        });
  }

  public Long sadd(final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.sadd(key, members);
          }

          public String getOperationName() {
            return "sadd";
          }
        });
  }

  public Set<String> smembers(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.smembers(key);
          }

          public String getOperationName() {
            return "smembers";
          }
        });
  }

  public Long srem(final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.srem(key, members);
          }

          public String getOperationName() {
            return "srem";
          }
        });
  }

  public String spop(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.spop(key);
          }

          public String getOperationName() {
            return "spop";
          }
        });
  }

  public Long scard(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.scard(key);
          }

          public String getOperationName() {
            return "scard";
          }
        });
  }

  public Boolean sismember(final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            return jedis.sismember(key, member);
          }

          public String getOperationName() {
            return "sismember";
          }
        });
  }

  public String srandmember(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.srandmember(key);
          }

          public String getOperationName() {
            return "srandmember";
          }
        });
  }

  public Long zadd(final String key, final double score, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zadd(key, score, member);
          }

          public String getOperationName() {
            return "zadd";
          }
        });
  }

  public Long zadd(final String key, final Map<String, Double> scoreMembers) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zadd(key, scoreMembers);
          }

          public String getOperationName() {
            return "zadd";
          }
        });
  }

  public Set<String> zrange(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrange(key, start, end);
          }

          public String getOperationName() {
            return "zrange";
          }
        });
  }

  public Long zrem(final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zrem(key, members);
          }

          public String getOperationName() {
            return "zrem";
          }
        });
  }

  public Double zincrby(final String key, final double score, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Double>() {

          public Double doBiz(Jedis jedis) {
            return jedis.zincrby(key, score, member);
          }

          public String getOperationName() {
            return "zincrby";
          }
        });
  }

  public Long zrank(final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zrank(key, member);
          }

          public String getOperationName() {
            return "zrank";
          }
        });
  }

  public Long zrevrank(final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zrevrank(key, member);
          }

          public String getOperationName() {
            return "zrevrank";
          }
        });
  }

  public Set<String> zrevrange(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrevrange(key, start, end);
          }

          public String getOperationName() {
            return "zrevrange";
          }
        });
  }

  public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrangeWithScores(key, start, end);
          }

          public String getOperationName() {
            return "zrangeWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrevrangeWithScores(key, start, end);
          }

          public String getOperationName() {
            return "zrevrangeWithScores";
          }
        });
  }

  public Long zcard(final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zcard(key);
          }

          public String getOperationName() {
            return "zcard";
          }
        });
  }

  public Double zscore(final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Double>() {

          public Double doBiz(Jedis jedis) {
            return jedis.zscore(key, member);
          }

          public String getOperationName() {
            return "zscore";
          }
        });
  }

  public List<String> sort(final String key) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.sort(key);
          }

          public String getOperationName() {
            return "sort";
          }
        });
  }

  public List<String> sort(final String key, final SortingParams sortingParameters) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.sort(key, sortingParameters);
          }

          public String getOperationName() {
            return "sort";
          }
        });
  }

  public Long zcount(final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zcount(key, min, max);
          }

          public String getOperationName() {
            return "zcount";
          }
        });
  }

  public Long zcount(final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zcount(key, min, max);
          }

          public String getOperationName() {
            return "zcount";
          }
        });
  }

  public Set<String> zrangeByScore(final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrangeByScore(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScore(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<String> zrangeByScore(
      final String key, final double min, final double max, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrangeByScore(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final String key, final double max, final double min, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrangeByScoreWithScores(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final String key, final double max, final double min) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {

            return jedis.zrevrangeByScoreWithScores(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final String key, final double min, final double max, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final String key, final double max, final double min, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<String> zrangeByScore(final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrangeByScore(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScore(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<String> zrangeByScore(
      final String key, final String min, final String max, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrangeByScore(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final String key, final String max, final String min, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrangeByScoreWithScores(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final String key, final String max, final String min) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScoreWithScores(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final String key, final String min, final String max, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final String key, final String max, final String min, final int offset, final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Long zremrangeByRank(final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zremrangeByRank(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByRank";
          }
        });
  }

  public Long zremrangeByScore(final String key, final double start, final double end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zremrangeByScore(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByScore";
          }
        });
  }

  public Long zremrangeByScore(final String key, final String start, final String end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.zremrangeByScore(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByScore";
          }
        });
  }

  public Long linsert(
      final String key, final LIST_POSITION where, final String pivot, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            return jedis.linsert(key, where, pivot, value);
          }

          public String getOperationName() {
            return "linsert";
          }
        });
  }

  /**
   * ******** EXT API ***************
   */
  public String set(final String key, final Object value) {

    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            byte[] byteValue = serialize(value);
            return jedis.set(SafeEncoder.encode(key), byteValue);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public Object getObject(final String key) {

    return doOperation(
        key,
        new JedisCallBack<Object>() {

          public Object doBiz(Jedis jedis) {
            byte[] result = jedis.get(SafeEncoder.encode(key));
            Object object = deserialize(result);
            return object;
          }

          public String getOperationName() {
            return "getObject";
          }
        });
  }

  public String info(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.info(key);
          }

          public String getOperationName() {
            return "info";
          }
        });
  }

  public String set(final int DBIndex, final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.set(key, value);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public String set(final int DBIndex, final String key, final int seconds, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setex(key, seconds, value);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public String get(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.get(key);
          }

          public String getOperationName() {
            return "get";
          }
        });
  }

  @Override
  public Boolean exists(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.exists(key);
          }

          public String getOperationName() {
            return "echo";
          }
        });
  }

  public String type(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.type(key);
          }

          public String getOperationName() {
            return "type";
          }
        });
  }

  public Long expire(final int DBIndex, final String key, final int seconds) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.expire(key, seconds);
          }

          public String getOperationName() {
            return "expire";
          }
        });
  }

  public Long expireAt(final int DBIndex, final String key, final long unixTime) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.expireAt(key, unixTime);
          }

          public String getOperationName() {
            return "expireAt";
          }
        });
  }

  public Long ttl(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.ttl(key);
          }

          public String getOperationName() {
            return "ttl";
          }
        });
  }

  public Boolean setbit(
      final int DBIndex, final String key, final long offset, final boolean value) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setbit(key, offset, value);
          }

          public String getOperationName() {
            return "setbit";
          }
        });
  }

  public Boolean setbit(
      final int DBIndex, final String key, final long offset, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setbit(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
          }

          public String getOperationName() {
            return "setbit";
          }
        });
  }

  public Boolean getbit(final int DBIndex, final String key, final long offset) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.getbit(key, offset);
          }

          public String getOperationName() {
            return "getbit";
          }
        });
  }

  public Long setrange(final int DBIndex, final String key, final long offset, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setrange(key, offset, value);
          }

          public String getOperationName() {
            return "setrange";
          }
        });
  }

  public String getrange(
      final int DBIndex, final String key, final long startOffset, final long endOffset) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.getrange(key, startOffset, endOffset);
          }

          public String getOperationName() {
            return "getrange";
          }
        });
  }

  public String getSet(final int DBIndex, final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.getSet(key, value);
          }

          public String getOperationName() {
            return "getSet";
          }
        });
  }

  public Long setnx(final int DBIndex, final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setnx(key, value);
          }

          public String getOperationName() {
            return "setnx";
          }
        });
  }

  public String setex(final int DBIndex, final String key, final int seconds, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.setex(key, seconds, value);
          }

          public String getOperationName() {
            return "setex";
          }
        });
  }

  public Long decrBy(final int DBIndex, final String key, final long integer) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.decrBy(key, integer);
          }

          public String getOperationName() {
            return "decrBy";
          }
        });
  }

  public Long decr(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.decr(key);
          }

          public String getOperationName() {
            return "decr";
          }
        });
  }

  public Long incrBy(final int DBIndex, final String key, final long integer) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.incrBy(key, integer);
          }

          public String getOperationName() {
            return "incrBy";
          }
        });
  }

  public Long incr(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.incr(key);
          }

          public String getOperationName() {
            return "incr";
          }
        });
  }

  public Long append(final int DBIndex, final String key, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.append(key, value);
          }

          public String getOperationName() {
            return "append";
          }
        });
  }

  public String substr(final int DBIndex, final String key, final int start, final int end) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.substr(key, start, end);
          }

          public String getOperationName() {
            return "substr";
          }
        });
  }

  public Long hset(final int DBIndex, final String key, final String field, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hset(key, field, value);
          }

          public String getOperationName() {
            return "hset";
          }
        });
  }

  public String hget(final int DBIndex, final String key, final String field) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hget(key, field);
          }

          public String getOperationName() {
            return "hget";
          }
        });
  }

  public Long hsetnx(final int DBIndex, final String key, final String field, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hsetnx(key, field, value);
          }

          public String getOperationName() {
            return "hsetnx";
          }
        });
  }

  public String hmset(final int DBIndex, final String key, final Map<String, String> hash) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hmset(key, hash);
          }

          public String getOperationName() {
            return "hmset";
          }
        });
  }

  public List<String> hmget(final int DBIndex, final String key, final String... fields) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hmget(key, fields);
          }

          public String getOperationName() {
            return "hmget";
          }
        });
  }

  public Long hincrBy(final int DBIndex, final String key, final String field, final long value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hincrBy(key, field, value);
          }

          public String getOperationName() {
            return "hincrBy";
          }
        });
  }

  public Boolean hexists(final int DBIndex, final String key, final String field) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hexists(key, field);
          }

          public String getOperationName() {
            return "hexists";
          }
        });
  }

  @Override
  public Long del(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.del(key);
          }

          public String getOperationName() {
            return "del";
          }
        });
  }

  public Long hdel(final int DBIndex, final String key, final String... fields) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hdel(key, fields);
          }

          public String getOperationName() {
            return "hdel";
          }
        });
  }

  public Long hlen(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hlen(key);
          }

          public String getOperationName() {
            return "hlen";
          }
        });
  }

  public Set<String> hkeys(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hkeys(key);
          }

          public String getOperationName() {
            return "hkeys";
          }
        });
  }

  public List<String> hvals(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hvals(key);
          }

          public String getOperationName() {
            return "hvals";
          }
        });
  }

  public Map<String, String> hgetAll(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Map<String, String>>() {

          public Map<String, String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.hgetAll(key);
          }

          public String getOperationName() {
            return "hgetAll";
          }
        });
  }

  public Long rpush(final int DBIndex, final String key, final String... strings) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.rpush(key, strings);
          }

          public String getOperationName() {
            return "rpush";
          }
        });
  }

  public Long lpush(final int DBIndex, final String key, final String... strings) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lpush(key, strings);
          }

          public String getOperationName() {
            return "lpush";
          }
        });
  }

  public Long lpushx(final int DBIndex, final String key, final String string) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lpushx(key, string);
          }

          public String getOperationName() {
            return "lpushx";
          }
        });
  }

  public Long strlen(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.strlen(key);
          }

          public String getOperationName() {
            return "strlen";
          }
        });
  }

  public Long move(final int DBIndex, final String key, final int dbIndex) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.move(key, dbIndex);
          }

          public String getOperationName() {
            return "move";
          }
        });
  }

  public Long rpushx(final int DBIndex, final String key, final String string) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.rpushx(key, string);
          }

          public String getOperationName() {
            return "rpushx";
          }
        });
  }

  public Long persist(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.persist(key);
          }

          public String getOperationName() {
            return "persist";
          }
        });
  }

  public Long llen(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.llen(key);
          }

          public String getOperationName() {
            return "llen";
          }
        });
  }

  public List<String> lrange(
      final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lrange(key, start, end);
          }

          public String getOperationName() {
            return "lrange";
          }
        });
  }

  public String ltrim(final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.ltrim(key, start, end);
          }

          public String getOperationName() {
            return "ltrim";
          }
        });
  }

  public String lindex(final int DBIndex, final String key, final long index) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lindex(key, index);
          }

          public String getOperationName() {
            return "lindex";
          }
        });
  }

  public String lset(final int DBIndex, final String key, final long index, final String value) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lset(key, index, value);
          }

          public String getOperationName() {
            return "lset";
          }
        });
  }

  public Long lrem(final int DBIndex, final String key, final long count, final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lrem(key, count, value);
          }

          public String getOperationName() {
            return "lrem";
          }
        });
  }

  public String lpop(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.lpop(key);
          }

          public String getOperationName() {
            return "lpop";
          }
        });
  }

  public String rpop(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.rpop(key);
          }

          public String getOperationName() {
            return "rpop";
          }
        });
  }

  public Long sadd(final int DBIndex, final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.sadd(key, members);
          }

          public String getOperationName() {
            return "sadd";
          }
        });
  }

  public Set<String> smembers(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.smembers(key);
          }

          public String getOperationName() {
            return "smembers";
          }
        });
  }

  public Long srem(final int DBIndex, final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.srem(key, members);
          }

          public String getOperationName() {
            return "srem";
          }
        });
  }

  public String spop(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.spop(key);
          }

          public String getOperationName() {
            return "spop";
          }
        });
  }

  public Long scard(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.scard(key);
          }

          public String getOperationName() {
            return "scard";
          }
        });
  }

  public Boolean sismember(final int DBIndex, final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Boolean>() {

          public Boolean doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.sismember(key, member);
          }

          public String getOperationName() {
            return "sismember";
          }
        });
  }

  public String srandmember(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.srandmember(key);
          }

          public String getOperationName() {
            return "srandmember";
          }
        });
  }

  public Long zadd(final int DBIndex, final String key, final double score, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zadd(key, score, member);
          }

          public String getOperationName() {
            return "zadd";
          }
        });
  }

  public Long zadd(final int DBIndex, final String key, final Map<String, Double> scoreMembers) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zadd(key, scoreMembers);
          }

          public String getOperationName() {
            return "zadd";
          }
        });
  }

  public Set<String> zrange(final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrange(key, start, end);
          }

          public String getOperationName() {
            return "zrange";
          }
        });
  }

  public Long zrem(final int DBIndex, final String key, final String... members) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrem(key, members);
          }

          public String getOperationName() {
            return "zrem";
          }
        });
  }

  public Double zincrby(
      final int DBIndex, final String key, final double score, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Double>() {

          public Double doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zincrby(key, score, member);
          }

          public String getOperationName() {
            return "zincrby";
          }
        });
  }

  public Long zrank(final int DBIndex, final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrank(key, member);
          }

          public String getOperationName() {
            return "zrank";
          }
        });
  }

  public Long zrevrank(final int DBIndex, final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrank(key, member);
          }

          public String getOperationName() {
            return "zrevrank";
          }
        });
  }

  public Set<String> zrevrange(
      final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrange(key, start, end);
          }

          public String getOperationName() {
            return "zrevrange";
          }
        });
  }

  public Set<Tuple> zrangeWithScores(
      final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeWithScores(key, start, end);
          }

          public String getOperationName() {
            return "zrangeWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeWithScores(
      final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeWithScores(key, start, end);
          }

          public String getOperationName() {
            return "zrevrangeWithScores";
          }
        });
  }

  public Long zcard(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zcard(key);
          }

          public String getOperationName() {
            return "zcard";
          }
        });
  }

  public Double zscore(final int DBIndex, final String key, final String member) {
    return doOperation(
        key,
        new JedisCallBack<Double>() {

          public Double doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zscore(key, member);
          }

          public String getOperationName() {
            return "zscore";
          }
        });
  }

  public List<String> sort(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.sort(key);
          }

          public String getOperationName() {
            return "sort";
          }
        });
  }

  public List<String> sort(
      final int DBIndex, final String key, final SortingParams sortingParameters) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.sort(key, sortingParameters);
          }

          public String getOperationName() {
            return "sort";
          }
        });
  }

  public Long zcount(final int DBIndex, final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zcount(key, min, max);
          }

          public String getOperationName() {
            return "zcount";
          }
        });
  }

  public Long zcount(final int DBIndex, final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zcount(key, min, max);
          }

          public String getOperationName() {
            return "zcount";
          }
        });
  }

  public Set<String> zrangeByScore(
      final int DBIndex, final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScore(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final int DBIndex, final String key, final double max, final double min) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScore(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<String> zrangeByScore(
      final int DBIndex,
      final String key,
      final double min,
      final double max,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScore(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final int DBIndex,
      final String key,
      final double max,
      final double min,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScore(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final int DBIndex, final String key, final double min, final double max) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScoreWithScores(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final int DBIndex, final String key, final double max, final double min) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);

            return jedis.zrevrangeByScoreWithScores(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final int DBIndex,
      final String key,
      final double min,
      final double max,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final int DBIndex,
      final String key,
      final double max,
      final double min,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<String> zrangeByScore(
      final int DBIndex, final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScore(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final int DBIndex, final String key, final String max, final String min) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScore(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<String> zrangeByScore(
      final int DBIndex,
      final String key,
      final String min,
      final String max,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScore(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScore";
          }
        });
  }

  public Set<String> zrevrangeByScore(
      final int DBIndex,
      final String key,
      final String max,
      final String min,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<String>>() {

          public Set<String> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScore(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScore";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final int DBIndex, final String key, final String min, final String max) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScoreWithScores(key, min, max);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final int DBIndex, final String key, final String max, final String min) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScoreWithScores(key, max, min);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrangeByScoreWithScores(
      final int DBIndex,
      final String key,
      final String min,
      final String max,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
          }

          public String getOperationName() {
            return "zrangeByScoreWithScores";
          }
        });
  }

  public Set<Tuple> zrevrangeByScoreWithScores(
      final int DBIndex,
      final String key,
      final String max,
      final String min,
      final int offset,
      final int count) {
    return doOperation(
        key,
        new JedisCallBack<Set<Tuple>>() {

          public Set<Tuple> doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
          }

          public String getOperationName() {
            return "zrevrangeByScoreWithScores";
          }
        });
  }

  public Long zremrangeByRank(
      final int DBIndex, final String key, final long start, final long end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zremrangeByRank(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByRank";
          }
        });
  }

  public Long zremrangeByScore(
      final int DBIndex, final String key, final double start, final double end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zremrangeByScore(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByScore";
          }
        });
  }

  public Long zremrangeByScore(
      final int DBIndex, final String key, final String start, final String end) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.zremrangeByScore(key, start, end);
          }

          public String getOperationName() {
            return "zremrangeByScore";
          }
        });
  }

  public Long linsert(
      final int DBIndex,
      final String key,
      final LIST_POSITION where,
      final String pivot,
      final String value) {
    return doOperation(
        key,
        new JedisCallBack<Long>() {

          public Long doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.linsert(key, where, pivot, value);
          }

          public String getOperationName() {
            return "linsert";
          }
        });
  }

  @Override
  public String set(final int DBIndex, final String key, final Object value) {

    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            byte[] byteValue = serialize(value);
            return jedis.set(SafeEncoder.encode(key), byteValue);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public String set(final int DBIndex, final String key, final int seconds, final Object value) {

    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            byte[] byteValue = serialize(value);
            return jedis.setex(SafeEncoder.encode(key), seconds, byteValue);
          }

          public String getOperationName() {
            return "set";
          }
        });
  }

  public Object getObject(final int DBIndex, final String key) {

    return doOperation(
        key,
        new JedisCallBack<Object>() {

          public Object doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            byte[] result = jedis.get(SafeEncoder.encode(key));
            Object object = deserialize(result);
            return object;
          }

          public String getOperationName() {
            return "getObject";
          }
        });
  }

  public String info(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.info(key);
          }

          public String getOperationName() {
            return "info";
          }
        });
  }

  public String echo(final int DBIndex, final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            jedis.select(DBIndex);
            return jedis.echo(key);
          }

          public String getOperationName() {
            return "echo";
          }
        });
  }

  @Override
  public Long pexpireAt(String key, long millisecondsTimestamp) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long zlexcount(String key, String min, String max) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> zrangeByLex(String key, String min, String max) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> zrevrangeByLex(String key, String max, String min) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long zremrangeByLex(String key, String min, String max) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long lpushx(String key, String... string) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long rpushx(String key, String... string) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> blpop(String arg) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> blpop(int timeout, String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> brpop(String arg) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> brpop(final int timeout, final String key) {
    return doOperation(
        key,
        new JedisCallBack<List<String>>() {

          public List<String> doBiz(Jedis jedis) {
            return jedis.brpop(timeout, key);
          }

          public String getOperationName() {
            return "brpop";
          }
        });
  }

  @Override
  public String echo(final String key) {
    return doOperation(
        key,
        new JedisCallBack<String>() {

          public String doBiz(Jedis jedis) {
            return jedis.echo(key);
          }

          public String getOperationName() {
            return "echo";
          }
        });
  }

  @Override
  public Long bitcount(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long bitcount(String key, long start, long end) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<String> sscan(String key, int cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<Tuple> zscan(String key, int cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<String> sscan(String key, String cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ScanResult<Tuple> zscan(String key, String cursor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Long pfadd(String key, String... elements) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long pfcount(String key) {
    // TODO Auto-generated method stub
    return 0;
  }

  protected byte[] serialize(Object o) {
    if (o == null) {
      return new byte[0];
    }
    byte[] rv = null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(bos);
      os.writeObject(o);
      os.close();
      bos.close();
      rv = bos.toByteArray();
    } catch (IOException e) {
      throw new IllegalArgumentException("Non-serializable object", e);
    }
    return rv;
  }

  /**
   * Get the object represented by the given serialized bytes.
   *
   * @return 406@/rigel-tcom2/modules/tcom-cache/src/main/java/com/baidu/rigel /service/cache/redis/RedisClient.java
   */
  protected Object deserialize(byte[] in) {
    Object rv = null;
    try {
      if (in != null) {
        ByteArrayInputStream bis = new ByteArrayInputStream(in);
        ObjectInputStream is = new ObjectInputStream(bis);
        rv = is.readObject();
        is.close();
        bis.close();
      }
    } catch (IOException e) {
      // log.warn("Caught IOException decoding %d bytes of data", e);
    } catch (ClassNotFoundException e) {
      // log.warn("Caught CNFE decoding %d bytes of data", e);
    }
    return rv;
  }

  @Override
  public String set(String key, String value, String nxxx, String expx, long time) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Long pexpire(String key, long milliseconds) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Double incrByFloat(String key, double value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> spop(String key, long count) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> srandmember(String key, int count) {
    throw new UnsupportedOperationException();
  }
}
