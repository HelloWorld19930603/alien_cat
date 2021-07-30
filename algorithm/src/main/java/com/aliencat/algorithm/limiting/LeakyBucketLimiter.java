package com.aliencat.algorithm.limiting;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 漏桶限流算法
 */
public class LeakyBucketLimiter {

    private int capacity;

    private int rate;

    private volatile int water = 0;

    private volatile long lastTime = 0L;

    private Lock lock = new ReentrantLock();

    public LeakyBucketLimiter(int rate) {
        this.rate = rate;
        this.capacity = rate;
    }

    public boolean tryAcquire() {
        try {
            lock.lock();
            long now = System.currentTimeMillis();
            // 匀速漏出
            int outWater = Math.round((now - lastTime) / 1000L * rate);
            if (outWater > 0) {
                lastTime = now;
            }

            water = Math.max(0, water - outWater);
            if (water < capacity) {
                water++;
                return true;
            }
        } finally {
            lock.unlock();
        }

        return false;
    }
}