package com.github.components.springcloud.dynamic;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ZuulPropertiesRefresher implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ZuulPropertiesRefresher.class);
    public static final String PREFIX_ZUUL = "zuul.";

    private ApplicationContext applicationContext;

    @Autowired
    private RouteLocator routeLocator;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean zuulPropertiesChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith(PREFIX_ZUUL)) {
                zuulPropertiesChanged = true;
                break;
            }
        }
        if (zuulPropertiesChanged) {
            refreshZuulProperties(changeEvent);
        }
    }

    private void refreshZuulProperties(ConfigChangeEvent changeEvent) {
        //重新绑定配置
        // @see ConfigurationPropertiesRebinder#onApplicationEvent
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));

        // 刷新路由
        //see ZuulServerAutoConfiguration.ZuulRefreshListener#onApplicationEvent
        this.applicationContext.publishEvent(new RoutesRefreshedEvent(routeLocator));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
