package com.github.common.componets.lock;

/**
 * 分布式锁模板类
 */
public interface DistributedLockTemplate {

    /**
     * @param lockId   锁id
     * @param timeout  单位毫秒
     * @param callback 回调函数
     * @return
     */
    Object execute(String lockId, int timeout, LockCallback callback);
}
