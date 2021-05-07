package com.aliencat.algorithm.lb;

import com.aliencat.algorithm.lb.bean.Host;

import java.util.List;

/**
 * 1、轮询法
 * 将请求按顺序轮流地分配到后端服务器上，它均衡地对待后端的每一台服务器，而不关心服务器实际的连接数和当前的系统负载。
 */
public class PollLb {

    private static Integer pos = 0;
    public String getHostByPoll(){
        List<String> hosts = Host.getHostList();
        String host;
        synchronized (pos){
            if(pos >= hosts.size()){
                pos = 0;
            }
            host = hosts.get(pos++);
        }
        return host;
    }

    public static void main(String[] args) {
        PollLb pollLb = new PollLb();
        for (int i = 0;i < 10;i++){
            System.out.println(pollLb.getHostByPoll());
        }
    }
}
