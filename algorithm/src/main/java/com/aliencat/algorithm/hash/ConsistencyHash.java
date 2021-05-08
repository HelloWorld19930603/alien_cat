package com.aliencat.algorithm.hash;

import java.util.*;

public class ConsistencyHash {

    //物理节点集合  用String 类型表示
    private List<String> physicalIps = new ArrayList<>();
    //每个物理ip对应实现的虚拟节点数
    private Map<String, List<Integer>> physicalIp2Virtuals = new HashMap<>();
    //每个物理ip分配的虚拟节点数量，默认0
    private int virtualsNum;
    //虚拟节点对应的物理节点  相当于环  用TreeMap实现红黑树存储
    private SortedMap<Integer, String> sortedMap = new TreeMap<>();


    public ConsistencyHash(int virtualsNum) {
        this.virtualsNum = virtualsNum;
    }

    public ConsistencyHash() {
    }

    /**
     * 增加物理ip  到环
     */
    public void addServer(String physicalIp) {
        this.physicalIps.add(physicalIp);
        //加入物理ip对应的虚拟集合
        ArrayList<Integer> virtuals = new ArrayList<>();
        this.physicalIp2Virtuals.put(physicalIp, virtuals);

        int count = 0, i = 0;
        while (count < this.virtualsNum) {
            i++;
            int hash = getHash(physicalIp+"&&v-"+i);
            //解决hash碰撞问题
            if (!sortedMap.containsKey(hash)) {
                virtuals.add(hash);
                this.sortedMap.put(hash, physicalIp);
                count ++;
            }
           // System.out.println(count);
        }

    }

    /**
     * 获取物理ip
     */
    public String getServer(String key){
        int hash = getHash(key);
        //获取大于环上大于key hash的所有虚拟对应 物理ip
        SortedMap<Integer, String> integerStringSortedMap = this.sortedMap.tailMap(hash);

        if (!integerStringSortedMap.isEmpty()){
            return integerStringSortedMap.get(integerStringSortedMap.firstKey());
        }else { //没有数据时  取第一个虚拟节点上的  物理ip  顺时针取值
            return this.sortedMap.get(sortedMap.firstKey());
        }

    }

    /**
     * 移除物理ip
     */
    public void removeServer(String physicalIp){
        //获得此物理ip 对应所有虚拟节点
        List<Integer> integers = this.physicalIp2Virtuals.get(physicalIp);
        if (!integers.isEmpty()) {
            for (Integer integer : integers) {
                this.sortedMap.remove(integer);
            }
        }
        this.physicalIps.remove(physicalIp);
        this.physicalIp2Virtuals.remove(physicalIp);

    }

    //计算hash值
    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }


    public static void main(String[] args) {
        ConsistencyHash consistencyHash = new ConsistencyHash(100);
        consistencyHash.addServer("A");
        consistencyHash.addServer("B");
        consistencyHash.addServer("C");

        System.out.println();
        for (int i=0; i <10 ; i++){
            System.out.println("request"+i+"服务器物理ip:"+consistencyHash.getServer("request"+i));
        }


    }


}
