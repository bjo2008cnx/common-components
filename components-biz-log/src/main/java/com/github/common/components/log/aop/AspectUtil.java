package com.github.common.components.log.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 切面工具类
 *
 * @author
 * @date 2018/9/11
 */
public class AspectUtil {
  /**
   * 获取类名
   *
   * @param joinPoint
   * @return
   */
  public static String getClassName(ProceedingJoinPoint joinPoint) {
    return joinPoint.getTarget().getClass().getName();
  }

  /**
   * 获取方法名
   *
   * @param joinPoint
   * @return
   */
  public static String getMethodName(ProceedingJoinPoint joinPoint) {
    return joinPoint.getSignature().getName();
  }

  /**
   * 获取参数列表
   *
   * @param joinPoint
   * @return
   */
  public static Object[] getInput(ProceedingJoinPoint joinPoint) {
    return joinPoint.getArgs();
  }
}
