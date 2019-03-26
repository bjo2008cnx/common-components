package com.github.common.componets.lock.redis;

import com.github.common.componets.lock.DistributedReentrantLock;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RedisReentrantLock implements DistributedReentrantLock {

    private final Map<Thread, LockData> threadData = new ConcurrentHashMap();

    private RedisLockInternals internals;

    private String lockId;


    public RedisReentrantLock(JedisPool jedisPool, String lockId) {
        this.lockId = lockId;
        this.internals = new RedisLockInternals(jedisPool);
    }

    private static class LockData {
        final Thread owningThread;
        final String lockVal;
        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread owningThread, String lockVal) {
            this.owningThread = owningThread;
            this.lockVal = lockVal;
        }
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        log.info("try lock");
        Thread currentThread = Thread.currentThread();
        LockData lockData = threadData.get(currentThread);
        if (lockData != null) {
            lockData.lockCount.incrementAndGet();
            return true;
        }
        String lockVal = internals.tryLock(lockId, timeout, unit);
        if (lockVal != null) {
            LockData newLockData = new LockData(currentThread, lockVal);
            threadData.put(currentThread, newLockData);
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        Thread currentThread = Thread.currentThread();
        LockData lockData = threadData.get(currentThread);
        if (lockData == null) {
            throw new IllegalMonitorStateException("You do not own the lock: " + lockId);
        }
        int newLockCount = lockData.lockCount.decrementAndGet();
        if (newLockCount > 0) {
            return;
        } else if (newLockCount < 0) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock: " + lockId);
        }
        try {
            internals.unlock(lockId, lockData.lockVal);
        } finally {
            threadData.remove(currentThread);
        }
    }
}
