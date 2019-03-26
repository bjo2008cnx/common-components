package com.github.biz;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AopTest {

  private ApplicationContext ac = new ClassPathXmlApplicationContext("aop-service.xml");

  @Test
  public void test01() {
    ServiceY dao = (ServiceY) ac.getBean("serviceY");
    dao.execute("hello");
  }
}
