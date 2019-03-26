package com.github.common.components.dlog;

import java.util.Map;

/**
 * Log4j抽象类
 *
 * @author
 * @date 2018/8/23
 */
public interface LogProvider<Logger> {
    Map<String, Logger> fetchAllLoggers();

    void refresh(String name, String logLevel);
}