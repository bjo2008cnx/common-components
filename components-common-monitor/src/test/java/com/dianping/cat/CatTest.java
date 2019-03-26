package com.dianping.cat;

import com.dianping.cat.message.Transaction;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

/**
 * CatTest
 *
 * @author
 * @date 2018/10/13
 */
@Slf4j
public class CatTest {

  public static void main(String[] args) {
    ThreadLocal local = new ThreadLocal();
    for (int i = 0; i < 10_000_000; i++) {
      execute();
    }
  }

  public static void doService() throws InterruptedException {
    int abs = Math.abs(new Random().nextInt(1000));
    System.out.println(abs);
    abs = abs > 1000 ? 300 : abs;
    Thread.sleep(abs);
  }

  public static void execute() {
    log.info("start to execute.");
    try {
      int x = 10 / 0;
    } catch (Exception e) {
      //测试catAppender是否正确接收log
      //log.error("divide", e);
    }

    Transaction t = Cat.newTransaction("Service", "hi");
    try {
      int abs = Math.abs(new Random().nextInt(1000));
      abs = abs > 1000 ? 300 : abs;
      Thread.sleep(abs);
      //t.setSuccessStatus();
      t.setStatus(Transaction.SUCCESS);

      //用于测试event与Business
      Cat.logEvent("send money", String.valueOf(abs) + " $");
      Cat.logMetricForSum("Money", abs);
    } catch (InterruptedException e) {
      t.setStatus(e);
      log.error("fail to execute", e);
    } finally {
      t.complete();
    }
  }
}