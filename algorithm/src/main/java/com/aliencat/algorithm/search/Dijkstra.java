package com.aliencat.algorithm.search;

/**
 * Dijkstra(迪特斯克拉算法)算法：
 * 1.Dijkstra处理的是带正权值的有权图，那么，就需要一个二维数组(如果空间大用list数组)存储各个点到达(边)的权值大小。(邻接矩阵或者邻接表存储)
 * 2.还需要一个boolean数组判断那些点已经确定最短长度，那些点没有确定。int数组记录距离(在算法执行过程可能被多次更新)。
 * 3.需要优先队列加入已经确定点的周围点。每次抛出确定最短路径的那个并且确定最短，直到所有点路径确定最短为止。
 */
public class Dijkstra {

    public static void main(String[] args) {
        //这里我们将无穷大定义为int类型最大值 2^32-1  2147483647
        Integer infinity = Integer.MAX_VALUE;
        //构建图，用邻接矩阵作为图的数据结构，该地图是边的数组
        Integer[][] paraGraph = new Integer[][]{
                //从a1定点出发，到各定点的开销 从a1开始
                {0, infinity, 30, 60, infinity, 300},
                //从a2顶点出发,到各个顶点的开销 从a1开始
                {infinity, 0, 3, infinity, infinity, infinity},
                //从a3顶点出发,到各个顶点的开销 从a1开始
                {infinity, infinity, 0, 25, infinity, infinity},
                //从a4顶点出发,到各个顶点的开销 从a1开始
                {infinity, infinity, infinity, 0, infinity, 70},
                //从a5顶点出发,到各个顶点的开销 从a1开始
                {infinity, infinity, infinity, 15, 0, 10},
                //从a6顶点出发,到各个顶点的开销 从a1开始
                {infinity, infinity, infinity, infinity, infinity, 0}
        };
//      distance代表从第一个点到其他点的开销之和。例如 distance[2] 代表从第一个点，也就是 temp[0] 到 temp[2] 的开销的和（也是最小开销），其中，从第一个点到最后一个点，不经过的点的开销为无穷大
        Integer[] distance = new Integer[6];
        for (Integer i = 0; i < 6; i++)
            distance[i] = paraGraph[0][i];

        Boolean[] temp = new Boolean[6];//temp集合

        minDistance(paraGraph, distance, temp);

    }

    public static void minDistance(Integer[][] paraGraph, Integer[] distance, Boolean[] temp) {
        temp[0] = true; //如果对应位置的定点计算过，则temp[i]设定为true

        while (true) {//外层循环
            Integer min = Integer.MAX_VALUE; //当前顶点最小的开销
            Integer index = -1; //当前顶点所在的位置

            //内层循环:比对每个顶点和其他顶点之间的距离，获取最小距离
            for (Integer i = 0; i < distance.length; i++) {
                //比对定点是否已经计算过，开始的顶点忽略，从第二个顶点开始
                if (temp[i] == Boolean.TRUE)
                    continue;//不包括集合temp中的顶点 已经包括了就继续循环,不在加入范围
                else {
                    System.out.println(i);
                    //获取到下一个顶点的最短距离，以及下一个顶点的index
                    if (distance[i] < min) {
                        index = i;
                        min = distance[i];
                    }
                }
            }

            System.out.println("index " + index);
            Dijkstra.printTostring(distance);

            if (index == -1) break;//index移动到最后的顶点，跳出循环

            // 标记此顶点已经当过 “当前顶点”这个角色
            temp[index] = Boolean.TRUE;
            for (Integer i = 0; i < paraGraph.length; i++) {
                //如果当前顶点和另外一个顶点有链接
                if (paraGraph[index][i] != Integer.MAX_VALUE) {
                    //比对第一个顶点到其他顶点的开销，是否小于（第一个顶点到当前顶点的最小开销+当前顶点到下一个顶点的开销）
                    if (distance[i] < min + paraGraph[index][i]) {
                        distance[i] = distance[i]; // 如果小，则直接使用第一个点到下一个点的开销
                    } else {
                        distance[i] = min + paraGraph[index][i]; // 否则，使用第一个点到当前点，然后从当前点再到下一个点
                    }
                }
            }
        }
    }

    //    用于打印数组的方法
    public static <T> void printTostring(T[] parent) {
        for (T son : parent) {
            System.out.print(son + " ");
        }
        System.out.println("");
    }

}


