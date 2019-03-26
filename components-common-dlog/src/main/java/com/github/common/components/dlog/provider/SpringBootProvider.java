package com.github.common.components.dlog.provider;

import com.github.common.components.dlog.LogProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * SpringBootLog实现
 *
 * @author
 * @date 2018/8/23
 */
@Service
public class SpringBootProvider implements LogProvider {

    @Autowired
    private LoggingSystem loggingSystem;

    @Override
    public Map fetchAllLoggers() {
        return null;
    }

    @Override
    public void refresh(String name, String logLevel) {
        LogLevel level = LogLevel.valueOf(logLevel);
        loggingSystem.setLogLevel(name, level);
    }
}