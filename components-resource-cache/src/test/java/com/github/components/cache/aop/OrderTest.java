package com.github.components.cache.aop;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GlobalConfig.class)
public class OrderTest {
  @Autowired private OrderService orderService;

  @Test
  public void test() {
    System.out.println(orderService.getOrder(1L));
  }
}
