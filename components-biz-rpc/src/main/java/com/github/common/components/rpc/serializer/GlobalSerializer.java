package com.github.common.components.rpc.serializer;

import java.lang.reflect.Type;

/**
 * 全局序列化工具，统一使用该类进行序列化
 *
 * @date 2018/6/16
 */
public class GlobalSerializer {
    /**
     * 解析json
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<?> clazz) {
        return JsonUtil.parseJson(json, clazz);
    }

    public static <T> T parseObject(String json, Type type) {
        return JsonUtil.parseObject(json, type);
    }

    /**
     * 转成json
     *
     * @param content
     * @return
     */
    public static String serialize(Object content) {
        return JsonUtil.toJSON(content);
    }
}