package com.github.common.components.dlog.provider;

import com.github.common.components.dlog.constant.LogConstant;
import com.github.common.components.dlog.constant.LogTypeEnum;
import org.slf4j.impl.StaticLoggerBinder;


/**
 * Slf4j工具类
 *
 * @author
 * @date 2018/8/23
 */
public class Slf4jAbstractProvider {
    /**
     * 检测slf4j 绑定的Log类型
     *
     * @return
     */
    public static LogTypeEnum check() {
        String type = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        if (LogConstant.LOG4J_LOGGER_FACTORY.equals(type)) {
            return LogTypeEnum.LOG4J;
        } else if (LogConstant.LOGBACK_LOGGER_FACTORY.equals(type)) {
            return LogTypeEnum.LOGBACK;
        } else if (LogConstant.LOG4J2_LOGGER_FACTORY.equals(type)) {
            return LogTypeEnum.LOG4J2;
        } else {
            return LogTypeEnum.UNKNOWN;
        }
    }
}