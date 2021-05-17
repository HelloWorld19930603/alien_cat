package com.aliencat.javabase.designpattern.singleton;

public enum EnumSingleton {

    SINGLETON;
    public  static EnumSingleton getDemo(){
        return SINGLETON;
    }
}
