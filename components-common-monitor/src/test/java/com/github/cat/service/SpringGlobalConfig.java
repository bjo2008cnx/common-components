package com.github.cat.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // 表明是配置类
@EnableAspectJAutoProxy // 启用AspectJ自动代理
@ComponentScan(basePackages = "com.github.common.monitor.aop,com.github.cat.service") // 扫描基础包
public class SpringGlobalConfig {}
