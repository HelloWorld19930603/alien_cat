package com.aliencat.javabase.thread.join;

import lombok.SneakyThrows;

/**
 * 现有三个线程t1,t2,t3,使得t3始终在t1和t2之后执行。
 * 可使用Thread类的join方法实现。
 * join()方法的作用：当前线程等待调用了join方法的线程对象执行完毕后再执行后续逻辑。
 * Join方法实现是通过wait。
 * 当前线程调用t.join时候，当前线程会获得线程对象t的锁（wait 意味着拿到该对象的锁),
 * 调用该对象的wait(等待时间)，直到该对象唤醒main线程 ，
 * 比如退出后。这就意味着main 线程调用t.join时，必须能够拿到线程t对象的锁。
 */
public class ThreadJoinTest implements Runnable {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadJoinTest(), "线程1");
        Thread t2 = new Thread(new ThreadJoinTest(), "线程2");

        Thread t3 = new Thread(() -> {
            try {
                t1.join();
                t2.join();
                System.out.println("线程3执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        t1.start();
        Thread.sleep(100); //确保t1,t2先启动
        t3.start();

    }

    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName());
    }
}
