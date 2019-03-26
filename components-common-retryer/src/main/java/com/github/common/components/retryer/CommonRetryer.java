package com.github.common.components.retryer;

import com.github.common.components.retryer.config.RetryConfig;

import java.util.concurrent.Callable;

/**
 * CommonRetryer
 *
 * @author
 * @date 2018/9/11
 */
public interface CommonRetryer {
    /**
     * 调用callable对象
     *
     * @param callable
     */
    void call(Callable callable, RetryConfig config);
}