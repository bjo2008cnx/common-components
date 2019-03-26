package com.github.common.componets.config.encrypt;

import java.util.regex.Pattern;

/**
 * 加密常量
 *
 * @author
 * @date 2018/8/25
 */
public class EncryptConstant {
    public static String KEY_PASSWORD = "password";

    /**
     * 默认密钥
     */
    static String DEFAULT_PASSWORD = "8sLxdL23Pxd9FxDaKaX2CxW";
    /**
     * 制表符、空格、换行符 PATTERN
     */
    static Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");
    /**
     * 加密算法
     */
    static String ALGORITHM = "PBEWithMD5AndDES";
}