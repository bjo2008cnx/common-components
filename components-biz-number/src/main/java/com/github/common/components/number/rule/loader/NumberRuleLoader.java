package com.github.common.components.number.rule.loader;

import com.github.common.components.number.rule.domain.NumberRules;

/**
 * 规则加载器
 *
 * @author Michael
 */
public interface NumberRuleLoader {
  /**
   * 加载规则
   *
   * @return
   */
  NumberRules load();
}
