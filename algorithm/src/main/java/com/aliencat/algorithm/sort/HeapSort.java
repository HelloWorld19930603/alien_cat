package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。
 * 堆是一个近似完全二叉树的结构，并同时满足堆的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。
 * 堆排序的基本思想是：将待排序序列构造成一个大顶堆，此时，整个序列的最大值就是堆顶的根节点。
 * 将其与末尾元素进行交换，此时末尾就为最大值。然后将剩余n-1个元素重新构造成一个堆，这样会得到n个元素的次小值。
 * 如此反复执行，便能得到一个有序序列了。
 */
public class HeapSort implements Sort {


    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new HeapSort());
    }

    /**
     * 1.创建一个堆 H[0……n-1]；
     * 2.把堆首（最大值）和堆尾互换；
     * 3.把堆的尺寸缩小 1，并把新的数组顶端数据调整到相应位置；
     * 4.重复步骤 2，直到堆的尺寸为 1。
     */
    public int[] sort(int[] arr) {
        //创建一个大顶堆
        buildMaxHeap(arr);

        for (int i = arr.length - 1; i > 0; i--) {
            SortUtil.swapArr(arr, 0, i);
            heapify(arr, 0, i);
        }
        return arr;
    }

    public void buildMaxHeap(int[] arr) {
        for (int i = (arr.length - 2)/ 2; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }

    }

    public void heapify(int[] arr, int i, int len) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;
        if (left < len && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < len && arr[right] > arr[largest]) {
            largest = right;
        }
        if (i != largest) {
            SortUtil.swapArr(arr, i, largest);
            heapify(arr, largest, len);
        }

    }
}
