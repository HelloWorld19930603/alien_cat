package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.interfaces.Sort;

public class BubbleSort implements Sort {

    @Override
    public int[] sort(int[] arr) {
        for(int i = 0;i<arr.length -1;i++){
            for(int j = arr.length-1;j > i;j--){
                if(arr[j-1] > arr[j]){
                    SortUtil.swapArr(arr,j-1,j);
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) throws Exception {
        int[] arr = SortUtil.initArr(10, 100);
        SortUtil.printArr(arr, "排序前：");
        Sort sort = new BubbleSort();
        sort.sort(arr);
        SortUtil.printArr(arr, "排序后：");
    }
}
