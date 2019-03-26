package com.github.common.components.number.generator;

import com.github.common.components.number.sequence.SequenceGenerator;
import com.github.common.components.number.rule.domain.NumberRules;

/**
 * 编号生成工具
 *
 * @author michael
 */
public interface NumberGenerator {
  String generate(String bizType);

  void setSequenceGenerator(SequenceGenerator sequenceGenerator);

  void setNumberRules(NumberRules numberRules);
}
