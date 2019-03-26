package com.github.common.componets.exception.handler;

import java.util.Map;

/**
 * 异常处理接口
 *
 * @date 2016/4/14
 */
public interface ExceptionHandler {
  /**
   * 处理异常
   *
   * @param t 异常
   * @param data 异常相关的数据
   */
  void handle(Throwable t, Map data);
}
