package com.github.common.componets.ds.core;

import lombok.Data;

import javax.sql.DataSource;

/**
 * DataSource
 *
 * @date 2018/8/18
 */
@Data
public class DataSourceBean {
    private String beanName;

    private String namespace;

    private DataSource dataSource;

    private String databaseUrl;
}