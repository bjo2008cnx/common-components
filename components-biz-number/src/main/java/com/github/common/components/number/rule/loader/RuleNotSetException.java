package com.github.common.components.number.rule.loader;

/**
 * 规则未配置异常
 *
 * @author
 * @date 2018/9/15
 */
public class RuleNotSetException extends RuntimeException {
  public RuleNotSetException() {
    super();
  }

  public RuleNotSetException(String message) {
    super(message);
  }
}
