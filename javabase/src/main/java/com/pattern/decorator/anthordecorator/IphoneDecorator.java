package com.pattern.decorator.anthordecorator;

/**
 * 装饰者模式的装饰类
 */
public class IphoneDecorator implements Iphone {


    private Iphone iphone;

    public IphoneDecorator(Iphone iphone) {
        this.iphone = iphone;
    }

    @Override
    public void call() {
    System.out.println("播放一首音乐野狼discoy。。。。。。。。");
    this.iphone.call();
    }
}
