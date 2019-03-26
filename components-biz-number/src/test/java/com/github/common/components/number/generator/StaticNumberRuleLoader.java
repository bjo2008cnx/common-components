package com.github.common.components.number.generator;

import com.github.common.components.number.rule.domain.NumberRules;
import com.github.common.components.number.rule.loader.NumberRuleLoader;
import com.github.common.components.rpc.serializer.JsonUtil;

/**
 * 规则加载器，使用方法配置
 *
 * @author Michael
 */
public class StaticNumberRuleLoader implements NumberRuleLoader {

  public static NumberRules numberRules;

  /**
   * 加载规则
   *
   * @return
   */
  public NumberRules load() {
    String str = BillNumRuleSetting.RULES.replaceAll("'", "\"");
    NumberRules billNumRules = JsonUtil.parseJson(str, NumberRules.class);
    return billNumRules;
  }
}
