package com.github.common.componets.ds.core;

import com.github.common.components.util.lang.StringUtil;
import com.github.common.componets.ds.config.ConfigLoaderContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 动态数据源
 *
 * @author arch
 */
@Slf4j
public class ConfigableDataSource extends ConfigLoaderContext implements DataSource {

    private String beanName;

    private String namespace;

    private DataSource dataSource;

    private String databaseUrl;


    /**
     * 实现具体方法
     *
     * @return
     * @throws SQLException
     */

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUrl() {
        if (StringUtil.isEmpty(databaseUrl)) {
            Class<? extends DataSource> clazz = dataSource.getClass();
            Method method = ReflectionUtils.findMethod(clazz, "getUrl");
            try {
                Object invokeJdbcMethod = ReflectionUtils.invokeJdbcMethod(method, dataSource);
                if (invokeJdbcMethod != null) {
                    databaseUrl = invokeJdbcMethod.toString();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return databaseUrl;
    }
}
