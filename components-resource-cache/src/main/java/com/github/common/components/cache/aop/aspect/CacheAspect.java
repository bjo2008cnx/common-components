package com.github.common.components.cache.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.github.common.components.cache.aop.annotation.CacheGetter;
import com.github.common.components.cache.aop.annotation.CacheRemover;
import com.github.common.components.cache.client.CacheClient;
import com.github.common.components.rpc.serializer.GlobalSerializer;
import com.github.common.components.util.lang.ExceptionUtil;
import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Aspect
@Component
@Slf4j
public class CacheAspect {

  @Autowired
  @Qualifier(value = "simpleRedisClient")
  private CacheClient cacheClient;

  @Around("@annotation(com.github.common.components.cache.aop.annotation.CacheGetter)")
  public Object getCache(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    Object[] args = joinPoint.getArgs();
    return doCacheGet(args, method, joinPoint);
  }

  @Around("@annotation(com.github.common.components.cache.aop.annotation.CacheRemover)")
  public Object removeCache(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    Object[] args = joinPoint.getArgs();
    return doCacheRemove(args, method, joinPoint);
  }

  /**
   * @param args
   * @param method
   * @param joinPoint
   * @return
   * @throws Throwable
   * @description CacheGetter注解解析方法
   */
  public Object doCacheGet(Object[] args, Method method, ProceedingJoinPoint joinPoint)
      throws Throwable {
    CacheGetter cacheGetterAnnotation = method.getAnnotation(CacheGetter.class);

    // 获取被拦截方法参数名列表(使用Spring支持类库)
    LocalVariableTableParameterNameDiscoverer nameDiscoverer =
        new LocalVariableTableParameterNameDiscoverer();
    String[] parameterNames = nameDiscoverer.getParameterNames(method);
    Type methodGenericReturnType = method.getGenericReturnType();
    String key = com.github.common.components.cache.aop.aspect.CacheService.parseKey(args, parameterNames, cacheGetterAnnotation.key());

    // 默认是json格式
    if (cacheGetterAnnotation.force()) {
      // 强制更新数据，直接从db中取,取完之后放入cache
      return proceedAndUpdateRedis(args, joinPoint, cacheGetterAnnotation, key);
    } else {
      // 如果不是强制更新，则直接从redis中取
      Object result = null;
      String json = cacheClient.get(cacheGetterAnnotation.DBIndex(), key);
      if (StringUtil.isNotEmpty(json)) {
        json = json.substring(7); // 去掉无关信息 XXX
        result = GlobalSerializer.parseObject(json, methodGenericReturnType);
      }
      if (StringUtil.isEmpty(json)) {
        // 如果redis中不存在，则从db查询数据，缓存，返回对象
        result = proceedAndUpdateRedis(args, joinPoint, cacheGetterAnnotation, key);
      }
      return result;
    }
  }

  /**
   * 执行方法后更新cache
   *
   * @param args
   * @param joinPoint
   * @param annotation
   * @param key
   * @return
   */
  private Object proceedAndUpdateRedis(
      Object[] args, ProceedingJoinPoint joinPoint, CacheGetter annotation, String key) {
    Object object;
    try {
      object = joinPoint.proceed(args);
    } catch (Throwable t) {
      throw ExceptionUtil.transform(t);
    }
    if (object != null) {
      setRedisValueJson(annotation, key, object);
    }
    return object;
  }

  /**
   * @param methodType
   * @param key
   * @param object
   */
  private void setRedisValueJson(CacheGetter methodType, String key, Object object) {
    String jsonStr = JSON.toJSONString(object);
    if (methodType.expire() == 0) { // 0:永不过期
      cacheClient.set(methodType.DBIndex(), key, jsonStr);
    } else if (methodType.expire() == 1) { // 1:过期时间为24h
      cacheClient.set(methodType.DBIndex(), key, com.github.common.components.cache.aop.aspect.CacheService.DEFAULT_TTL, jsonStr);
    } else { // 手动指定
      cacheClient.set(methodType.DBIndex(), key, methodType.expire(), jsonStr);
    }
  }

  /**
   * @param args
   * @param method
   * @param joinPoint
   * @return
   * @throws Throwable
   * @description redisCache 清除方法
   */
  public Object doCacheRemove(Object[] args, Method method, ProceedingJoinPoint joinPoint)
      throws Throwable {
    ExpressionParser parser = new SpelExpressionParser();
    EvaluationContext context = new StandardEvaluationContext();

    // 获取被拦截方法参数名列表(使用Spring支持类库)
    LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
    String[] parameterNames = u.getParameterNames(method);
    // 把方法参数放入SPEL上下文中
    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }
    // 如果有这个注解，则获取注解类
    Object object = joinPoint.proceed(args);

    // 如果有这个注解，则获取注解类
    CacheRemover annotation = method.getAnnotation(CacheRemover.class);
    if (annotation.isBatch()) {
      for (String str : annotation.key()) {
        String keyStr = parser.parseExpression(str).getValue(context, String.class);
        String[] keys = keyStr.split(",");
        for (String key : keys) {
          cacheClient.del(annotation.DBIndex(), key);
        }
      }
    } else {
      for (String str : annotation.key()) {
        String key = parser.parseExpression(str).getValue(context, String.class);
        cacheClient.del(annotation.DBIndex(), key);
      }
    }
    return object;
  }
}
