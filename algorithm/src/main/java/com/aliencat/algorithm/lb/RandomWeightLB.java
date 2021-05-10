package com.aliencat.algorithm.lb;

import com.aliencat.algorithm.lb.common.Host;

import java.util.Map;
import java.util.Random;

/**
 * 4、加权随机法
 * 不同的后端服务器可能机器的配置和当前系统的负载并不相同，因此它们的抗压能力也不相同。
 * 给配置高、负载低的机器配置更高的权重，让其处理更多的请；而配置低、负载高的机器，给其分配较低的权重，降低其系统负载。
 * 与加权轮询法不同的是，它是按照权重随机请求后端服务器。
 */
public class RandomWeightLB {

    public String getHostByRandomWeight(){
        Map<String ,Integer> hosts = Host.getHostMap();
        int randomNum = new Random().nextInt(Host.totalWeight);
        for(Map.Entry<String,Integer> entry : hosts.entrySet()){
            if(randomNum < entry.getValue()){
                return entry.getKey();
            }else{
                randomNum -= entry.getValue();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        RandomWeightLB randomWeightLB = new RandomWeightLB();
        for(int i = 0;i < 10;i++){
            System.out.println(randomWeightLB.getHostByRandomWeight());
        }
    }
}
