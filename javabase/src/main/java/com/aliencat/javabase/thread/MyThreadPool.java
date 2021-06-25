package com.aliencat.javabase.thread;

import java.util.concurrent.*;

public class MyThreadPool {


    /**
     *分三步进行：
     *
     * 1. 如果运行的线程少于corePoolSize，请尝试
     *以给定的命令作为第一个线程开始一个新线程
     *任务。对addWorker的调用以原子方式检查运行状态和
     *workerCount，从而防止会增加
     *当它不应该的时候，返回false。
     *
     * 2. 如果任务可以成功排队，那么我们仍然需要
     *仔细检查是否应该添加线程
     *（因为自从上次检查以来已有的已经死了）或者
     *自进入此方法后，池已关闭。所以我们
     *重新检查状态，必要时回滚排队
     *已停止，如果没有，则启动新线程。
     *
     * 3. 如果无法将任务排队，则尝试添加新的线程。
     * 如果失败了，我们就知道我们已经关闭或者饱和了
     *所以拒绝这个任务。
     */
    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(// 自定义一个线程池
                1, // 核心线程数
                2, // 最大线程数
                60, // 超过核心线程数的额外线程存活时间
                TimeUnit.SECONDS, // 线程存活时间的时间单位
                new ArrayBlockingQueue<>(3) // 有界队列，容量是3个
                , Executors.defaultThreadFactory()    // 线程工厂
                , new ThreadPoolExecutor.AbortPolicy() //线程的拒绝策略
        );
        //执行一个任务
        threadPool.execute(()->{
            //线程执行的具体逻辑
            //Runnable to do something.
            System.out.println("hello world");
        });
        threadPool.shutdown();
        threadPool.shutdownNow();
    }
}
