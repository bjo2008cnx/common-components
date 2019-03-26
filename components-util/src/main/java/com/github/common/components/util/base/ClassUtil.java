package com.github.common.components.util.base;

/**
 * class相关的工具类
 *
 * @author
 * @date 2018/8/23
 */
public class ClassUtil {
    /**
     * 判断类是否存在于ClassLoader。
     *
     * @param name
     * @return
     */
    public static boolean isPresent(String name) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}