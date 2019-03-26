package com.github.common.componets.lock;

import com.github.common.componets.lock.annotation.DistributedLock;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: luffy
 * @create: 2018-10-19 09:16
 **/
@Service
public class TestService {

  @DistributedLock(key = "#i",value = "ssss")
  public void lockTest(int i) {
    System.out.println("success");
    try {
      Thread.sleep(10l);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
