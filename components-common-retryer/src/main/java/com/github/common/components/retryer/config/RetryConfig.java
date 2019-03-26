package com.github.common.components.retryer.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * RetryConfig
 *
 * @author
 * @date 2018/9/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryConfig {
    /**
     * 是否启用重试
     */
    private boolean isRetryEnabled;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 重试间隔的毫秒
     */
    private Integer retryIntervalMs;

    /**
     * 抛出exception时是否重试
     */
    private boolean isExceptionRetry;

    /**
     * 异常列表
     */
    private Set<String> exceptions;
}