package com.github.common.componets.dds;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 动态数据源：可根据apollo配置文件动态更新
 */
public class DynamicDataSource implements DataSource {
    private final AtomicReference<DataSource> datasourceRef;

    public DynamicDataSource(DataSource dataSource) {
        datasourceRef = new AtomicReference<>(dataSource);
    }

    /**
     * 设置新数据源，并返回之前的数据源
     */
    public DataSource setDataSource(DataSource newDataSource) {
        return datasourceRef.getAndSet(newDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return datasourceRef.get().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return datasourceRef.get().getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return datasourceRef.get().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return datasourceRef.get().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return datasourceRef.get().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        datasourceRef.get().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        datasourceRef.get().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return datasourceRef.get().getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return datasourceRef.get().getParentLogger();
    }
}
