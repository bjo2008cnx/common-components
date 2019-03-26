package com.github.components.cache.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@Configuration // 表明是配置类
@EnableAspectJAutoProxy // 启用AspectJ自动代理
@ComponentScan(
  basePackages =
      "com.github;com.github.common.components.cache.client.impl;com.github.common.components.cache.aop"
) // 扫描基础包
@ImportResource(locations = "applicationContext.redis.xml")
public class GlobalConfig {}
