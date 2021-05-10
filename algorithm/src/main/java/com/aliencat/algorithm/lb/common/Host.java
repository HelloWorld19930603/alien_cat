package com.aliencat.algorithm.lb.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host {


    private static List<String> hostList;
    //key:主机地址  value:权重
    private static Map<String,Integer> hostMap;
    private static Map<String,Integer> hostMap2;
    //记录了连接数的Server集合
    private static List<Server> serverList;
    //记录总权重
    public static int totalWeight;

    static {
        //初始化主机ip地址
        initHostList();
        initHostMap();
        initHostMap2();
        initServerList();
    }

    public static void initHostList(){
        hostList = new ArrayList<>();
        hostList.add("192.168.0.1");
        hostList.add("192.168.0.2");
        hostList.add("192.168.0.3");
    }

    public static void initHostMap(){
        hostMap = new HashMap<>();
        //这里为了方便用A、B、C代表三个主机ip
        hostMap.put("A",5);
        hostMap.put("B",2);
        hostMap.put("C",3);

        Map<String ,Integer> hosts = Host.getHostMap();
        for(int v : hosts.values()){
            totalWeight += v;
        }
    }

    public static void initHostMap2(){
        hostMap2 = new HashMap<>();
        int weight = 0;
        //A对应0~4,B对应5~6,C对应7~9
        for(Map.Entry<String,Integer> entry : hostMap.entrySet()){
            weight += entry.getValue();
            hostMap2.put(entry.getKey(),weight);
        }

    }

    public static void initServerList(){
        serverList = new ArrayList<>();
        //初始化Server连接数都是0
        serverList.add(new Server("A"));
        serverList.add(new Server("B"));
        serverList.add(new Server("C"));
    }

    public static List<String> getHostList(){
        return hostList;
    }

    public static Map<String,Integer> getHostMap(){
        return hostMap;
    }

    public static Map<String,Integer> getHostMap2(){
        return hostMap2;
    }

    public static List<Server> getServerList(){
        return serverList;
    }
}
