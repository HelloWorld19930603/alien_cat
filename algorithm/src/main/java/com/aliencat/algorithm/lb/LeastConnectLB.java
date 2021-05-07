package com.aliencat.algorithm.lb;

import com.aliencat.algorithm.lb.bean.Host;
import com.aliencat.algorithm.lb.bean.Server;

import java.util.List;
import java.util.Random;

/**
 * 6、最小连接数法
 * 由于后端服务器的配置不尽相同，对于请求的处理有快有慢，
 * 它正是根据后端服务器当前的连接情况，动态地选取其中当前积压连接数最小的一台服务器来处理当前请求，
 * 尽可能地提高后端服务器的利用效率，将负载合理地分流到每一台机器。
 */
public class LeastConnectLB {


    public Server getServerByLeastConnect(){
        List<Server> serverList = Host.getServerList();
        if(serverList == null || serverList.size() == 0){
            return null;
        }
        Server minConnectServer = serverList.get(0);
        for(int i = 1; i < serverList.size();i++){
            Server server = serverList.get(i);
            if(minConnectServer.getConnect() > server.getConnect()){
                minConnectServer = server;
            }
        }
        minConnectServer.addConnect();
        return minConnectServer;
    }

    public static void main(String[] args) {
        LeastConnectLB leastConnectLB = new LeastConnectLB();
        for (int i = 0;i < 10;i++){
            //模拟随机主机断开连接的情况,概率为30%
            int pos = new Random().nextInt(10);
            if(pos < 3) {
                Host.getServerList().get(pos).disconnect();
            }
            Server server = leastConnectLB.getServerByLeastConnect();
            System.out.println("ip: "+server.getIp()+" , connection: "+server.getConnect());
        }
    }
}
