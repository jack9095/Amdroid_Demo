package com.kuanquan.annotationandeventbusdemo.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    public ExecutorService executorService;
    private int CPU_COUNT; // CPU 核心数量
    private int CORE_COUNT; // 核心线程数量

    // 通常核心线程数可以设为 CPU 数量+1，而最大线程数可以设为 CPU 的数量*2+1。
    public ThreadPoolManager() {
        CPU_COUNT = Runtime.getRuntime().availableProcessors();
        CORE_COUNT = Math.max(2,CPU_COUNT);
        executorService = new ThreadPoolExecutor(
                3,
                3,
                5, TimeUnit.MINUTES,
                new PriorityBlockingQueue<Runnable>());
    }
}
