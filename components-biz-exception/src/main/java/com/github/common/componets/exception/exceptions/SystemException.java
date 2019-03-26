package com.github.common.componets.exception.exceptions;

/**
 * 系统级错误 异常描述:分类(模块),类型(系统,业务),编号,描述, 产生原因, 解决办法, 级别
 *
 * @author jason
 */
public class SystemException extends RuntimeException {

  public SystemException() {
    super();
  }

  public SystemException(String errorMessage) {
    super(errorMessage);
  }

  public SystemException(Throwable t) {
    super(t);
  }

  public SystemException(String errorMessage, Throwable t) {
    super(errorMessage, t);
  }
}
