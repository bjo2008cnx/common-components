package com.github.common.componets.lock.annotation;

import com.github.common.components.util.lang.StringUtil;
import com.github.common.componets.lock.DistributedReentrantLock;
import com.github.common.componets.lock.exception.RedisLockException;
import com.github.common.componets.lock.exception.RedisLockCode;
import com.github.common.componets.lock.redis.RedisReentrantLock;
import com.github.common.componets.lock.util.RedisKeyUtil;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

/**
 * @redis lock
 **/
@Aspect
@Service
public class DistributedLockAspect {

  @Autowired
  private JedisPool jedisPool;

  /**
   * AOP处理类
   *
   * @param joinPoint
   * @return
   */
  @Around("@annotation(com.github.common.componets.lock.annotation.DistributedLock)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    Object[] args = joinPoint.getArgs();
    return doTryLock(args, method, joinPoint);
  }

  /**
   * @param args
   * @param method
   * @param joinPoint
   * @return
   * @description CacheGetter注解解析方法
   */
  private Object doTryLock(Object[] args, Method method, ProceedingJoinPoint joinPoint) throws Throwable {
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
    // 获取被拦截方法参数名列表(使用Spring支持类库)
    LocalVariableTableParameterNameDiscoverer nameDiscoverer =
        new LocalVariableTableParameterNameDiscoverer();
    String[] parameterNames = nameDiscoverer.getParameterNames(method);
    String key = RedisKeyUtil.parseKey(args, parameterNames, distributedLock.key());
    if (!StringUtil.isEmpty(distributedLock.value())) {
      key = distributedLock.value() + ":" + key;
    }
    boolean isLockSuccess = false;
    DistributedReentrantLock distributedReentrantLock = null;
    try {
      distributedReentrantLock = new RedisReentrantLock(jedisPool, key);
      isLockSuccess = distributedReentrantLock.tryLock(distributedLock.waitMills(), TimeUnit.MILLISECONDS);
      if (isLockSuccess) {
        return joinPoint.proceed();
      } else {
        //TODO 锁数据失败
        throw new RedisLockException(RedisLockCode.REDIS_LOCK_KEY_ERROR);
      }
    } catch (Throwable t) {
      // TODO 处理异常
      throw new RedisLockException(RedisLockCode.REDIS_LOCK_ERROR, t);
    } finally {
      if (null != distributedReentrantLock && isLockSuccess) {
        distributedReentrantLock.unlock();
      }
    }
  }
}
