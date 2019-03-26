package com.github.common.componets.exception.exceptions;

import com.github.common.components.util.enums.CodedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误编码，业务方应继承此类，扩展其他的异常
 *
 * @author Michael
 */
@AllArgsConstructor
@Getter
public enum ErrorCode implements CodedEnum<Integer> {
  SUCCESS(0, "操作成功");

  private Integer code;
  private String desc;
}
