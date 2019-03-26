package com.github.common.components.web.repeatcheck.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举类
 */
@Getter
@AllArgsConstructor
public enum TokenStatusEnum {

  INIT("1", "初始化"),

  IN_PROGRESS("2", "进行中"),;

  private String key;

  private String description;
}
