package com.aliencat.algorithm.limiting;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 滑动窗口限流算法
 * 滑动窗口将固定窗口再等分为多个小的窗口，每一次对一个小的窗口进行流量控制。
 */
@Slf4j
public class SlidingWindowRateLimiter implements RateLimiter, Runnable {
    private static final int DEFAULT_BLOCK = 10;
    private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 5;
    private final long maxVisitPerSecond;
    private final int block;
    private final AtomicLong[] countPerBlock;

    private AtomicLong count;
    private volatile int index;

    public SlidingWindowRateLimiter(int block, long maxVisitPerSecond) {
        this.block = block;
        this.maxVisitPerSecond = maxVisitPerSecond;
        countPerBlock = new AtomicLong[block];
        for (int i = 0; i < block; i++) {
            countPerBlock[i] = new AtomicLong();
        }
        count = new AtomicLong(0);
    }

    public SlidingWindowRateLimiter() {
        this(DEFAULT_BLOCK, DEFAULT_ALLOWED_VISIT_PER_SECOND);
    }

    public static void main(String[] args) {
        SlidingWindowRateLimiter slidingWindowRateLimiter = new SlidingWindowRateLimiter(10, 1000);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(slidingWindowRateLimiter, 100, 100, TimeUnit.MILLISECONDS);

        new Thread(() -> {
            while (true) {
                slidingWindowRateLimiter.visit();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                slidingWindowRateLimiter.visit();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean isOverLimit() {
        return currentQPS() > maxVisitPerSecond;
    }

    @Override
    public long currentQPS() {
        return count.get();
    }

    @Override
    public boolean visit() {
        countPerBlock[index].incrementAndGet();
        count.incrementAndGet();
        return isOverLimit();
    }

    @Override
    public void run() {
        log.info("是否超出限制:" + isOverLimit());
        log.info("当前QPS:" + currentQPS());
        log.info("index:" + index);
        index = (index + 1) % block;
        long val = countPerBlock[index].getAndSet(0);
        count.addAndGet(-val);
    }
}
