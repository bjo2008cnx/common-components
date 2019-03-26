package com.github.common.componets.exception.exceptions;

/**
 * 可以直接抛给用户的业务异常. 统一处理异常时，需要过滤此异常
 *
 * @date 2016/4/14
 */
public class BusinessException extends CodedRuntimeException {
  public BusinessException(ErrorCode code) {
    super(code);
  }

  public BusinessException(ErrorCode code, Throwable t) {
    super(code, t);
  }
}
