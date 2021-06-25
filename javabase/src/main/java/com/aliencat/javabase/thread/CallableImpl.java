package com.aliencat.javabase.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableImpl implements Callable {

    // 与run()方法不同的是，call()方法具有返回值
    @Override
    public Integer call() throws InterruptedException {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            sum += i;
        }
        Thread.sleep(3000);   //计算完成sum后，我们让线程休眠3秒
        return sum;
    }

    public static void main(String[] args) {
        Callable<Integer> callableImpl = new CallableImpl();    // 创建Callable对象
        //事实上FutureTask也实现了Runnable接口
        FutureTask<Integer> ft = new FutureTask<>(callableImpl); //使用FutureTask来包装Callable对象

        Thread thread = new Thread(ft);       //FutureTask对象作为Thread对象的target创建新的线程
        thread.start();                       //线程进入到就绪状态

        System.out.println("主线程for循环执行完毕..");

        try {
            int sum = ft.get();            //取得新创建的thread线程中的call()方法返回的结果,这里也会停顿3s，说明该方法是阻塞的
            System.out.println("sum = " + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
