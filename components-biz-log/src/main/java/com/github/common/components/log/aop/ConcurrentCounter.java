package com.github.common.components.log.aop;

import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发计数器
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
public class ConcurrentCounter {
  /** 并发数 */
  public static final Map<String, AtomicInteger> CONCURRENT_COUNT = new ConcurrentHashMap();

  /** 获取并发计数器 */
  public static AtomicInteger getConcurrent(String className, String methodName) {
    AtomicInteger concurrent = null;
    try {
      String key = StringUtil.join('.', className, methodName);
      concurrent = CONCURRENT_COUNT.get(key);
      if (concurrent == null) {
        CONCURRENT_COUNT.putIfAbsent(key, new AtomicInteger());
        concurrent = CONCURRENT_COUNT.get(key);
      }
    } catch (Exception e) {
      log.error("fail to get concurrent count", e);
    }
    return concurrent;
  }
}
