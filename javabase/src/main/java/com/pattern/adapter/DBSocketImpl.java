package com.pattern.adapter;

public class DBSocketImpl implements DBSocket {
    @Override
    public void charge() {
        System.out.println("使用110V电压充电。。。。。。");
    }
}
