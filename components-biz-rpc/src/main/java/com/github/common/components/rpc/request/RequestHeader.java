package com.github.common.components.rpc.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求头
 *
 * @date 2018/7/18
 */
@Data
public class RequestHeader implements Serializable {
    /**
     * 唯一的id，用于避免重复提交
     */
    private String id;

    /**
     * 全局调用链id
     */
    private String chainId;

    /**
     * 请求来源
     */
    private int source;

    /**
     * 时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    private String appId;

    private String sign;
}