package com.github.common.componets.config.util;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.internals.DefaultConfig;
import com.github.common.components.util.base.PopulateUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Apollo工具类
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
public class ApolloUtil {

    /**
     * 拷贝对象
     *
     * @param namespace
     * @param <T>
     * @return
     */
    public static <T> T populate(String namespace, Class<T> clazz) {
        Config config = ConfigService.getConfig(namespace);
        Map<String, Object> map = getPropertiesMap(config);
        return PopulateUtil.map2Obj(map, clazz);
    }

    /**
     * 拷贝对象
     *
     * @param namespace
     * @param <T>
     * @return
     */
    public static <T> T populate(String namespace, String prefix, Class<T> clazz) {
        Config config = ConfigService.getConfig(namespace);
        Map<String, Object> map = getPropertiesMap(config);
        return PopulateUtil.map2Obj(map, prefix, clazz);
    }

    /**
     * 获取全部属性的map
     *
     * @param config
     * @return
     */
    public static Map<String, Object> getPropertiesMap(Config config) {
        Map<String, Object> map = new HashMap<>();
        for (String propertyName : config.getPropertyNames()) {
            map.put(propertyName, config.getProperty(propertyName, ""));
        }
        return map;
    }

    /**
     * 获取属性
     *
     * @param config
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Properties getProperties(Config config) {
        if (!(config instanceof DefaultConfig)) {
            log.error("fail to populate. Not instance of DefaultConfig.");
            throw new RuntimeException("fail to populate.");
        }
        DefaultConfig defaultConfig = (DefaultConfig) config;
        Properties properties = null;
        try {
            //获取内置的属性
            Field field = defaultConfig.getClass().getDeclaredField("m_configProperties");
            field.setAccessible(true);
            AtomicReference<Properties> ref = (AtomicReference<Properties>) field.get(defaultConfig);
            properties = ref.get();
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("fail to get properties", e);
        }
        return properties;
    }


}