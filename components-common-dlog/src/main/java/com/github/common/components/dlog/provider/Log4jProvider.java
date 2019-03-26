package com.github.common.components.dlog.provider;

import com.github.common.components.dlog.LogProvider;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Log4j实现
 *
 * @author
 * @date 2018/8/23
 */
public class Log4jProvider implements LogProvider<Logger> {

    /**
     * 获取全部Logger对象
     *
     * @return
     */
    public Map<String, Logger> fetchAllLoggers() {
        Map<String, Logger> loggerMap = new HashMap<>();
        Enumeration enumeration = LogManager.getCurrentLoggers();
        while (enumeration.hasMoreElements()) {
            org.apache.log4j.Logger logger = (org.apache.log4j.Logger) enumeration.nextElement();
            if (logger.getLevel() != null) {
                loggerMap.put(logger.getName(), logger);
            }
        }
        Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
        loggerMap.put(rootLogger.getName(), rootLogger);
        return loggerMap;
    }

    @Override
    public void refresh(String name, String logLevel) {

    }
}