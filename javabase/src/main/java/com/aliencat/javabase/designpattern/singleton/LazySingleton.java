package com.aliencat.javabase.designpattern.singleton;

public class LazySingleton {

    private static LazySingleton singleton = null;
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
