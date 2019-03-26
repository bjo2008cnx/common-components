package com.github.common.components.dlog.provider;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.github.common.components.dlog.LogProvider;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Logback工具类
 *
 * @author
 * @date 2018/8/23
 */
public class LogbackProvider implements LogProvider<Logger> {
    /**
     * 获取全部Logger对象
     *
     * @return
     */
    public Map<String, Logger> fetchAllLoggers() {
        Map loggerMap = new HashMap();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        //增加logger
        loggerContext.getLoggerList().stream().filter(logger -> logger.getLevel() != null).forEach(logger -> {
            loggerMap.put(logger.getName(), logger);
        });

        //增加root logger
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        loggerMap.put(rootLogger.getName(), rootLogger);
        return loggerMap;
    }

    @Override
    public void refresh(String name, String logLevel) {

    }
}