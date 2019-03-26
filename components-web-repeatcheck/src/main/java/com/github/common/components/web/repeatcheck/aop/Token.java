package com.github.common.components.web.repeatcheck.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Token, 用于避免客户端的重复提交
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {

  /**
   * 标记需要生成的token数量
   */
  int generate() default 1;

  /**
   * 标记为需要删除token(只删除1个)
   */
  boolean remove() default false;
}