package com.aliencat.algorithm.lb;

import com.aliencat.algorithm.lb.common.Host;

import java.util.List;
import java.util.Random;

/**
 * 5、源地址哈希法
 * 源地址哈希的思想是根据获取客户端的IP地址，通过哈希函数计算得到的一个数值，
 * 用该数值对服务器列表的大小进行取模运算，得到的结果便是客服端要访问服务器的序号。
 * 采用源地址哈希法进行负载均衡，同一IP地址的客户端，
 * 当后端服务器列表不变时，它每次都会映射到同一台后端服务器进行访问。
 */
public class IpHashLB {

    public static void main(String[] args) {
        IpHashLB ipHashLB = new IpHashLB();
        for (int i = 0; i < 10; i++) {
            System.out.println(ipHashLB.getHostByIpHash(ipHashLB.getClientHash()));
        }
    }

    public String getHostByIpHash(int ipHash) {
        List<String> hosts = Host.getHostList();
        int pos = ipHash % hosts.size();
        return hosts.get(pos);
    }

    //模拟随机客户端的hash值
    public int getClientHash() {
        return new Random().nextInt(3);
    }
}
