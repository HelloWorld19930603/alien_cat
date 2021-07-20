package com.spring.code;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class assertTest {
		public static void main(String[] args) {
			 //创建一个可缓存线程池
			ExecutorService catchThreadPool=Executors.newCachedThreadPool();
			for(int i=0;i<10;i++){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new Thread(()->{
					System.out.println(Thread.currentThread().getName());
				}).start();
				catchThreadPool.execute(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println(Thread.currentThread().getName());
					}
				});
			}
			
		}
}
