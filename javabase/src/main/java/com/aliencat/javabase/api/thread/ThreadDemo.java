package com.aliencat.javabase.api.thread;

public class ThreadDemo {

    //创建线程的三种方式
    public static void main(String[] args) {
        Thread thread = new Thread();
        //第一种：将实现Runnable接口的类传入Thread构造器创建，推荐使用此方式
        Thread threadA = new Thread(new RunnableImpl());
        //第二种：继承Thread类，重写run()方法
        Thread threadB = new ThreadExt();
        //第三种：匿名内部类
        Thread threadC = new Thread(()->{
            for(int i = 0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        });
        //以上三个线程皆处于新建状态

        //使用start方法分别启动这三个线程后就会进入就绪状态，
        threadA.start();
        threadB.start();
        threadC.start();
        //抢到cpu资源的就会进入运行状态，否则进入阻塞状态
        //run方法执行完毕则线程进入销毁状态，线程生命周期完结
    }
}



