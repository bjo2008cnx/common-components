package com.github.common.componets.dds.context;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.github.common.componets.dds.DataSourceManager;
import com.github.common.componets.dds.DynamicDataSource;
import com.github.common.componets.dds.task.DataSourceStopTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据源刷新工具
 */
@Slf4j
@Component
public class DataSourceRefresher implements ApplicationContextAware {

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private ApplicationContext applicationContext;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean dataSourceConfigChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("spring.datasource.")) {
                dataSourceConfigChanged = true;
                break;
            }
        }

        if (dataSourceConfigChanged) {
            refreshDataSource(changeEvent.changedKeys());
        }
    }

    /**
     * 刷新数据源
     *
     * @param changedKeys
     */
    private synchronized void refreshDataSource(Set<String> changedKeys) {
        try {
            log.info("Refreshing data source");

            // 重新绑定配置的bean, 例如：DataSourceProperties
            // @see org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder#onApplicationEvent
            this.applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));

            DataSource newDataSource = dataSourceManager.createAndTestDataSource();
            DataSource oldDataSource = dynamicDataSource.setDataSource(newDataSource);
            asyncTerminate(oldDataSource);

            log.info("Finished refreshing data source");
        } catch (Throwable ex) {
            log.error("Refreshing data source failed", ex);
        }
    }

    private void asyncTerminate(DataSource dataSource) {
        DataSourceStopTask task = new DataSourceStopTask(dataSource, scheduledExecutorService);

        //启动任务
        scheduledExecutorService.schedule(task, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
