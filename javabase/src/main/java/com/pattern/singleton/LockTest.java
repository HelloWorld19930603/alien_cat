package com.pattern.singleton;

public class LockTest {
    /***
     * 此处使用volatile是为了防止使用指令重排序
     */
    public static volatile  LockTest lockTest;

    private LockTest(){

    }

    /**
     * 使用双重检测锁防止多线程同时竞争
     * @return
     */
    public static LockTest getInstance(){
        if (lockTest==null){
            synchronized (LockTest.class){
                if (lockTest==null){
                    return new LockTest();
                }
            }
        }
       return lockTest;
    }

}
