package com.aliencat.algorithm.lb;

import java.util.Map;

/**
 * 3、加权轮询法
 *
 * 不同的后端服务器可能机器的配置和当前系统的负载并不相同，因此它们的抗压能力也不相同。
 * 给配置高、负载低的机器配置更高的权重，让其处理更多的请；而配置低、负载高的机器，给其分配较低的权重，降低其系统负载，
 * 加权轮询能很好地处理这一问题，并将请求顺序且按照权重分配到后端。
 */
public class PollWeightLB {

    private static Integer pos = 0;

    public String getHostByPollWeight(){
        Map<String,Integer> hosts = Host.getHostMap2();
        String host = null;
        synchronized (pos){
            for(Map.Entry<String,Integer> entry : hosts.entrySet()){
                if(pos >= Host.totalWeight){
                    pos = 0;
                }
                if(pos < entry.getValue()){
                    host = entry.getKey();
                    pos++;
                    break;
                }
            }
        }
        return host;
    }


}
