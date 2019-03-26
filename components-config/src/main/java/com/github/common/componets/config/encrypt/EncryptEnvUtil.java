package com.github.common.componets.config.encrypt;

/**
 * EncryptEnvUtil
 *
 * @author
 * @date 2018/8/25
 */
public class EncryptEnvUtil {
    /**
     * 设置环境变量
     */
    public static void setPasswordEnv() {
        //TODO 先从文件中取，如果文件中不存在，取默认值

        //取默认值
        System.setProperty("password", EncryptConstant.DEFAULT_PASSWORD);
    }
}