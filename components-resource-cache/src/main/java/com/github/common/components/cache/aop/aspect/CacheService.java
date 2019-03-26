package com.github.common.components.cache.aop.aspect;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 缓存服务
 *
 * @date 2018/6/30
 */
public class CacheService {

  // 默认的过期时间：24h
  public static final int DEFAULT_TTL = 60 * 60 * 24;

  /**
   * @param args 参数值
   * @param parameterNames 参数名
   * @param complexKey key值
   * @return
   */
  public static String parseKey(Object[] args, String[] parameterNames, String complexKey) {
    // 把方法参数名与参数值成对放入SPEL上下文中
    EvaluationContext expressionContext = new StandardEvaluationContext();
    for (int i = 0; i < parameterNames.length; i++) {
      expressionContext.setVariable(parameterNames[i], args[i]);
    }

    // 解析key
    ExpressionParser parser = new SpelExpressionParser();
    return parser.parseExpression(complexKey).getValue(expressionContext, String.class);
  }
}
