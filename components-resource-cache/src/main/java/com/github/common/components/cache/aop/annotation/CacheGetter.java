package com.github.common.components.cache.aop.annotation;

import com.github.common.components.cache.aop.CachedDataType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CacheGetter {

  /**
   * key值
   *
   * @return
   */
  String key();

  /**
   * 缓存过期时间,单位为秒默认为不过期(0)
   *
   * @return
   */
  int expire() default 0;

  /**
   * db 索引
   *
   * @return
   */
  int DBIndex() default 0;

  /**
   * force是否强制刷新数据
   *
   * @return
   */
  boolean force() default false;

  /**
   * 数据类型
   *
   * @return
   */
  CachedDataType dataType() default CachedDataType.JSON;
}
