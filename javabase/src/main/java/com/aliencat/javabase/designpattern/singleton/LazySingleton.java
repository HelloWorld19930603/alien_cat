package com.aliencat.javabase.designpattern.singleton;

public class LazySingleton {

    private volatile static LazySingleton singleton = null;
    //private static LazySingleton singleton = null; //不加volatile会产生线程安全的bug

    private LazySingleton(){}

    public static LazySingleton getSingleton(){
        if(singleton == null){
            singleton = new LazySingleton();
        }
        return singleton;
    }

    public synchronized static LazySingleton getSingleton2(){
        if(singleton == null){
            singleton = new LazySingleton();
        }
        return singleton;
    }

    public  static LazySingleton getSingleton3(){
        if(singleton == null){
            synchronized(LazySingleton.class) {
                singleton = new LazySingleton();
            }
        }
        return singleton;
    }

    //双检锁模式
    public  static LazySingleton getSingleton4(){
        if(singleton == null){
            synchronized(LazySingleton.class) {
                if(singleton == null) {
                    singleton = new LazySingleton();
                }
            }
        }
        return singleton;
    }
}
