package com.pattern.adapter;

/**
 * 用来适配各种充电方式的适配器
 */
public class SocketAdapter implements GJBZSocket {

    private Object socket;

    public SocketAdapter(Object socket) {
        this.socket = socket;
    }

    @Override
    public void charge() {
        if (socket instanceof DBSocket){
           ((DBSocket) socket).charge();;
        }else if (socket instanceof  GBSocket){
            ((GBSocket) socket).charge();
        }
    }
}
