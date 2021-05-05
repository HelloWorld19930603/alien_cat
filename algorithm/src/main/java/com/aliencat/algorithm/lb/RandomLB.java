package com.aliencat.algorithm.lb;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
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
