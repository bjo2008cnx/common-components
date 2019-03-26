package com.github.common.componets.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式可重入锁
 */
public interface DistributedReentrantLock {
    boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    void unlock();
}
