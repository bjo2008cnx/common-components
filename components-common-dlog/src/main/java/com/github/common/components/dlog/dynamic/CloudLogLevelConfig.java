package com.github.common.components.dlog.dynamic;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * springcloud 动态日志配置.将log level存储于apollo,可在运行时动态修改日志级别.
 * 业务方需要扫描到该路径
 */
@Service
public class CloudLogLevelConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @ApolloConfig
    private Config config;

    @PostConstruct
    private void initialize() {
        refreshLoggingLevels(config.getPropertyNames());
    }

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels(changeEvent.changedKeys());
    }

    private void refreshLoggingLevels(Set<String> changedKeys) {
        boolean loggingLevelChanged = false;
        for (String changedKey : changedKeys) {
            if (changedKey.startsWith("logging.level.")) {
                loggingLevelChanged = true;
                break;
            }
        }

        if (loggingLevelChanged) {
            System.out.println("Refreshing logging levels");
            //refresh logging levels 参考 org.springframework.cloud.logging.LoggingRebinder#onApplicationEvent
            this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));
            System.out.println("Logging levels refreshed");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
