package com.github.common.components.number.rule.domain;

import com.github.common.components.util.enums.CodedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则，枚举类
 *
 * @author Michael
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum implements CodedEnum<String> {
  CONSTANT("FIXED", "固定串"),
  DATE("DATE", "时间"),
  SEQUENCE("SEQ", "流水号"),
  RANDOM("RANDOM", "随机数");

  private String code;
  private String desc;
}
