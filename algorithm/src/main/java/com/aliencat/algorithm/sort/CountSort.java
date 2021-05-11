package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 计数排序是一个非基于比较的排序算法，该算法于1954年由 Harold H. Seward 提出。
 * 它的优势在于在对一定范围内的整数排序时，它的复杂度为Ο(n+k)（其中k是整数的范围），快于任何比较排序算法。
 * 当然这是一种牺牲空间换取时间的做法，而且当O(k)>O(n*log(n))的时候其效率反而不如基于比较的排序
 * （基于比较的排序的时间复杂度在理论上的下限是O(n*log(n)), 如归并排序，堆排序）
 */
public class CountSort implements Sort {

    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new CountSort());
    }


    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        //1.找出待排序的数组中最大和最小的元素
        int minValue = arr[0];
        int maxValue = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (minValue > arr[i]) {
                minValue = arr[i];
            }
            if (maxValue < arr[i]) {
                maxValue = arr[i];
            }
        }
        int len = maxValue - minValue + 1;
        //2. 创建一个新数组count，长度为待排序数组的取值范围的宽度+1
        int[] count = new int[len];
        //3.统计源数组中每个值为a的元素出现的次数，存入数组count的第(a-minValue)项
        for (int a : arr) {
            count[a - minValue] += 1; //其中minValue对应count数组中的下标即为0
        }
        //4.反向填充源数组：将C中不为0的下标为pos元素计算后放在源数组的第i项，每放一个元素就将count[pos]减去1,同时i加1
        for (int pos = 0, i = 0; pos < len; pos++) {
            while (count[pos]-- != 0) {
                arr[i++] = pos + minValue;
            }
        }
        return arr;
    }
}
