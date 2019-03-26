package com.github.common.componets.config;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * 配置中心接口
 *
 * @date 2018/6/22
 */
public interface CommonConfig {

    Set<String> getPropertyNames();

    String getString(String key, String defaultValue);

    Integer getInt(String key, Integer defaultValue);

    Long getLong(String key, Long defaultValue);

    Short getShort(String key, Short defaultValue);

    Float getFloat(String key, Float defaultValue);

    Double getDouble(String key, Double defaultValue);

    Byte getByte(String key, Byte defaultValue);

    Boolean getBoolean(String key, Boolean defaultValue);

    String[] getArray(String key, String delimiter, String[] defaultValue);

    Date getDate(String key, Date defaultValue);

    Date getDate(String key, String format, Date defaultValue);

    Date getDate(String key, String format, Locale locale, Date defaultValue);

    <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defaultValue);

    long getDuration(String key, long defaultValue);

}