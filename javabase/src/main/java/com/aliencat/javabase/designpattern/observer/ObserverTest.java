package com.aliencat.javabase.designpattern.observer;

public class ObserverTest {

    public static void main(String[] args) {
        // 创建一个观察目标
        BlogObservable blogObservable = new BlogObservable();

        // 添加观察者
        blogObservable.addObserver(new ReaderObserver("张三"));
        blogObservable.addObserver(new ReaderObserver("李四"));
        blogObservable.addObserver(new ReaderObserver("赵五"));

        // 发表文章
        blogObservable.publish("观察者模式");
        System.out.println();
        blogObservable.publish("又一篇新文章");
    }
}
