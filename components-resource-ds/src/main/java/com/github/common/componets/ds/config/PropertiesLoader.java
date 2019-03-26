package com.github.common.componets.ds.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.github.common.components.util.lang.StringUtil;

import java.util.Properties;

/**
 * PropertiesLoader
 *
 * @author arch
 * @date 2018/8/18
 */
public class PropertiesLoader {

    /**
     * 从配置中心加载配置文件
     *
     * @return
     */
    public static Properties loadProperties(String namespace, String beanName) {
        Properties props = new Properties();

        //namespace
        String injectNamespace = StringUtil.isEmpty(namespace) ? beanName : namespace;
        Config config = ConfigService.getConfig(injectNamespace);

        //构建配置文件
        for (String key : config.getPropertyNames()) {
            props.put(key, config.getProperty(key, ""));
        }

        if (props.isEmpty()) {
            throw new RuntimeException("Can't load dataSource [" + beanName + ".properties] from config center!");
        }
        return props;
    }
}