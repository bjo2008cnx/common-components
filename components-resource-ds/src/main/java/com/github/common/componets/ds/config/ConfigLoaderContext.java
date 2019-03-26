package com.github.common.componets.ds.config;

import com.github.common.componets.ds.core.OriginDataSourceBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 动态数据源
 *
 * @author arch
 */
@Slf4j
@Data
public class ConfigLoaderContext implements InitializingBean, BeanNameAware {

    private String beanName;

    private String namespace;

    private DataSource dataSource;

    private String databaseUrl;

    /**
     * 初始化操作
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载数据源配置文件
        Properties properties = PropertiesLoader.loadProperties(namespace, beanName);
        // 从配置文件初始化数据源
        dataSource = OriginDataSourceBuilder.load(properties);
        log.info("  DataSource[" + beanName + "] load success!");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
