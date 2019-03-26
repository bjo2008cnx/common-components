package com.github.common.components.dlog.provider;

import com.github.common.components.dlog.LogProvider;
import com.github.common.components.util.lang.StringUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Log4j2工具类
 *
 * @author
 * @date 2018/8/23
 */
public class Log4j2Provider implements LogProvider<LoggerConfig> {
    public static final String DEFAULT_KEY = "root";
    private static Map<String, LoggerConfig> allLoggers;

    /**
     * 获取全部Logger对象
     *
     * @return
     */
    public Map<String, LoggerConfig> fetchAllLoggers() {
        Map<String, LoggerConfig> loggerMap = new HashMap<>();
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
        for (LoggerConfig loggerConfig : map.values()) {
            String key = loggerConfig.getName();
            if (StringUtil.isEmpty(key)) {
                key = DEFAULT_KEY;
            }
            loggerMap.put(key, loggerConfig);
        }
        return loggerMap;
    }

    @Override
    public void refresh(String name, String logLevel) {
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Map<String, LoggerConfig> map = loggerContext.getConfiguration().getLoggers();
        for (LoggerConfig loggerConfig : map.values()) {
            if (name.equals(loggerConfig.getName())) {
                Level targetLevel = Level.toLevel(logLevel);
                loggerConfig.setLevel(targetLevel);
                break;
            }
        }
    }
}