package com.github.common.components.retryer.impl;

import com.github.rholder.retry.*;
import com.github.common.components.retryer.CommonRetryer;
import com.github.common.components.retryer.config.RetryConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Guava 重试实现
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
public class GuavaRetryer implements CommonRetryer {
    @Override
    public void call(Callable callable, RetryConfig config) {
        if (!Boolean.TRUE.equals(config.isRetryEnabled())) {
            config.setRetryTimes(0);
        }
        Retryer<Boolean> retryer = buildConfig(config);
        try {
            retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            log.error("fail to retry", e);
        }
    }

    /**
     * 构建Retryer对象
     *
     * @param config
     * @return
     */
    private Retryer<Boolean> buildConfig(RetryConfig config) {
        RetryerBuilder<Boolean> builder = RetryerBuilder.newBuilder();
        if (config.isExceptionRetry()) {
            //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
            builder.retryIfException();
        }
        //重调策略
        if (config.getRetryIntervalMs() != null) {
            builder.withWaitStrategy(WaitStrategies.fixedWait(config.getRetryIntervalMs(), TimeUnit.MILLISECONDS));
        }
        //尝试次数
        if (config.getRetryTimes() != null) {
            builder.withStopStrategy(StopStrategies.stopAfterAttempt(config.getRetryTimes())).build();
        }
        return builder.build();
    }
}