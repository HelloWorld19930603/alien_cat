package com.aliencat.springboot.nebula.ocean.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName RtsThread
 **/
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RtsThread {
    ThreadPoolExecutor threadPoolExecutor = null;
    //最大可用的CPU核数
    public static int PROCESSORS = Runtime.getRuntime().availableProcessors();

    // 线程队列
    public static int arrayBlockingQueue = 20000;

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    @PreDestroy
    public void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
        }
    }

    public void initExecutor() {
        if (threadPoolExecutor != null) {
            return;
        }
        synchronized (RtsThread.class) {
            if (threadPoolExecutor != null) {
                return;
            }
            if (PROCESSORS < 40) {
                PROCESSORS = 40;
            }
            System.out.println("RtsThread PROCESSORS" + PROCESSORS);
            threadPoolExecutor = new ThreadPoolExecutor(
                    PROCESSORS * 2 - 10, PROCESSORS * 4,
                    60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(arrayBlockingQueue),
                    new ThreadPoolUtil("RTS"), new CustomRejectedExecutionHandler());
        }
    }

    @PostConstruct
    private void init() {
        this.initExecutor();
    }


    private static class ThreadPoolUtil implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        ThreadPoolUtil(String namePrefix) {
            this.namePrefix = namePrefix + "-";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon()) {
                t.setDaemon(true);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            throw new RejectedExecutionException();
        }
    }
}




