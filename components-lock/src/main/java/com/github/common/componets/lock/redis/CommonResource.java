package com.github.common.componets.lock.redis;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 通用资源类
 */
@Data
public class CommonResource {
    private GenericObjectPoolConfig config;

    private String host;

    private int port;

    private String instance;

    private String password;

    private int timeout;
}
