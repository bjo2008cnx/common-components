package com.github.common.components.kafka.data;

/**
 * 常量
 */
public class ConsumerConstant {

    public static final int PAGE_SIZE = 50;

    //消费超时，单位:ms
    public static final int POLL_TIMEOUT = 100;

    //自动确认的时间间隔
    public static final int AUTO_COMMIT_INTERVAL = 1000;

    /**
     * rebalanced 之前等待client提交Offset的时间.
     */
    public static final int RABALANCE_WAIT_COMMIT_SECONDS = 2;
}