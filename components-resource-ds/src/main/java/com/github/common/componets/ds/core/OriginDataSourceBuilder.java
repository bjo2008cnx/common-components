package com.github.common.componets.ds.core;

import com.github.common.components.util.lang.AssertUtil;
import com.github.common.componets.ds.util.EncryptUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 原生数据源加载
 *
 * @date 2018/8/18
 */
public class OriginDataSourceBuilder {

    public static final String PASSWORD = "password";

    /**
     * 加载数据源
     *
     * @param properties
     * @return
     */
    public static DataSource load(Properties properties) {
        try {
            //加载jdbc驱动
            String driverClassName = properties.getProperty(DataSourceConstant.JDBC + DataSourceConstant.DRIVER_CLASS_NAME);
            AssertUtil.assertNotEmpty(driverClassName, "driverClassName from config center");
            Class.forName(driverClassName, true, OriginDataSourceBuilder.class.getClassLoader()).newInstance();

            //加载数据源
            String dataSourceClassName = properties.getProperty(DataSourceConstant.JDBC + DataSourceConstant.DATASOURCE_CLASS_NAME);
            DataSource dataSource = (DataSource) Thread.currentThread().getContextClassLoader().loadClass(dataSourceClassName).newInstance();
            //初始化
            setDataSourceProperties(dataSource, properties);
            return dataSource;
        } catch (Exception e) {
            throw new RuntimeException("Can't load datasource class.", e);
        }
    }

    /**
     * 设置属性
     *
     * @param dataSource
     * @param properties
     * @throws Exception
     */
    private static void setDataSourceProperties(DataSource dataSource, Properties properties) throws Exception {
        BeanWrapper dataSourceWrapper = new BeanWrapperImpl(dataSource);
        for (Object obj : properties.keySet()) {
            String key = (String) obj;
            if (!key.startsWith(DataSourceConstant.JDBC)) {
                continue;
            }
            key = key.substring(DataSourceConstant.JDBC.length());
            switch (key) {
                case DataSourceConstant.DATASOURCE_CLASS_NAME:
                    continue;
                case DataSourceConstant.DRIVER_CLASS_NAME:
                    continue;
                case DataSourceConstant.PASSWORD_ENCRYPT_COLUMN:
                    //从启动参数中获取secret
                    String secret = System.getProperty(DataSourceConstant.ENCRYPT_KEY_DATA);
                    String value = properties.get(key).toString();
                    String propValue = secret == null ? EncryptUtil.decrypt(value) : EncryptUtil.decrypt(value, secret);
                    dataSourceWrapper.setPropertyValue(PASSWORD, propValue);
                    break;
                default:
                    dataSourceWrapper.setPropertyValue(key, properties.get(key));
                    break;
            }
        }
    }
}