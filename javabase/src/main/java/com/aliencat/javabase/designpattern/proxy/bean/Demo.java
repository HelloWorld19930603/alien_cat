package com.aliencat.javabase.designpattern.proxy.bean;

import com.aliencat.javabase.designpattern.proxy.interfaces.IDemo;

public class Demo implements IDemo {

    public Demo(){

    }

    public  String print(){
        System.out.println("this is a demo.");
        return "end";
    }
}
