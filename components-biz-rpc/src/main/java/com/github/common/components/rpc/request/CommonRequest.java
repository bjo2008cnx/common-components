package com.github.common.components.rpc.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用请求
 *
 * @date 2018/7/18
 */
@Data
public class CommonRequest<T> implements Serializable {

    /**
     * 请求头
     */
    private RequestHeader header;

    /**
     * 请求体
     */
    private T body;
}