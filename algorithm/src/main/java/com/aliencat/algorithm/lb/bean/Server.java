package com.aliencat.algorithm.lb.bean;

public class Server {

    private String ip;
    private int connect;

    public Server(String ip){
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getConnect() {
        return connect;
    }

    public void addConnect() {
        connect++;
    }

    //模拟断开连接
    public void disconnect(){
        if(connect > 0){
            connect--;
        }
    }
}
