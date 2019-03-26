package com.github.common.components.retryer;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试工具类
 */
@Slf4j
public class RetryUtil {
    /**
     * 重试
     *
     * @param action
     * @param <T>
     * @return
     */
    public static <T> T retry(Callable<T> action, int retryIntervalMills, int retryTimes) {
        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfException()
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(retryIntervalMills, TimeUnit.MILLISECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryTimes))
                .build();

        try {
            T response = retryer.call(action);
            return response;
        } catch (Exception e) {
            log.error("fail to call action", e);
            throw new RuntimeException(e);
        }
    }
}
