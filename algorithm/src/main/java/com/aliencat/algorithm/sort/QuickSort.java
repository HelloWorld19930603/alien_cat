package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 快速排序（Quicksort）是对冒泡排序算法的一种改进。
 * 快速排序由C. A. R. Hoare在1960年提出。
 * 它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，
 * 其中一部分的所有数据都比另外一部分的所有数据都要小，然后再按此方法对这两部分数据分别进行快速排序，
 * 整个排序过程可以递归进行，以此达到整个数据变成有序序列
 */
public class QuickSort implements Sort {

    /**
     * 1．先从数列中取出一个数作为基准数。
     * 2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
     * 3．再对左右区间重复第二步，直到各区间只有一个数。
     */
    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        return sort(arr,0,arr.length - 1);
    }

    public int[] sort(int[] arr ,int start,int end){
        if(start >= end){
            return arr;
        }
        int key = arr[start];
        int i= start,j = end;
        while(i < j){
            //从右向左找第一个小于key的数
            while (i < j && arr[j] > key){
                j--;
            }
            if(i < j)
                SortUtil.swapArr(arr,i++,j);
            //从左向右找第一个大于或等于于key的数
            while (i < j && arr[i] <= key){
                i++;
            }
            if(i < j)
                SortUtil.swapArr(arr,i,j--);
        }
        //此时i必等于j,左右交汇的位置就是key的位置
        arr[i] = key;
        //对[start,i)区间进行快速排序
        sort(arr, start, i - 1);
        //对（i,end]区间进行快速排序
        sort(arr, i + 1, end);

        return arr;
    }

    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new QuickSort());

    }
}
