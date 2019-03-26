package com.github.common.componets.lock;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AnnotatedLockTest
 *
 * @author
 * @date 2018/10/20
 */

public class AnnotatedLockTest extends BaseJunit4Test {

  @Autowired
  private TestService service;

  /**
   * 测试每次lock都使用不同key的场景
   */
  @Ignore
  @Test
  public void testLock() {
    for (int i = 0; i < 10; i++) {
      service.lockTest(i);
    }
  }

  /**
   * 测试每次lock都使用相同key的场景
   */
  @Test
  public void testLock_SameId() {
    for (int i = 0; i < 10; i++) {
      service.lockTest(33);
    }
  }
}