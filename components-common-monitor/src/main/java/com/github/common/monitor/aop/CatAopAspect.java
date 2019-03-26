package com.github.common.monitor.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author luffy
 */

@Aspect
public class CatAopAspect {

  public static final String METHOD = "Method";

  @Around("@annotation(com.github.common.monitor.aop.MonitorTransaction)")
  public void aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    proceedWithCat(joinPoint, method);
  }

  /**
   * 无打点
   */
  private void proceedNoraml(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      joinPoint.proceed();
    } catch (Throwable e) {
      //e.printStackTrace();
      //XXX 是否会异常处理堆栈的丢失
    }
  }

  /**
   * 使用CAT 打点
   */
  private void proceedWithCat(ProceedingJoinPoint joinPoint, Method method) throws Throwable {
    Transaction tx = Cat.newTransaction(METHOD, method.getName());
    try {
      joinPoint.proceed();
      tx.setStatus(Transaction.SUCCESS);
//      tx.setSuccessStatus();  3.0 api
    } catch (Throwable e) {
      tx.setStatus(e);
      Cat.logError(e);
      throw e;
    } finally {
      tx.complete();
    }
  }
}
