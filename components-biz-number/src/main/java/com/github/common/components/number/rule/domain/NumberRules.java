package com.github.common.components.number.rule.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberRules {
  private List<NumberRule> bills;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NumberRule {
    private String bill;
    private List<NumberRuleElement> rules;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NumberRuleElement {
    // 规则类型，如常量，日期等
    private String type;

    // 规则长度
    private int length;

    // 规则的值，如果是常量，则设置该值；否则（如日期），可以不设置，系统自动生成
    private String value;
  }
}
