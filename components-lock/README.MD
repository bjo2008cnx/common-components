示例：
JedisPool jedisPool = .....;
DistributedReentrantLock lock = new RedisReentrantLock(jedisPool, lockId);
boolean isLockSuccess = false;
try {
    isLockSuccess = lock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
    if (isLockSuccess) {
        //do biz
    }
} catch (Throwable t) {
    //handle error
} finally {
    if (isLockSuccess) {
        lock.unlock();
    }
}
