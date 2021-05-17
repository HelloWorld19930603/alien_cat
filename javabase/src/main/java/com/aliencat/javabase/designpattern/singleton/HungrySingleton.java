package com.aliencat.javabase.designpattern.singleton;

public class HungrySingleton {

    private static HungrySingleton singleton = new HungrySingleton();

    /*
    static{
        singleton = new HungrySingleton(); //也可以通过静态代码块初始化
    }
    */
    private HungrySingleton(){}

    public static HungrySingleton getSingleton(){
        return singleton;
    }



}
