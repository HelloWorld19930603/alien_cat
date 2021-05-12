package com.aliencat.javabase.proxy.bean;

import com.aliencat.javabase.proxy.interfaces.IDemo;

public class Demo implements IDemo {

    public Demo(){

    }

    public  String print(){
        System.out.println("this is a demo.");
        return "end";
    }
}
