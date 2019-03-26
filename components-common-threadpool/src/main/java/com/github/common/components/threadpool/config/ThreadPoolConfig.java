package com.github.common.components.threadpool.config;

import lombok.Builder;
import lombok.Data;

/**
 * 线程池配置
 *
 * @author
 * @date 2018/9/28
 */
@Data
@Builder
public class ThreadPoolConfig {

  /**
   * core size
   */
  private Integer corePoolSize;

  private Integer maximumPoolSize;

  /**
   * 存活时间，当pool size>core_pool_size时，超时该时间则被回收
   */
  private Integer keepAliveTimeMs;

  /**
   * 队列大小
   */
  private Integer queueSize;

  /**
   * 拒绝策略：CallerRunsPolicy,AbortPolicy,DiscardPolicy,DiscardOldestPolicy
   */
  private String rejectPolicy;

  /**
   * 名称,用于可命名的线程池
   */
  private String poolName;

  /**
   * 是否deamon
   */
  private Boolean isDeamon;

}