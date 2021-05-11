package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

public class BubbleSort implements Sort {

    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new BubbleSort());
    }

    @Override
    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j - 1] > arr[j]) {
                    SortUtil.swapArr(arr, j - 1, j);
                }
            }
        }
        return arr;
    }
}
