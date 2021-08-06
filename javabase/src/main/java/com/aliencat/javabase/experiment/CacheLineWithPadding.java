package com.aliencat.javabase.experiment;

import java.util.concurrent.CountDownLatch;

/**
 * Cache Line
 * CPU不是按单个bytes来读取内存数据的，而是以“块数据”的形式，
 * 每块的大小通常为64bytes，这些“块”被成为“Cache Line”
 * 一个cache lien可以被多个不同的线程所使用。
 * 如果v1和v2在同一个缓存行，并且有其他线程修改了v2的值，那么线程将会强制重新加载cache line。
 * 这里假设有v1和v2共存在同一缓存行（这里前提是volatile修饰的变量）。
 * A，B线程分别修改v1，v2的值。当A线程修改v1之后，会导致整个缓存行失效，
 * 然后B线程想修改v2的值的时候就会导致无法命中缓存，然后就会从L3甚至是从主内存中去重新加载v2的值。
 * 这一会使程序运行的效率大大降低。
 * 但是通过缓存行填充，使得v1和v9不在一个缓存行(64字节)内，就可以避免缓存行失效
 * <p>
 * 注意：如果不是volatile修饰的变量，缓存行应该是不会立即失效的，也就是还会读到脏数据。
 * 有人说@Contended注解也可以实现缓存行填充，但是我试过却没有效果，不知道什么原因，先记下
 */
public class CacheLineWithPadding {

    public volatile long v1;
    /**
     * 由于jvm会减少或者重排序没有使用的字段，因此可能会重新引入“false sharing”。
     * 因此对象会在堆中的位置是没有办法保证的。
     * 为了减少未使用的填充字段被优化掉的机会，将这些字段设置成为volatile。
     * 对于填充的建议是你只需要在高度竞争的并发类上使用填充，
     * 并且在你的目标架构上测试使用有很大提升之后采用填充
     */

    public volatile long v2;   // padding
    public volatile long v3;   // padding
    public volatile long v4;   // padding
    public volatile long v5;   // padding
    public volatile long v6;   // padding
    public volatile long v7;   // padding
    public volatile long v8;   // padding
    public volatile long v9;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        final CacheLineWithPadding cache = new CacheLineWithPadding();
        Thread t1 = new Thread(() -> {
            try {
                cache.countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                cache.v1++;
            }
            System.out.println("t1耗时:" + (System.currentTimeMillis() - start));
        });

        Thread t2 = new Thread(() -> {
            try {
                cache.countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                //cache.v2++;    //耗时:3831
                cache.v9++;     //耗时:586    可见，通过使用缓存行填充可以提高cpu执行效率
            }
            System.out.println("t2耗时:" + (System.currentTimeMillis() - start));
        });


        t1.start();
        t2.start();
        cache.countDownLatch.countDown(); //让两个线程同时启动
    }
}
