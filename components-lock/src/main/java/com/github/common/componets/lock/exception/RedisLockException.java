package com.github.common.componets.lock.exception;

import com.github.common.components.util.enums.CodedEnum;
import com.github.common.componets.exception.exceptions.CodedRuntimeException;

/**
 * @description: redis 锁异常
 * @author: luffy
 * @create: 2018-10-20 14:39
 **/
public class RedisLockException extends CodedRuntimeException {

  private static final long serialVersionUID = 7049667991452421417L;


  public RedisLockException(CodedEnum code) {
    super(code);
  }

  public RedisLockException(CodedEnum code, String message) {
    super(code, message);
  }

  public RedisLockException(CodedEnum code, Throwable t) {
    super(code, t);
  }
}
