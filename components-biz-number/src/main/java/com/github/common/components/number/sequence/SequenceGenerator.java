package com.github.common.components.number.sequence;

/**
 * 序列号生成接口
 *
 * @author Michael
 */
public interface SequenceGenerator {
  /**
   * 生成序列号
   *
   * @param bizType
   * @return
   */
  long generateNext(String bizType);
}
