package com.github.common.components.kafka.core;

import com.github.common.components.kafka.data.ConsumerConstant;
import com.github.common.components.util.lang.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * rebalance  监听器
 */
@Slf4j
public class RebalanceListener implements ConsumerRebalanceListener {
    private KafkaConsumer consumer;
    private volatile boolean isPaused = false;

    public RebalanceListener(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * rebalanced 之前的操作
     *
     * @param topicPartitions
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> topicPartitions) {
        long startTime = System.currentTimeMillis();
        if (topicPartitions == null || topicPartitions.size() == 0) {
            log.info("no partitions to be revoked.");
        } else {
            KafkaManager.pause(consumer, topicPartitions);
            isPaused = true;
            //休眠一段时间，等待未提交的client提交
            ThreadUtil.sleepSeconds(ConsumerConstant.RABALANCE_WAIT_COMMIT_SECONDS);
            log.warn("Rebalanced sleeping ended.");
        }
        long timeSpent = System.currentTimeMillis() - startTime;
        log.info("onPartitionsRevoked TIME SPENT :{}", timeSpent );
    }

    /**
     * rebalanced 之后的操作
     *
     * @param topicPartitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> topicPartitions) {
        long startTime = System.currentTimeMillis();
        log.info("Partitions assigned." + KafkaManager.buildLog(topicPartitions));

        try {
            KafkaManager.resetToCommitedOffset(consumer, topicPartitions);
        } finally {
            //继续消费已暂停的分区
            if (isPaused) {
                KafkaManager.resume(consumer);
                isPaused = false;
            }
        }
        long timeSpent = System.currentTimeMillis() - startTime;
        log.info("onPartitionsAssigned TIME SPENT :{}", timeSpent );
    }
}