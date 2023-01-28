package com.greencat.antimony.utils.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("AntimonyThreads-%d").build());
    public static Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }
}
