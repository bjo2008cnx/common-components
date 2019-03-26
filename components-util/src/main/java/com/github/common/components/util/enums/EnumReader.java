package com.github.common.components.util.enums;


import com.github.common.components.util.lang.ObjectUtil;

/**
 * 枚举帮助类
 */
public final class EnumReader {

    /**
     * 根据code查找枚举值
     *
     * @param clazz 枚举类的实例
     * @param key   待查找的key
     * @param <K>   Key的类型，一般是Int或String
     * @param <E>   具体s的枚举类
     * @return 枚举值
     */
    public static <K, E extends CodedEnum<K>> E getEnumByCode(Class<E> clazz, K key) {
        if (key == null) {
            return null;
        }

        if (!Enum.class.isAssignableFrom(clazz)) {
            return null;
        }
        E[] enumConstants = clazz.getEnumConstants();//all enum constants
        if (enumConstants != null) {
            for (E e : enumConstants) {
                if (ObjectUtil.equal(e.getCode(), key)) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 根据Desc获取enum
     *
     * @param clazz 枚举类的实例
     * @param desc  待查找的desc
     * @param <K>   Key的类型，一般是Int或String
     * @param <E>   具体s的枚举类
     * @return 枚举值
     */
    public static <K, E extends CodedEnum<K>> E getEnumByDesc(Class<E> clazz, String desc) {
        if (desc == null) {
            return null;
        }

        if (!Enum.class.isAssignableFrom(clazz)) {
            return null;
        }
        //all enum constants
        E[] enumConstants = clazz.getEnumConstants();
        if (enumConstants != null) {
            for (E e : enumConstants) {
                if (ObjectUtil.equal(e.getDesc(), desc)) {
                    return e;
                }
            }
        }
        return null;
    }

    public static String getAll(Class<? extends CodedEnum> clazz) {
        StringBuilder builder = new StringBuilder();
        CodedEnum[] codedEnumConstants = clazz.getEnumConstants();
        if (null != codedEnumConstants && codedEnumConstants.length > 0) {
            for (int i = 0; i < codedEnumConstants.length; i++) {
                CodedEnum e = codedEnumConstants[i];
                builder.append(e.getCode()).append(":").append(e.getDesc());
                if (i != codedEnumConstants.length - 1) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }
}
