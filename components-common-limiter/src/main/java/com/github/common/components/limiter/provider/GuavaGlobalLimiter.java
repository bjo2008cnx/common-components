package com.github.common.components.limiter.provider;

import com.google.common.util.concurrent.RateLimiter;
import com.github.common.components.limiter.config.ConfigLoader;
import com.github.common.components.limiter.config.LimiterConfig;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 流控工具类
 *
 * @date 2018/8/25
 */
@Slf4j
public class GuavaGlobalLimiter implements GlobalLimiter {

  @Getter
  private LimiterConfig config;

  private RateLimiter limiter;

  /**
   * 根据LimiterConfig对象初始化RateLimiter对象
   */
  public GuavaGlobalLimiter(LimiterConfig config) {
    this.config = config;
    limiter = RateLimiter.create(config.getPermitsPerSecond());
  }

  /**
   * 根据apollo namespace对象初始化RateLimiter对象。apollo中具体的配置请参考limiter.properties文件
   */
  public GuavaGlobalLimiter(String namespace) {
    this.config = ConfigLoader.loadByApollo(namespace);

    if (config.getWarmupPeriodMs() == null) {
      limiter = RateLimiter.create(config.getPermitsPerSecond());
    } else {
      limiter =
          RateLimiter.create(
              config.getPermitsPerSecond(), config.getWarmupPeriodMs(), TimeUnit.MILLISECONDS);
    }
  }

  /**
   * 流控
   */
  public boolean acquire(int count) {
    count = count == 0 ? 1 : count;
    // 如果没启用限流，直接返回true
    if (!isLimiterEnabled()) {
      return true;
    }
    try {
      return limiter.tryAcquire(count, config.getAcquireTimeoutMs(), TimeUnit.MILLISECONDS);
    } catch (Throwable t) {
      log.error("fail to acquire permission", t);
      return false;
    }
  }

  /**
   * 判断流控是否启用
   */
  public boolean isLimiterEnabled() {
    return config.getIsLimiterEnabled();
  }

  /**
   * 获取1个令牌
   */
  public boolean acquire() {
    return acquire(1);
  }
}
