package com.github.common.componets.ds.core;

/**
 * DataSourceConstant
 *
 * @author arch
 * @date 2018/8/18
 */
public interface DataSourceConstant {
    /**
     * 驱动名
     */
    String DRIVER_CLASS_NAME = "driverClassName";
    /**
     * 数据源类名
     */
    String DATASOURCE_CLASS_NAME = "dataSourceClassName";
    /**
     * 加密密码属性字段名
     */
    String PASSWORD_ENCRYPT_COLUMN = "password.encrypt";

    /**
     * jdbc
     */
    String JDBC = "jdbc.";

    /**
     * 加密key
     */
    String ENCRYPT_KEY_DATA = "encrypt.key.data";
}