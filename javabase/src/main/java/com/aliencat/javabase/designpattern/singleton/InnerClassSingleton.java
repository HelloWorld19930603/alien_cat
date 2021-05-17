package com.aliencat.javabase.designpattern.singleton;

public class InnerClassSingleton {

    private InnerClassSingleton(){}

    private static class Singleton{
        private static final InnerClassSingleton SINGLETON = new InnerClassSingleton();
    }

    public  static InnerClassSingleton getSingleton(){
        return Singleton.SINGLETON;
    }
}
