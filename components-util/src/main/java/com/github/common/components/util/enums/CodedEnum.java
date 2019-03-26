package com.github.common.components.util.enums;

/**
 * 通用枚举接口
 *
 * @param <K>
 */
public interface CodedEnum<K> {

    K getCode();

    String getDesc();
}
