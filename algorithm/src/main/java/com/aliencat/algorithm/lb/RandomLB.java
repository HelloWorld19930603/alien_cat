package com.aliencat.algorithm.lb;

import java.util.List;
import java.util.Random;

/**
 * 2、随机法
 * 通过系统的随机算法，根据后端服务器的列表大小值来随机选取其中的一台服务器进行访问。
 * 由概率统计理论可以得知，随着客户端调用服务端的次数增多，每台主机处理请求数越接近平均值
 */
public class RandomLB {

    public String getHostByRandom(){
        List<String> hosts = Host.getHostList();
        int randomNum = new Random().nextInt(hosts.size());
        System.out.println(randomNum);
        return hosts.get(randomNum);
    }

    public static void main(String[] args) {
        RandomLB randomLB = new RandomLB();
        for(int i=0;i<10;i++){
            System.out.println(randomLB.getHostByRandom());
        }
    }
}
