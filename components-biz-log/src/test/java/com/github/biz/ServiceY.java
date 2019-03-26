package com.github.biz;

import com.github.common.components.log.annotation.CallLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试类
 *
 * @author
 * @date 2018/9/12
 */
@Service
public class ServiceY {
  @Autowired private ServiceX serviceX;

  @CallLog
  public void execute(String c) {
    System.out.println("service y is called.");
    serviceX.execute("wu.kong.sun", 1000);
  }
}
