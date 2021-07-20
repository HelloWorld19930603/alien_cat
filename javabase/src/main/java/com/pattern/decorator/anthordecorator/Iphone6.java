package com.pattern.decorator.anthordecorator;

/**
 * 装饰者模式的被装饰类
 */
public class Iphone6 implements Iphone {
    @Override
    public void call() {
    System.out.println("我在拨打电话。。。");
    }
}
