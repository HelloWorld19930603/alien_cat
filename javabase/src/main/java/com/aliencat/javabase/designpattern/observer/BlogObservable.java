package com.aliencat.javabase.designpattern.observer;

import lombok.Getter;

import java.util.Observable;

/**
 * 被关注的博客：发布新文章
 * <p>
 * 观察者模式（Observer Pattern）：
 * 定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
 * 当对象间存在一对多关系时，则使用观察者模式。
 * 比如，当一个对象被修改时，则会自动通知依赖它的对象。
 * 观察者模式属于行为型模式。
 */
@Getter
public class BlogObservable extends Observable {

    private String article;

    /**
     * 发表文章
     *
     * @param article
     */
    public void publish(String article) {

        this.article = article;
        // 更新观察者状态
        this.setChanged();
        // 通知所有观察者
        this.notifyObservers();
    }

}
