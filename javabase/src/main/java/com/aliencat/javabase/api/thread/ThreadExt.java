package com.aliencat.javabase.api.thread;

public class ThreadExt extends Thread{

    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
    }

    public static void main(String[] args) {
        Thread thread = new ThreadExt();
        thread.start();
    }
}