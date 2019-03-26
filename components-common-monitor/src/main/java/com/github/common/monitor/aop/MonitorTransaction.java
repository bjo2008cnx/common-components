package com.github.common.monitor.aop;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 监控Transaction。收集方法的耗时、执行次数等信息
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface MonitorTransaction {

}
