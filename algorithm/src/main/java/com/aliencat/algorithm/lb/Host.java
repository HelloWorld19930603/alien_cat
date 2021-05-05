package com.aliencat.algorithm.lb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host {

    private static List<String> hostList;
    private static Map<String,Integer> hostMap;

    static {
        //初始化主机ip地址
        initHostList();
        initHostMap();
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
    }

    public static List<String> getHostList(){
        return hostList;
    }

    public static Map<String,Integer> getHostMap(){
        return hostMap;
    }
}
