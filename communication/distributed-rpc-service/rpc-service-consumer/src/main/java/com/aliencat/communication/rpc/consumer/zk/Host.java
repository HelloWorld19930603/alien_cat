package com.aliencat.communication.rpc.consumer.zk;

public class Host {
    private String ip;
    private int port;
    private long responseTime;
    private long lastTime;

    public Host getHost(String host) {
        String[] split = host.split(":");
        if (split.length == 2) {
            this.ip = split[0];
            this.port = Integer.parseInt(split[1]);
        }
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
