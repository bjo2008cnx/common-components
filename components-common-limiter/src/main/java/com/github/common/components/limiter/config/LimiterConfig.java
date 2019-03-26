package com.github.common.components.limiter.config;

import lombok.Data;

/**
 * 流控配置
 *
 * @author
 * @date 2018/8/25
 */
@Data
public class LimiterConfig {

  /** 是否启动限流 */
  private Boolean isLimiterEnabled;

  /** 每秒的流量 */
  private Integer permitsPerSecond;

  /** 获取令牌的超时时间(毫秒) */
  private Integer acquireTimeoutMs;

  /** 从冷启动速率过渡到平均速率的时间间隔,单位为毫秒 */
  private Long warmupPeriodMs;
}
