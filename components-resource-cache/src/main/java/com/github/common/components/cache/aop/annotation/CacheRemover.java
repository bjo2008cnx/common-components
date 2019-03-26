package com.github.common.components.cache.aop.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CacheRemover {

  int DBIndex();

  /**
   * key值
   *
   * @return
   */
  String[] key();

  /**
   * 是否批量
   *
   * @return
   */
  boolean isBatch() default false;
}
