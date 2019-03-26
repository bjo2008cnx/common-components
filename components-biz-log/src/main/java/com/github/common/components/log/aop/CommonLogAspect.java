package com.github.common.components.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志处理类：将方法调用的输入输出写入本地文件，便于logstash收集。该日志用于出错时排错
 *
 * @author Michael
 */
@Aspect
@Component
@Slf4j
public class CommonLogAspect {
  @Autowired private LogHandler logHandler;

  /**
   * AOP处理类
   *
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around("@annotation(com.github.common.components.log.annotation.CallLog)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    String className = AspectUtil.getClassName(joinPoint);
    String methodName = AspectUtil.getMethodName(joinPoint);

    // 执行前处理
    logHandler.preHandle(className, methodName);
    Long startTime = System.currentTimeMillis();

    // 执行方法
    Object output = null;
    Throwable ex = null;
    try {
      output = joinPoint.proceed();
    } catch (Throwable t) {
      ex = t;
    }

    Long endTime = System.currentTimeMillis();

    // 执行后处理
    Object[] inputParameters = AspectUtil.getInput(joinPoint);
    logHandler.postHandle(inputParameters, className, methodName, startTime, endTime, output, ex);
    if (ex != null) {
      // 抛出异常
      throw ex;
    }
    return output;
  }
}
