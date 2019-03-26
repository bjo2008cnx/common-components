package com.github.common.componets.exception.exceptions;

import com.github.common.components.util.enums.CodedEnum;
import lombok.Data;

/**
 * 带编码的异常
 */
@Data
public class CodedRuntimeException extends RuntimeException {

  private CodedEnum errorCode;

  public CodedRuntimeException(CodedEnum code) {
    this.errorCode = code;
  }

  public CodedRuntimeException(CodedEnum code, String message) {
    super(message);
    this.errorCode = code;
  }

  public CodedRuntimeException(CodedEnum code, Throwable t) {
    super(t);
    this.errorCode = code;
  }
}
