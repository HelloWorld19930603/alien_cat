package com.pattern.adapter;


public class GBSocketImpl implements GBSocket {

    @Override
    public void charge() {
        System.out.println("使用220V电压充电。。。。。。。。。");
    }
}
