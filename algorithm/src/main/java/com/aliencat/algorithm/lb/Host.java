package com.aliencat.algorithm.lb;

import java.util.ArrayList;
import java.util.List;

public class Host {

    private static List<String> hostList;

    static {
        //初始化主机ip地址
        initHostList();
    }

    public static void initHostList(){
        hostList = new ArrayList<>();
        hostList.add("192.168.0.1");
        hostList.add("192.168.0.2");
        hostList.add("192.168.0.3");
    }

    public static List<String> getHostList(){
        return hostList;
    }
}
