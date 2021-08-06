package com.aliencat.javabase.thread.pool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池异常情况下的测试
 * 结论
 * 当一个线程池里面的线程异常后:
 * 1、当执行方式是execute时,可以看到堆栈异常的输出
 * 原因：ThreadPoolExecutor.runWorker()方法中，task.run()，即执行我们的方法，如果异常的话会throw x;所以可以看到异常。
 * 2、当执行方式是submit时,堆栈异常没有输出。但是调用Future.get()方法时，可以捕获到异常
 * 原因：ThreadPoolExecutor.runWorker()方法中，task.run()，其实还会继续执行FutureTask.run()方法，
 * 再在此方法中c.call()调用我们的方法，
 * 如果报错是setException()，并没有抛出异常。当我们去get()时，会将异常抛出。
 * 3、不会影响线程池里面其他线程的正常执行
 * 4、线程池会把这个线程移除掉，并创建一个新的线程放到线程池中
 * 当线程异常，会调用ThreadPoolExecutor.runWorker()方法最后面的finally中的processWorkerExit()，
 * 会将此线程remove，并重新addworker()一个线程。
 */
public class ExecutorExecutorTest {

    public static void main(String[] args) throws Exception {
        ThreadPoolTaskExecutor executor = init();
        /**
         * execute源码执行流程
         * 1、开始执行任务，新增或者获取一个线程去执行任务(比如刚开始是新增coreThread去执行任务)。
         *      执行到task.run()时会去执行提交的任务。
         *      如果任务执行失败，或throw x抛出异常。
         * 2、之后会到finally中的afterExecute()扩展方法，我们可以扩展该方法对异常做些什么。
         * 3、之后因为线程执行异常会跳出runWorker的外层循环，进入到processWorkerExit()方法，
         *      此方法会将执行任务失败的线程删除，并新增一个线程。
         * 4、之后会到ThreadGroup#uncaughtException方法，进行异常处理。
         *      如果没有通过setUncaughtExceptionHandler()方法设置默认的UncaughtExceptionHandler，
         *      就会在uncaughtException()方法中打印出异常信息。
         */
        executor.execute(() -> throwException("execute")); //execute后,可以看到堆栈异常的输出
        Thread.sleep(1000);
        /**
         * submit源码执行流程
         * 1、将传进来的任务封装成FutureTask，同样走execute的方法调用，然后直接返回FutureTask。
         * 2、开始执行任务，新增或者获取一个线程去执行任务(比如刚开始是新增coreThread去执行任务)。
         * 3、执行到task.run()时，因为是FutureTask，所以会去调用FutureTask.run()。
         * 4、在FutureTask.run()中，c.call()执行提交的任务。如果抛出异常，并不会throw x，
         *      而是setException()保存异常。
         * 5、当我们阻塞获取submit()方法结果时get()，才会将异常信息抛出。
         *      当然因为runWorker()没有抛出异常，所以并不会删除线程。
         */
        Future future = executor.submit(() -> throwException("submit")); //submit方法执行时不会输出异常，返回结果封装在future中
        future.get(); //如果调用future.get()方法则需要进行异常捕获，从而可以抛出(打印)堆栈异常。
    }

    public static void throwException(String name) {
        String printStr = "线程：" + Thread.currentThread().getName() + ",执行方式：" + name;
        System.out.println(printStr);
        throw new RuntimeException(printStr + " error!!!");
    }

    /**
     * 初始化线程池
     * 这里是使用的Spring提供的ThreadPoolTaskExecutor线程池
     *
     * @return ThreadPoolTaskExecutor
     */
    private static ThreadPoolTaskExecutor init() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("thread_");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(30);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
