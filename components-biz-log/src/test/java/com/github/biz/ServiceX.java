package com.github.biz;

import com.github.common.components.log.annotation.CallLog;
import org.springframework.stereotype.Service;

/**
 * 测试类
 *
 * @author
 * @date 2018/9/12
 */
@Service
public class ServiceX {
  @CallLog
  public void execute(String a, Integer b) {
    System.out.println("service x is called.");
  }
}
