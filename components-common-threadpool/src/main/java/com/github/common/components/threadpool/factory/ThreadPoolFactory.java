package com.github.common.components.threadpool.factory;

import com.github.common.components.threadpool.config.ConfigLoader;
import com.github.common.components.threadpool.config.ThreadPoolConfig;
import com.github.common.components.util.lang.AssertUtil;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂
 *
 * @author
 * @date 2018/9/28
 */
public class ThreadPoolFactory {

  /**
   * 根据配置创建线程池
   */
  public ExecutorService create(String namespace) {
    return create(ConfigLoader.load(namespace));
  }

  /**
   * 创建线程池
   */
  public ExecutorService create(ThreadPoolConfig config) {
    Integer coreSize = config.getCorePoolSize();
    Integer maxSize = config.getCorePoolSize();
    String name = config.getPoolName();
    Integer keepAliveTimeMs = config.getKeepAliveTimeMs();

    AssertUtil.assertNotEmpty(coreSize, "coreSize");
    AssertUtil.assertNotEmpty(maxSize, "maxSize");
    AssertUtil.assertNotEmpty(name, "name");

    Integer qsize = config.getQueueSize();
    BlockingQueue<Runnable> queue = (qsize == null || qsize <= 0) ? new LinkedBlockingQueue() : new LinkedBlockingQueue(qsize);

    ThreadPoolExecutor instance = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTimeMs, TimeUnit.MILLISECONDS, queue,
        new NamedThreadFactory(name));
    return instance;
  }

  /**
   * 创建单实例无界线程池
   */
  public ExecutorService createSingleInfinite(String poolName) {
    ThreadPoolConfig config = ThreadPoolConfig.builder().corePoolSize(1).maximumPoolSize(1).queueSize(-1).poolName(poolName).build();
    return create(config);
  }
}