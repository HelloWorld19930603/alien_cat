package com.aliencat.algorithm.sort;



//网传该屌炸天的休眠排序算法是由一个9K的java程序员写出来的，当时老板让他写一个排序算法，接着就被开除了。
public class SleepSort implements Runnable{

    int num ;

    SleepSort(int num){
        this.num = num;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(num);
            System.out.println(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int[] arr = new int[]{102,38,62,91,58,66};
        for(int a : arr){
            new Thread(new SleepSort(a)).start();
        }
    }
}
