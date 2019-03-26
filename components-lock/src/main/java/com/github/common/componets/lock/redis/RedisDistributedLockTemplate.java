package com.github.common.componets.lock.redis;

import com.github.common.componets.lock.DistributedLockTemplate;
import com.github.common.componets.lock.LockCallback;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;


@Slf4j
@AllArgsConstructor
public class RedisDistributedLockTemplate implements DistributedLockTemplate {

    private JedisPool jedisPool;

    @Override
    public Object execute(String lockId, int timeout, LockCallback callback) {
        RedisReentrantLock distributedReentrantLock = null;
        boolean isLocked = false;
        try {
            distributedReentrantLock = new RedisReentrantLock(jedisPool, lockId);
            if (distributedReentrantLock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                isLocked = true;
                return callback.onGetLock();
            } else {
                return callback.onTimeout();
            }
        } catch (InterruptedException e) {
            log.error("lock is interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("unknown error.", e);
        } finally {
            if (isLocked) {
                distributedReentrantLock.unlock();
            }
        }
        return null;
    }
}
