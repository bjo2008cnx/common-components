package com.github.common.components.rpc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHeader<T> implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 响应码
     */
    private long code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 用于跟踪链路的全局id
     */
    private String chainId;

    /**
     * 时间戳
     */
    private final String timestamp = String.valueOf(System.currentTimeMillis());

}
