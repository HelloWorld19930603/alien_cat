package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 选择排序（Selection sort）是一种简单直观的排序算法。
 * 它的工作原理是：第一次从待排序的数据元素中选出最小（或最大）的一个元素，存放在序列的起始位置，
 * 然后再从剩余的未排序元素中寻找到最小（大）元素，然后放到已排序的序列的末尾。
 * 以此类推，直到全部待排序的数据元素的个数为零。选择排序是不稳定的排序方法。
 * 最好情况是，已经有序，交换0次；最坏情况交换n-1次。。
 */
public class SelectionSort implements Sort {


    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new SelectionSort());
    }

    public int[] sort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            //记录最小值的下标
            int tmp = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[tmp] > arr[j]) {
                    tmp = j;
                }
            }
            if (tmp != i) {
                //交换数组中的两个数
                SortUtil.swapArr(arr, i, tmp);
            }
        }
        return arr;
    }
}
