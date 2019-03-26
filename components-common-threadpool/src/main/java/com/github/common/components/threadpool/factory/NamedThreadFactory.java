package com.github.common.components.threadpool.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可命名的线程工厂.
 */
public class NamedThreadFactory implements ThreadFactory {

  private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

  private final AtomicInteger threadCount = new AtomicInteger(1);

  private final String prefix;

  private final boolean isDeamon;

  private final ThreadGroup threadGroup;

  public NamedThreadFactory() {
    this("pool-" + POOL_SEQ.getAndIncrement(), false);
  }

  public NamedThreadFactory(String prefix) {
    this(prefix, false);
  }

  public NamedThreadFactory(String prefix, boolean daemon) {
    this.prefix = prefix + "-pool-" + POOL_SEQ.getAndIncrement() + "-thread-";
    isDeamon = daemon;
    SecurityManager manager = System.getSecurityManager();
    threadGroup = (manager == null) ? Thread.currentThread().getThreadGroup() : manager.getThreadGroup();
  }

  @Override
  public Thread newThread(Runnable runnable) {
    String name = prefix + threadCount.getAndIncrement();
    Thread thread = new Thread(threadGroup, runnable, name, 0);
    thread.setDaemon(isDeamon);
    return thread;
  }

  public ThreadGroup getThreadGroup() {
    return threadGroup;
  }
}
