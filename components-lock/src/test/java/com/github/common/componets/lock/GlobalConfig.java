package com.github.common.componets.lock;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import redis.clients.jedis.JedisPool;

@Configuration // 表明是配置类
@EnableAspectJAutoProxy // 启用AspectJ自动代理
@ComponentScan(basePackages = "com.github.common.componets.lock") // 扫描基础包
public class GlobalConfig {

  @Bean
  public JedisPool jedisPool() {
    GenericObjectPoolConfig gop = new GenericObjectPoolConfig();
    gop.setMaxTotal(100);
    return new JedisPool(gop, "192.168.80.113", 6379,
        10000,
        "k7lla2rL7qdR9vP2", 0);
  }
}
