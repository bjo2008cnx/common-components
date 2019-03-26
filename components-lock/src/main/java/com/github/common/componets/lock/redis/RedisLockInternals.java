package com.github.common.componets.lock.redis;

import com.github.common.components.util.io.StreamUtil;
import com.github.common.components.util.lang.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class RedisLockInternals {

    private JedisPool jedisPool;

    /**
     * 重试等待时间
     */
    private int retryAwait = 300;

    private int lockTimeout = 20000;

    public RedisLockInternals(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String tryLock(String lockId, long time, TimeUnit unit) {
        AssertUtil.assertNotNullOrEmpty("lockId", lockId);
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;
        String lockValue = null;
        while (lockValue == null) {
            lockValue = createRedisKey(lockId);
            if (lockValue != null) {
                break;
            } else if (System.currentTimeMillis() - startMillis - retryAwait > millisToWait) {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
        return lockValue;
    }

    private String createRedisKey(String lockId) {
        Jedis jedis = null;
        try {
            String value = lockId + randomId(3);
            jedis = jedisPool.getResource();
            //auth(jedis);
            List<String> keys = Arrays.asList(lockId);
            List<String> args = Arrays.asList(value, String.valueOf(lockTimeout));
            Long ret = (Long) jedis.eval(RedisLockConstant.CREATE_KEY_LUA, keys, args);
            Long result = 1L;
            return result.equals(ret) ? value : null;
        } finally {
            StreamUtil.close(jedis);
        }
    }

    protected void auth(Jedis jedis, String password) {
        if (password != null && !"".equals(password)) {
            jedis.auth(password);
        }
    }

    protected void unlock(String key, String value) {
        AssertUtil.assertNotNullOrEmpty(key, key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = Arrays.asList(key);
            List<String> args = Arrays.asList(value);
            jedis.eval(RedisLockConstant.UNLOCK_LUA, keys, args);
        } finally {
            StreamUtil.close(jedis);
        }
    }

    private static String randomId(int size) {
        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = RedisLockConstant.digits[ThreadLocalRandom.current().nextInt(RedisLockConstant.digits.length)];
        }
        return new String(cs);
    }

}
