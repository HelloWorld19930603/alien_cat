package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

import java.util.Arrays;

public class ArraySort implements Sort {

    @Override
    public int[] sort(int[] arr) {
        Arrays.sort(arr);
        return arr;
    }

    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10,100,new ArraySort());
    }
}
