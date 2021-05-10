package com.aliencat.algorithm.sort;

import java.util.Random;

public class SortUtil {

    //交换数组中两个下标为i,j的数
    public static void swapArr(int[] arr,int i,int j){
        arr[i] ^= arr[j];
        arr[j] ^= arr[i];
        arr[i] ^= arr[j];
    }

    /**
     * 初始化一个随机数组
     * @param size 数组大小
     * @param range 生成随机数范围
     * @return
     * @throws Exception
     */
    public static int[] initArr(int size,int range) throws Exception {
        if(size < 0 || range < 0){
            throw new Exception("输入非法参数");
        }
        int[] arr = new int[size];
        Random random = new Random();
        for(int i = 0;i<size;i++){
            arr[i] = random.nextInt(range);
        }
        return arr;
    }

    public static void printArr(int[] arr,String message){
        StringBuilder sb = new StringBuilder(message);
        for(int a : arr){
            sb.append(a).append(" ");
        }
        sb.append("\n"+message+(checkAsc(arr) ? "有序" : "无序"));
        System.out.println(sb.toString());
    }

    //检查数组是否升序排列
    public static boolean checkAsc(int[] arr){
        for (int i = 1;i<arr.length-1;i++){
            if(arr[i - 1] > arr[i]){
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        int[] arr = initArr(10,100);
        System.out.println(arr[0]+ "  " + arr[1]);
        swapArr(arr,0,1);
        System.out.println(arr[0]+ "  " + arr[1]);
    }
}
