package com.github.common.componets.lock;

import com.github.common.componets.lock.redis.RedisReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

/**
 * @description:
 * @author: luffy
 * @create: 2018-10-19 09:56
 **/
public class LockTest extends BaseJunit4Test {

  public JedisPool create() {
    GenericObjectPoolConfig gop = new GenericObjectPoolConfig();
    gop.setMaxTotal(100);
    return new JedisPool(gop, "192.168.80.113", 6379,
        10000,
        "k7lla2rL7qdR9vP2", 0);
  }

  /**
   * 测试同一个锁的并发
   */
  //@Test
  public void lockTest() throws InterruptedException {
    JedisPool jedisPool = create();
    final DistributedReentrantLock lock = new RedisReentrantLock(jedisPool, "lock_100");
    for (int i = 0; i < 100; i++) {
      new Thread(() -> {
        boolean isLockSuccess = false;
        try {
          isLockSuccess = lock.tryLock(100l, TimeUnit.MILLISECONDS);
          if (isLockSuccess) {
            System.out.println("success");
          } else {
            throw new RuntimeException("lock error");
          }
        } catch (Throwable t) {
          throw new RuntimeException("lock error");
        } finally {
          if (null != lock && isLockSuccess) {
            lock.unlock();
          }
        }
      }).start();
    }
    Thread.sleep(1000L);
  }

  /**
   * 测试不同锁的并发
   */
  @Test
  public void lockTest_MultiLock() throws InterruptedException {
    JedisPool jedisPool = create();
    for (int i = 0; i < 100; i++) {
      new Thread(() -> {
        DistributedReentrantLock lock = null;
        boolean isLockSuccess = false;
        try {
          lock = new RedisReentrantLock(jedisPool, "lock_100");
          isLockSuccess = lock.tryLock(100l, TimeUnit.MILLISECONDS);
          if (isLockSuccess) {
            System.out.println("success");
          } else {
            throw new RuntimeException("lock error");
          }
        } catch (Throwable t) {
          throw new RuntimeException("lock error");
        } finally {
          if (null != lock && isLockSuccess) {
            lock.unlock();
          }
        }
      }).start();
    }
    Thread.sleep(1000L);
  }

  //  @Test
  public void lockTest_MultiLock_Orderd() throws InterruptedException {
    JedisPool jedisPool = create();

    for (int i = 0; i < 100; i++) {
      new Thread(() -> {
        DistributedReentrantLock lock = null;
        boolean isLockSuccess = false;
        try {
          lock = new RedisReentrantLock(jedisPool, "lock_" + getIndex());
          isLockSuccess = lock.tryLock(100l, TimeUnit.MILLISECONDS);
          if (isLockSuccess) {
            System.out.println("success");
          } else {
            throw new RuntimeException("lock error");
          }
        } catch (Throwable t) {
          throw new RuntimeException("lock error");
        } finally {
          if (null != lock && isLockSuccess) {
            lock.unlock();
          }
        }
      }).start();
    }
    Thread.sleep(1000L);
  }

  public Integer getIndex() {
    return count.incrementAndGet();
  }

  private AtomicInteger count = new AtomicInteger(0);
}
