package com.github.common.components.number.sequence;

import com.github.common.components.util.lang.RandomUtil;

/**
 * 随机生成，仅用于测试
 *
 * @author Michael
 */
public class RandomSequenceGenerator implements SequenceGenerator {
  @Override
  public long generateNext(String bizType) {
    return RandomUtil.randomNumber(999999);
  }
}
