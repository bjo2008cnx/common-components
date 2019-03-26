package com.github.common.components.number.generator;

import com.github.common.components.number.sequence.RandomSequenceGenerator;

/**
 * 默认单据编号生成器，采用随机数作为序号生成器
 *
 * @author Michael
 */
public class DefaultBillNumGeneratorFacade {
  private static NumberGenerator generator;

  static {
    generator = new NumberGeneratorImpl();
    generator.setSequenceGenerator(new RandomSequenceGenerator());
  }

  /**
   * 根据业务类型获取编号
   *
   * @param bizType 业务类型，即表名
   * @return 单据编号
   */
  public static String generateNum(String bizType) {
    return generator.generate(bizType);
  }
}
