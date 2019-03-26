package com.github.common.components.limiter.provider;

/**
 * 限流工具
 *
 * @author
 * @date 2018/9/1
 */
public interface GlobalLimiter {

  /**
   * 获取1个令牌
   *
   * @return
   */
  boolean acquire();

  /**
   * 获取指定数量的令牌
   *
   * @param count
   * @return
   */
  boolean acquire(int count);
}
