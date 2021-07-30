package com.aliencat.javabase.designpattern.observer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Observable;
import java.util.Observer;


/**
 * 观察者：读者粉丝
 */
@RequiredArgsConstructor
public class ReaderObserver implements Observer {

    @NonNull
    private String title;

    @Override
    public void update(Observable o, Object arg) {
        // 更新文章
        BlogObservable blogObservable = (BlogObservable) o;
        String article = blogObservable.getArticle();
        System.out.printf("读者%s接收到了新文章：%s\n", this.title, article);
    }

}
