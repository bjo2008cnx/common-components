package com.github.common.componets.dds;

import com.github.common.componets.dds.context.ConfigPropertiesBinder;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源管理器,用于创建数据源
 */
@Slf4j
@Component
public class DataSourceManager {

    @Autowired
    private ConfigPropertiesBinder binder;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * 创建hikari 数据源
     *
     * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.Hikari#dataSource
     */
    public HikariDataSource createDataSource() {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            dataSource.setPoolName(dataSourceProperties.getName());
        }
        Bindable<?> target = Bindable.of(HikariDataSource.class).withExistingValue(dataSource);
        this.binder.bind("spring.datasource.hikari", target);
        return dataSource;
    }

    /**
     * 创建并测试数据源。如果连接失败则抛出异常
     *
     * @return
     * @throws SQLException
     */
    public HikariDataSource createAndTestDataSource() throws SQLException {
        HikariDataSource newDataSource = createDataSource();
        try {
            testConnection(newDataSource);
        } catch (SQLException ex) {
            log.error("Testing connection for data source failed: {}", newDataSource.getJdbcUrl(), ex);
            newDataSource.close();
            throw ex;
        }

        return newDataSource;
    }

    /**
     * 测试数据源
     *
     * @param dataSource
     * @throws SQLException
     */
    private void testConnection(DataSource dataSource) throws SQLException {
        //获取连接
        Connection connection = dataSource.getConnection();
        //释放连接
        connection.close();
    }
}
