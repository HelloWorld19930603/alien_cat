package com.aliencat.algorithm.limiting;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class FixedWindowLimiter {

    //本地缓存，以时间戳为key，以原子类计数器为value
    private LoadingCache<Long, AtomicLong> counter =
            CacheBuilder.newBuilder()
                    .expireAfterWrite(10, TimeUnit.SECONDS)
                    .build(new CacheLoader<Long, AtomicLong>() {
                        @Override
                        public AtomicLong load(Long seconds) throws Exception {
                            return new AtomicLong(0);
                        }
                    });
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    //设置限流阈值为15
    private long limit = 15;

    /**
     * 固定时间窗口
     * 每隔5s，计算时间窗口内的请求数量，判断是否超出限流阈值
     */
    private void fixWindow() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                // time windows 5 s
                long time = System.currentTimeMillis() / 5000;
                //每秒发送随机数量的请求
                int reqs = (int) (Math.random() * 5) + 1;
                long num = counter.get(time).addAndGet(reqs);
                log.info("time=" + time + ",num=" + num);
                if (num > limit) {
                    log.info("限流了,num=" + num);
                }
            } catch (Exception e) {
                log.error("fixWindow error", e);
            } finally {
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
