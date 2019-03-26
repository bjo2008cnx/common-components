package com.github.biz;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@Configuration // 表明是配置类
@EnableAspectJAutoProxy // 启用AspectJ自动代理
@ComponentScan(basePackages = "com.github.biz;com.github.common.components.log") // 扫描基础包
@ImportResource(locations = "aop-service.xml")
public class GlobalConfig {}
