package com.aliencat.algorithm.lb;

import java.util.Map;
import java.util.Random;

public class RandomWeightLB {

    private int totalWeight;

    RandomWeightLB(){
        Map<String ,Integer> hosts = Host.getHostMap();
        for(int v : hosts.values()){
            totalWeight += v;
        }
    }

    public String getHostByRandomWeight(){
        Map<String ,Integer> hosts = Host.getHostMap();
        int randomNum = new Random().nextInt(totalWeight);
        for(Map.Entry<String,Integer> entry : hosts.entrySet()){
            if(randomNum < entry.getValue()){
                return entry.getKey();
            }else{
                randomNum -= entry.getValue();
            }
        }
        return null;
    }

    public int getTotalWeight(){
        return totalWeight;
    }

    public static void main(String[] args) {
        RandomWeightLB randomWeightLB = new RandomWeightLB();
        for(int i = 0;i < 10;i++){
            System.out.println(randomWeightLB.getHostByRandomWeight());
        }
    }
}
