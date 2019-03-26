package com.github.common.components.dlog.dynamic;


import com.github.common.components.dlog.constant.LogTypeEnum;
import com.github.common.components.dlog.provider.Slf4jAbstractProvider;
import com.github.common.components.util.base.ClassUtil;

/**
 * Log类型检测
 *
 * @author
 * @date 2018/8/23
 */
public class LogTypeChecker {

    public static LogTypeEnum check() {
        if (ClassUtil.isPresent("org.slf4j.impl.StaticLoggerBinder")) {
            return Slf4jAbstractProvider.check();
        }
        if (ClassUtil.isPresent("org.apache.log4j.Logger")) {
            return LogTypeEnum.LOG4J;
        }
        if (ClassUtil.isPresent("org.apache.logging.log4j.core.config.LoggerConfig")) {
            return LogTypeEnum.LOG4J2;
        }
        if (ClassUtil.isPresent("ch.qos.logback.classic.Logger")) {
            return LogTypeEnum.LOGBACK;
        }
        return LogTypeEnum.UNKNOWN;
    }
}