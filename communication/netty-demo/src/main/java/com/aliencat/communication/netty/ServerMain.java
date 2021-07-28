package com.aliencat.communication.netty;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerMain {
    public static volatile boolean running = true;

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context = new
                ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        try {
            context.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        context.stop();
                        running = false;
                        ServerMain.class.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });

            synchronized (ServerMain.class) {
                while (running) {
                    ServerMain.class.wait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }


    }

}
