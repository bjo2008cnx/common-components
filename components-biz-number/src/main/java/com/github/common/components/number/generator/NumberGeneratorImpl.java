package com.github.common.components.number.generator;

import com.github.common.components.number.rule.loader.RuleNotSetException;
import com.github.common.components.number.sequence.SequenceGenerator;
import com.github.common.components.number.rule.domain.NumberRules;
import com.github.common.components.number.rule.domain.RuleTypeEnum;
import com.github.common.components.util.lang.AssertUtil;
import com.github.common.components.util.lang.DateUtil;
import com.github.common.components.util.lang.RandomUtil;
import com.github.common.components.util.lang.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * 编号生成工具,主要功能：解析json,生成编号
 *
 * @author Michael
 */
@Data
public class NumberGeneratorImpl implements NumberGenerator {

  /** 编号生成规则 */
  private NumberRules numberRules;

  /** 序列号生成算法 */
  private SequenceGenerator sequenceGenerator;

  @Override
  public String generate(String bizType) {
    AssertUtil.assertNotEmpty(bizType, "bizType");
    AssertUtil.assertNotEmpty(numberRules, "number generate rules");

    List<NumberRules.NumberRule> bills = getNumberRules().getBills();
    for (NumberRules.NumberRule bizNumRule : bills) {
      if (!bizType.equalsIgnoreCase(bizNumRule.getBill())) {
        continue;
      }
      List<NumberRules.NumberRuleElement> rules = bizNumRule.getRules();
      StringBuffer buffer = new StringBuffer();
      for (NumberRules.NumberRuleElement element : rules) {
        String type = element.getType();
        AssertUtil.assertNotEmpty(type, "type");
        type = type.toUpperCase();
        if (RuleTypeEnum.CONSTANT.getCode().equals(type)) {
          buffer.append(element.getValue()); // 固定值
        } else if (RuleTypeEnum.DATE.getCode().equals(type)) {
          buffer.append(DateUtil.getCurrentDate(element.getLength())); // 时间
        } else if (RuleTypeEnum.SEQUENCE.getCode().equals(type)) {
          long next = sequenceGenerator.generateNext(bizType);
          buffer.append(StringUtil.leftPad(next, element.getLength(), '0'));
        } else if (RuleTypeEnum.RANDOM.getCode().equals(type)) {
          buffer.append(RandomUtil.number(element.getLength()));
        }
      }
      return buffer.toString();
    }
    throw new RuleNotSetException();
  }
}
