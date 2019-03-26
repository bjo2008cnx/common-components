package com.github.common.componets.config.impl;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.github.common.components.util.lang.AssertUtil;
import com.github.common.componets.config.CommonConfig;
import com.github.common.componets.config.encrypt.EncryptEnvUtil;
import lombok.Getter;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * apollo的配置实现。已将各环境的配置中心地址固定在该client中，业务方不需要设置apollo配置中心的地址
 * 调用端调用时需要配置：1.环境变量,指明当前是哪个环境 2.appId
 *
 * @date 2018/6/22
 */
public class ApolloConfigImpl implements CommonConfig {
    /**
     * apollo Config对象
     */
    @Getter
    private Config config;

    public ApolloConfigImpl(String namespace) {
        //初始化
        this.config = namespace == null ? ConfigService.getAppConfig() : ConfigService.getConfig(namespace);
        //设置环境变量
        EncryptEnvUtil.setPasswordEnv();
    }

    @Override
    public String getString(String key, String defaultValue) {
        checkArguments(key, defaultValue);
        return config.getProperty(key, defaultValue);
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        checkArguments(key, defaultValue);
        return config.getIntProperty(key, defaultValue);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        checkArguments(key, defaultValue);
        return config.getLongProperty(key, defaultValue);
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        checkArguments(key, defaultValue);
        return config.getShortProperty(key, defaultValue);
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        checkArguments(key, defaultValue);
        return config.getFloatProperty(key, defaultValue);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        checkArguments(key, defaultValue);
        return config.getDoubleProperty(key, defaultValue);
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        checkArguments(key, defaultValue);
        return config.getByteProperty(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        checkArguments(key, defaultValue);
        return config.getBooleanProperty(key, defaultValue);
    }

    @Override
    public String[] getArray(String key, String delimiter, String[] defaultValue) {
        checkArguments(key, defaultValue);
        return config.getArrayProperty(key, delimiter, defaultValue);
    }

    @Override
    public Date getDate(String key, Date defaultValue) {
        checkArguments(key, defaultValue);
        return config.getDateProperty(key, defaultValue);
    }

    @Override
    public Date getDate(String key, String format, Date defaultValue) {
        checkArguments(key, defaultValue);
        return config.getDateProperty(key, defaultValue);
    }

    @Override
    public Date getDate(String key, String format, Locale locale, Date defaultValue) {
        checkArguments(key, defaultValue);
        return config.getDateProperty(key, defaultValue);
    }

    @Override
    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defaultValue) {
        checkArguments(key, defaultValue);
        return config.getEnumProperty(key, enumType, defaultValue);
    }

    @Override
    public long getDuration(String key, long defaultValue) {
        checkArguments(key, defaultValue);
        return config.getDurationProperty(key, defaultValue);
    }

    @Override
    public Set<String> getPropertyNames() {
        AssertUtil.assertNotNullOrEmpty(this.config, "namespace");
        return config.getPropertyNames();
    }

    private void checkArguments(String key, Object defaultValue) {
        AssertUtil.assertNotEmpty(key, "key");
        AssertUtil.assertNotNullOrEmpty(this.config, "namespace");
    }
}