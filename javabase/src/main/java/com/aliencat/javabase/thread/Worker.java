package com.aliencat.javabase.thread;

import java.util.concurrent.Semaphore;

public class Worker extends Thread {
    private int num;
    private Semaphore semaphore;

    public Worker(int num, Semaphore semaphore) {
        this.num = num;
        this.semaphore = semaphore;
    }

    public static void main(String[] args) {
        //工人数
        int N = 8;
        //机器数目 许可=5
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < N; i++)
            new Worker(i, semaphore).start();
    }

    @Override
    public void run() {
        try {
            //获取permits个许可，若无许可能够获得，则会一直等待，直到获得许可。
            semaphore.acquire();
            System.out.println("工人" + this.num + "占用一个机器在生产...");
            Thread.sleep(2000);
            System.out.println("工人" + this.num + "释放出机器");
            //释放许可。注意，在释放许可之前，必须先获获得许可。
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


