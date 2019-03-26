package com.github.common.components.log.dynamic.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Content :
 */
@Slf4j
public class CommonPrintLogger {

    public static void print() throws Exception {
        Callable<Object> callable = () -> {
            while (true) {
                log.info("我是info级别日志");
                log.error("我是error级别日志");
                log.warn("我是warn级别日志");
                log.debug("我是debug级别日志");
                TimeUnit.SECONDS.sleep(2);
            }
        };
        Executors.newSingleThreadExecutor().submit(callable);
    }
}
