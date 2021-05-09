package com.aliencat.algorithm.sort;

import java.util.Random;

public class SortUtil {

    public static void swapArr(int[] arr,int i,int j){
        arr[i] ^= arr[j];
        arr[j] ^= arr[i];
        arr[i] ^= arr[j];
    }

    /**
     *
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
        StringBuilder sb = new StringBuilder();
        for(int a : arr){
            sb.append(a).append(" ");
        }
        System.out.println(message + sb.toString());
    }


    public static void main(String[] args) throws Exception {
        int[] arr = initArr(10,100);
        System.out.println(arr[0]+ "  " + arr[1]);
        swapArr(arr,0,1);
        System.out.println(arr[0]+ "  " + arr[1]);
    }
}
