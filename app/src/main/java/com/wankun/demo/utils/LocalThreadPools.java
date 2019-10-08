/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.widget.Toast;

import com.wankun.demo.application.DemoApplication;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈自定义的全局线程池 , 避免使用 new Thread创建线程〉
 * <p>
 * ExecutorService是Java提供的线程池，
 * 也就是说，每次我们需要使用线程的时候，可以通过ExecutorService获得线程。
 * 它可以有效控制最大并发线程数，提高系统资源的使用率，
 * 同时避免过多资源竞争，避免堵塞，同时提供定时执行、定期执行、单线程、并发数控制等功能，也不用使用TimerTask了
 *
 * @author wankun
 * @create 2019/5/8
 * @since 1.0.0
 */
public class LocalThreadPools {
    private static ExecutorService THREAD_POOL_EXECUTOR;

    /**
     * corePoolSize：核心线程数
     */
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    /**
     * keepAliveTime:非核心线程闲置超时时间
     */
    private static final int KEEP_ALIVE_TIME = 1;
    /**
     * 非核心线程闲置超时时间单位
     */
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    /**
     * 任务队列，存储暂时无法执行的任务，等待空闲线程来执行任务
     */
    private static final BlockingQueue<Runnable> poolWorkQueue = new LinkedBlockingQueue<Runnable>();
    /**
     * 当线程边界和队列容量已经达到最大时，用于处理阻塞时的程序:
     * 线程池拒绝策略:直接丢弃（DiscardPolicy）
     * 丢弃队列中最老的任务(DiscardOldestPolicy)
     * 抛异常(AbortPolicy)
     * 将任务分给调用线程来执行(CallerRunsPolicy);
     */
    private static final ThreadPoolExecutor.AbortPolicy handler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 线程工程，用于创建线程
     */
    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "LocalThreadPools #" + mCount.getAndIncrement());
        }
    };

    /**
     * 当线程边界和队列容量已经达到最大时，用于处理阻塞时的程序:
     */
    private class RejectedHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //可在这里做一些提示用户的操作
            Toast.makeText(DemoApplication.getAppContext(), "当前执行的任务过多，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 不对外暴露构建方法
     */
    private LocalThreadPools() {
        initThreadPool();
    }

    /**
     * 用静态内部类实现单例
     */
    private static class LocalThreadPoolsInstance {
        private static final LocalThreadPools instance = new LocalThreadPools();
    }

    /**
     * 对外暴露对象实例的方法
     *
     * @return
     */
    public static LocalThreadPools getInstance() {
        return LocalThreadPoolsInstance.instance;
    }


    /**
     * 初始化方法
     */
    private void initThreadPool() {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                poolWorkQueue,
                threadFactory,
                new RejectedHandler()
        );

        //允许核心线程空闲超时时被回收
        //threadPoolExecutor.allowCoreThreadTimeOut(true);
    }


    /**
     * 使用线程
     *
     * @param command
     */
    public void execute(Runnable command) {
        THREAD_POOL_EXECUTOR.execute(command);
    }

    /**
     * 通过interrupt方法尝试停止正在执行的任务，但是不保证真的终止正在执行的任务
     * 停止队列中处于等待的任务的执行
     * 不再接收新的任务
     *
     * @return 等待执行的任务列表
     */
    public List<Runnable> shutdownNow() {
        return THREAD_POOL_EXECUTOR.shutdownNow();
    }

    /**
     * 停止队列中处于等待的任务
     * 不再接收新的任务
     * 已经执行的任务会继续执行
     * 如果任务已经执行完了没有必要再调用这个方法
     */
    public void shutDown() {
        THREAD_POOL_EXECUTOR.shutdown();
        poolWorkQueue.clear();
    }


}
