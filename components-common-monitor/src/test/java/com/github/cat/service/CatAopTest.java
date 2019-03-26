package com.github.cat.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringGlobalConfig.class)
@Slf4j
public class CatAopTest {

  @Autowired
  private UserService userService;

  @Test
  public void test() throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      userService.save(null);
      try {
        userService.update(null);
      } catch (Exception e) {
        //log 会被Log back收集
        log.error("update error", e);
      }
      userService.delete(null);
      userService.getAllObjects();
    }

    Thread.sleep(1000);
  }
}
