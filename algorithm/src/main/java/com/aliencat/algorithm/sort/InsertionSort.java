package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 插入排序，一般也被称为直接插入排序。
 * 对于少量元素的排序，它是一个有效的算法。
 * 插入排序是一种最简单的排序方法，它的基本思想是将一个记录插入到已经排好序的有序表中，从而得到一个新的、记录数增1的有序表。
 * 在其实现过程使用双层循环，外层循环对除了第一个元素之外的所有元素，内层循环对当前元素前面有序表进行待插入位置查找，并进行移动。
 */
public class InsertionSort implements Sort {


    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new InsertionSort());
    }

    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0 && arr[j] < arr[j - 1]; j--) {
                SortUtil.swapArr(arr, j, j - 1);
            }
        }
        return arr;
    }
}
