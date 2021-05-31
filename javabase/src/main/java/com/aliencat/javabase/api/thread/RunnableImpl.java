package com.aliencat.javabase.api.thread;

//使用实现实现Runnable接口好，原因实现了接口还可以继续继承，继承了类不能再继承。
public class RunnableImpl implements Runnable{
    @Override
    public void run() {
        for(int i = 0;i<10;i++){
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
    }

    public static void main(String[] args) {
        //将实现Runnable接口的类传入Thread构造器创建，推荐使用此方式
        Thread thread = new Thread(new RunnableImpl());
        //当然你也可以使用匿名内部类的形式实现Runnable接口,一般不推荐这么做
        thread = new Thread(()->{
            for(int i = 0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        });
        //
        thread.start();
    }
}
