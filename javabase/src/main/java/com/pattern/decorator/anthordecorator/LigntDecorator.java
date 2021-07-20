package com.pattern.decorator.anthordecorator;

/**
 * 让手机屏幕亮起装饰类
 */
public class LigntDecorator implements  Iphone {

    private Iphone iphone;

    public LigntDecorator(Iphone iphone) {
        this.iphone=iphone;
    }

    @Override
    public void call() {
    System.out.println("手机屏幕亮起。。。。。。。。。。");
    iphone.call();
    }
}
