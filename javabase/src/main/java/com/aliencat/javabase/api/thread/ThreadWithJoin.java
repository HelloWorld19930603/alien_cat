package com.aliencat.javabase.api.thread;

public class ThreadWithJoin {

    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{ System.out.println(Thread.currentThread().getName()); },"t1");
        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },"t2");
        Thread t3 = new Thread(() -> {
            try {
                t2.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },"t3");


        t3.start();
        t2.start();
        t1.start();
    }
}
