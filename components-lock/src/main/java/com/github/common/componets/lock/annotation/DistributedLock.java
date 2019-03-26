package com.github.common.componets.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @redis lock
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DistributedLock {

  /**
   * 锁的key
   */
  String key();

  /**
   * key的前缀
   */
  String value();

  /**
   * 未获取到锁等待时间
   */
  long waitMills() default 1 * 1000l;
}
