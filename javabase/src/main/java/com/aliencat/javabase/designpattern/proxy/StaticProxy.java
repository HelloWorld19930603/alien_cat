package com.aliencat.javabase.designpattern.proxy;

import com.aliencat.javabase.designpattern.proxy.interfaces.IDemo;
import com.aliencat.javabase.designpattern.proxy.bean.Demo;

public class StaticProxy implements IDemo {

    IDemo target;

    public StaticProxy(IDemo target) {
        this.target = target;
    }

    public static void main(String[] args) {
        IDemo demo = new StaticProxy(new Demo());
        System.out.println(demo.print());
    }

    @Override
    public String print() {
        System.out.println("before print");
        String result = target.print();
        System.out.println("after print");

        return "print " + result;
    }
}
