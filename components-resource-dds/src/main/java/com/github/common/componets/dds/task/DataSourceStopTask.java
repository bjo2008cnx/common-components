package com.github.common.componets.dds.task;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据源停止任务
 */
@Slf4j
public class DataSourceStopTask implements Runnable {

    private static final int MAX_RETRY_TIMES = 10;
    private static final int RETRY_DELAY_IN_MILLISECONDS = 5000;

    private final DataSource dataSourceToStop;
    private final ScheduledExecutorService scheduledExecutorService;

    private volatile int retryTimes;

    public DataSourceStopTask(DataSource dataSourceToStop, ScheduledExecutorService scheduledExecutorService) {
        this.dataSourceToStop = dataSourceToStop;
        this.scheduledExecutorService = scheduledExecutorService;
        this.retryTimes = 0;
    }

    @Override
    public void run() {
        if (terminate(dataSourceToStop)) {
            log.info("Data source {} terminated successfully!", dataSourceToStop);
        } else {
            scheduledExecutorService.schedule(this, RETRY_DELAY_IN_MILLISECONDS, TimeUnit.MILLISECONDS);
        }
    }

    private boolean terminate(DataSource dataSource) {
        log.info("Trying to terminate data source: {}", dataSource);

        try {
            if (dataSource instanceof HikariDataSource) {
                return terminateHikariDataSource((HikariDataSource) dataSource);
            }

            log.error("Not supported data source: {}", dataSource);
            return true;
        } catch (Throwable ex) {
            log.warn("Terminating data source {} failed, will retry in {} ms, error message: {}", dataSource, RETRY_DELAY_IN_MILLISECONDS, ex.getMessage());
            return false;
        } finally {
            retryTimes++;
        }
    }

    /**
     * @see <a href="https://github.com/brettwooldridge/HikariCP/issues/742">支持优雅关闭Connection pool</a>
     */
    private boolean terminateHikariDataSource(HikariDataSource dataSource) {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();

        //移除空闲连接
        poolMXBean.softEvictConnections();

        if (poolMXBean.getActiveConnections() > 0 && retryTimes < MAX_RETRY_TIMES) {
            log.warn("Data source {} still has {} active connections, will retry in {} ms.", dataSource, poolMXBean.getActiveConnections(),
                    RETRY_DELAY_IN_MILLISECONDS);
            return false;
        }

        if (poolMXBean.getActiveConnections() > 0) {
            log.warn("Retry times({}) >= {}, force closing data source {}, with {} active connections!", retryTimes, MAX_RETRY_TIMES, dataSource, poolMXBean
                    .getActiveConnections());
        }

        dataSource.close();
        return true;
    }
}
