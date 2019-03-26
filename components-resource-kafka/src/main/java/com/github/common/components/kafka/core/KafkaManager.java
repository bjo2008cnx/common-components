package com.github.common.components.kafka.core;

import com.github.common.components.kafka.data.ConsumeCommonRequest;
import com.github.common.components.kafka.exception.CommitFailException;
import com.github.common.components.util.constant.CharConstant;
import com.github.common.components.util.io.StreamUtil;
import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Kafka工具类
 *
 */
@Slf4j
public class KafkaManager {
    public static final char SEPARATOR = CharConstant.EXCLAMATION_MARK;

    /**
     * 订阅主题或分区
     *
     * @param instance   KafkaConsumer
     * @param topic      主题
     * @param partitions 分区 Integer[]
     */
    public static void subscribe(KafkaConsumer instance, String topic, Integer[] partitions) {
        //指定分区
        if (partitions != null) {
            List<TopicPartition> topicPartitions = new ArrayList();
            for (Integer partition : partitions) {
                TopicPartition topicPartition = new TopicPartition(topic, partition);
                topicPartitions.add(topicPartition);
            }
            instance.assign(topicPartitions);
        } else {
            instance.subscribe(Collections.singletonList(topic), new RebalanceListener(instance));
        }
    }

    /**
     * 仅提交
     *
     * @param consumer KafkaConsumer
     */
    public static void commit(KafkaConsumer consumer) {
        try {
            consumer.commitSync();
        } catch (Throwable t) {
            log.error("fail to commit", t);
            throw new CommitFailException("fail to commit", t);
        }
    }

    /**
     * 提交并关闭
     *
     * @param consumer KafkaConsumer
     */
    public static void commitAndClose(KafkaConsumer consumer) {
        try {
            consumer.commitSync();
        } catch (Throwable t) {
            log.error("fail to commit", t);
            throw new CommitFailException("fail to commit", t);
        } finally {
            StreamUtil.close(consumer);
        }
    }

    /**
     * 继续消费
     *
     * @param consumer
     */
    public static void resume(KafkaConsumer consumer) {
        log.info("Resume consuming [start]");
        consumer.resume(consumer.paused());
        log.info("Resume consuming [end]");
    }


    /**
     * 构造 key
     * 将斜杠'/'替换掉
     *
     * @param request ConsumeCommonRequest
     */
    public static String buildKeyWithoutSlash(ConsumeCommonRequest request) {
        StringBuilder keys = StringUtil.append(SEPARATOR,
                request.getServer(),
                replaceSlash(request.getGroupId()),
                replaceSlash(request.getTopic()));
        if (request.getPartitions() != null) {
            keys.append(StringUtil.join(Arrays.asList(request.getPartitions())));
        }
        if (log.isDebugEnabled()) {
            log.debug("ConsumeCommonRequest key is:" + keys);
        }
        return keys.toString();
    }

    private static String replaceSlash(String target) {
        return target.replaceAll("/", "-");
    }


    /**
     * 重置offset为committed offset。KafkaConsumerRebalanceListener中重新分配分区时需要调用此方法时行重置。
     *
     * @param consumer
     * @param topicPartitions
     */
    public static void resetToCommitedOffset(KafkaConsumer consumer, Collection<TopicPartition> topicPartitions) {
        //rebalanced之后 获取新的分区，获取最新的偏移量，设置拉取分量
        for (TopicPartition partition : topicPartitions) {
            //向协调者发送获取请求,获取已提交的偏移量
            OffsetAndMetadata committedOffset = consumer.committed(partition);
            //设置本地拉取分量，下次拉取消息以这个偏移量为准
            if (committedOffset != null) {
                long offset = committedOffset.offset();
                consumer.seek(partition, offset);
                log.warn(StringUtil.joinPairs("topic", partition.topic(), "partition", partition.partition(), "reset offset", offset));
            }
        }
    }

    /**
     * 暂停消费
     *
     * @param consumer
     * @param topicPartitions
     */
    public static void pause(KafkaConsumer consumer, Collection<TopicPartition> topicPartitions) {
        log.warn("To be rebalanced topic and partitions:" + buildLog(topicPartitions));
        log.warn("Pause polling and waiting for clients to commit.");
        consumer.pause(topicPartitions);
    }

    /**
     * 构建Log对象
     *
     * @param topicPartitions
     * @return
     */
    public static String buildLog(Collection<TopicPartition> topicPartitions) {
        StringBuilder builder = new StringBuilder();
        for (TopicPartition partition : topicPartitions) {
            builder.append(StringUtil.joinPairs("topic", partition.topic(), "partition", partition.partition()));
        }
        return builder.toString();
    }
}