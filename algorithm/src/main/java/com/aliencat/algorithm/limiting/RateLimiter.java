package com.aliencat.algorithm.limiting;

/**
 * 限流接口
 */
public interface RateLimiter {

    boolean isOverLimit();

    long currentQPS();

    boolean visit();
}
