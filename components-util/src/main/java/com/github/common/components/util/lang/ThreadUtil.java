package com.github.common.components.util.lang;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ThreadUtil {
    /**
     * 休眠
     *
     * @param millSeconds 休眠毫秒数
     */
    public static boolean sleep(long millSeconds) {
        try {
            Thread.sleep(millSeconds);
            return true;
        } catch (InterruptedException e) {
            log.warn("[WARNING]Sleeping is interrupted!");
            return false;
        }
    }

    public static void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public static void sleepMinutes(int minutes) {
        try {
            TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    /**
     * 获取线程id和名称
     *
     * @param t Thread
     * @return String
     */
    public static String getIdName(Thread t) {
        return "[" + t.getId() + ":" + t.getName() + "]";
    }

    /**
     * 挂起线程
     */
    public static void park() {
        if (log.isDebugEnabled()) {
            log.debug("current thread is going to park");
        }
        LockSupport.park();
        if (log.isDebugEnabled()) {
            log.debug("current thread is parked");
        }
    }

    /**
     * 解挂线程
     */
    public static void unpark(Thread t) {
        if (log.isDebugEnabled()) {
            log.debug("current thread is going to unpark");
        }
        LockSupport.unpark(t);
        if (log.isDebugEnabled()) {
            log.debug("current thread is unparked");
        }
    }

    /**
     * join到主线程
     *
     * @param threads Thread[]
     */
    public static void join(Thread... threads) {
        for (Thread t : threads) {
            String idName = getIdName(t);
            if (log.isDebugEnabled()) {
                log.debug("线程" + idName + "将join到主线程");
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                log.warn("线程" + idName + "join时被中断");
            }
            if (log.isDebugEnabled()) {
                log.debug("线程" + idName + "已join 到主线程");
            }
        }
    }

    /**
     * 创建一个固定大小的线程池并执行线程
     *
     * @param runnables 可执行线程
     */
    public static void execute(Runnable... runnables) {
        Executor executor = Executors.newFixedThreadPool(runnables.length);
        for (final Runnable r : runnables) {
            executor.execute(r);
        }
    }

    /**
     * 使用ExecutorService的invokeAll方法调研callable集合，批量执行多个线程
     * 在invokeAll方法结束之后，再执行主线程其他业务逻辑
     */
    public static <T> void execute(Callable<T>... callables) {
        Collection<Callable<T>> callList = Arrays.asList(callables);
        ExecutorService exec = Executors.newFixedThreadPool(2);

        try {
            exec.invokeAll(callList);
        } catch (InterruptedException e) {
            handleException(e);
        }
        exec.shutdown();
        log.info("子线程全部执行完毕，等待主线程执行任务");
        //主线程可以做些其他工作
        //do some extra work
    }

    /**
     * 处理线程异常
     *
     * @param e 异常
     */
    public static void handleException(Exception e) {
        if (e instanceof InterruptedException) {
            log.error("executor 被中断", e);
        }
    }

    /**
     * 判断任务是否已完成
     *
     * @param tasks 执行任务
     * @return boolean
     */
    public static boolean isDone(RecursiveTask[] tasks) {
        for (RecursiveTask task : tasks) {
            if (!task.isDone()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行任务
     *
     * @param tasks        执行任务
     * @param sleepSeconds 休眠毫秒数
     */
    public static void execute(RecursiveTask[] tasks, int sleepSeconds) {
        ForkJoinPool pool = new ForkJoinPool();
        for (RecursiveTask task : tasks) {
            pool.execute(task);
        }

        do {
            log.info("******************************************\n");
            log.info("Main: Parallelism: " + pool.getParallelism());
            log.info("Main: Active Threads:" + pool.getActiveThreadCount());
            log.info("Main: Task Count:" + pool.getQueuedTaskCount());
            log.info("Main: Steal Count:" + pool.getStealCount());
            log.info("******************************************\n");

            ThreadUtil.sleep(sleepSeconds * 1000);
        } while (!ThreadUtil.isDone(tasks));
        pool.shutdown();
    }

    public static Thread findThreadByName(String threadName) {
        Thread[] list = findAllThreads();
        for (Thread thread : list) {
            if (thread.getName().equals(threadName)) {
                return thread;
            }
        }
        return null;
    }

    public static List<Thread> findThreadByStartWith(String prefix) {
        Thread[] list = findAllThreads();
        List<Thread> res = new ArrayList<>();
        for (Thread thread : list) {
            if (StringUtil.startsWith(thread.getName(), prefix)) {
                res.add(thread);
            }
        }
        return res;
    }

    /**
     * @return Thread[]
     */
    public static Thread[] findAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slackList = new Thread[estimatedSize];
        // 获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slackList);
        // copy into validator list that is the exact size
        Thread[] list = new Thread[actualSize];
        System.arraycopy(slackList, 0, list, 0, actualSize);
        return list;
    }

    /**
     * 等待
     *
     * @param latch     CountDownLatch
     * @param sleepTime 休眠毫秒数
     */
    public static void await(CountDownLatch latch, long sleepTime) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            ThreadUtil.sleep(sleepTime);
        }
    }

    /**
     * 关闭线程池
     *
     * @param pools ExecutorService
     */
    public static void shutdown(ExecutorService... pools) {
        if (pools == null || pools.length == 0) {
            return;
        }
        shutdown(Arrays.asList(pools));
    }

    /**
     * 关闭线程池
     *
     * @param pools Collection<ExecutorService>
     */
    public static void shutdown(Collection<ExecutorService> pools) {
        log.info("shutdown Thread Pools.[start]");
        for (ExecutorService pool : pools) {
            if (pool == null || pool.isShutdown()) {
                continue;
            }
            try {
                pool.shutdown();
            } catch (Throwable t) {
                log.error("fail to shutdown Thread pool:" + pool.toString(), t);
            }
        }
        log.info("Thread Pools shutdown [end].");
    }

    /**
     * 设置通用的异常捕获处理器
     * 在启动类的main方法或Listener里调用该类，以捕获线程中未被捕获的异常
     * 示例：
     public static void main(String[] args) {
     try {
     running = true;
     logger.info("## set default uncaught exception handler");
     setGlobalUncaughtExceptionHandler();

     logger.info("## load canal configurations");
     String conf = System.getProperty("canal.conf", "classpath:canal.properties");
     Properties properties = new Properties();
     */
    public static void setGlobalUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("UnCaughtError", e);
            }
        });
    }
}
