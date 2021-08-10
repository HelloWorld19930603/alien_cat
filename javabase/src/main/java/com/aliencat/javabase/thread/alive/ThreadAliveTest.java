package com.aliencat.javabase.thread.alive;

/**
 * isAlive()---判断线程的活动状态
 * 问题：isAlive是判断当前线程还是调用该方法的对象对应的线程存活呢？
 */
public class ThreadAliveTest {


    public static void main(String[] args) {
        Thread t1 = new Thread() {
            public void run() {
                System.out.println("t1 : " + isAlive());
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                try {
                    t1.join();  //等t1执行结束
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(t1.isAlive()); //false，显然它判断的是t1线程的存活状态
                System.out.println(isAlive());    //true
            }
        };

        t1.start();
        t2.start();
    }
}
