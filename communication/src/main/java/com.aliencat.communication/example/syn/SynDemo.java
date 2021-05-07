package org.example.syn;

public class SynDemo {

    private static Boolean init = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!init){
                    synchronized (init){
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("tread 1 进入");
                        if(!init){
                            init = true;
                            System.out.println("thread 1 " + init);
                        }
                    }
                }

            }
        }).start();
        Thread.sleep(100);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!init){
                    synchronized (init){
                        System.out.println("tread 2 进入");
                        if(!init){
                            init = true;
                            System.out.println("thread 2 " + init);
                        }
                    }
                }

            }
        }).start();
    }




}
