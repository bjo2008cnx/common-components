package com.github.common.components.util.io;


import com.github.common.components.util.lang.AssertUtil;
import com.github.common.components.util.lang.ExceptionUtil;

import java.io.*;
import java.util.Properties;

/**
 * 属性文件工具
 *
 * @author Wangxm
 * @date 2016/5/5
 */
public class PropertiesUtil {
    /**
     * 从类路径加载属性文件
     *
     * @param filePath
     * @return
     */
    public static Properties loadByClassPath(String filePath) {
        AssertUtil.assertNotNullOrEmpty("filePath",filePath);
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            throw ExceptionUtil.transform(e);
        }
        return properties;
    }

    /**
     * 从本地加载属性文件
     *
     * @param filePath
     * @return
     */
    public static Properties load(String filePath) {
        AssertUtil.assertNotNullOrEmpty("filePath",filePath);
        Properties properties = new Properties();
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);
            properties.load(reader);
        } catch (IOException e) {
            throw ExceptionUtil.transform(e);
        } finally {
            StreamUtil.close(reader, fileReader);
        }
        return properties;
    }

    /**
     * 从系统属性文件中获取相应的值
     *
     * @param key key
     * @return 返回value
     */
    public static String getSystemKey(String key) {
        AssertUtil.assertNotNullOrEmpty("key",key);
        return System.getProperty(key);
    }

    /**
     * 写入Properties信息
     *
     * @param filePath 写入的属性文件
     * @param key     属性名称
     * @param value   属性值
     */
    public static void write(String filePath, String key, String value) throws IOException {
        AssertUtil.assertNotNullOrEmpty("filePath",filePath);
        AssertUtil.assertNotNullOrEmpty("key",key);
        AssertUtil.assertNotNullOrEmpty("value",value);
        Properties props = new Properties();
        props.load(new FileInputStream(filePath));

        // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream fos = new FileOutputStream(filePath);
        props.setProperty(key, value);

        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        props.store(fos, "Update '" + key + "' value");
    }
}
