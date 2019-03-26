package com.github.common.components.kafka.data;

import lombok.*;

import java.io.Serializable;

/**
 * 消费请求
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConsumeCommonRequest implements Serializable {
    /**
     * 请求id
     */
    private String id;

    /**
     * 消费者的server
     */
    private String server;

    /**
     * groupId
     */
    private String groupId;

    /**
     * 消费者id，如：消费者的ip
     */
    private String consumerId;

    /**
     * topic
     */
    private String topic;

    /**
     * 分区。如果不为空，顺序需要与offsets的顺序相对应
     */
    private Integer[] partitions;

}