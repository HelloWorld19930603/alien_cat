package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 计数排序是一个非基于比较的排序算法，该算法于1954年由 Harold H. Seward 提出。
 * 它的优势在于在对一定范围内的整数排序时，它的复杂度为Ο(n+k)（其中k是整数的范围），快于任何比较排序算法。
 * 当然这是一种牺牲空间换取时间的做法，而且当O(k)>O(n*log(n))的时候其效率反而不如基于比较的排序
 * （基于比较的排序的时间复杂度在理论上的下限是O(n*log(n)), 如归并排序，堆排序）
 */
public class CountSort implements Sort {

    /**
     *  1.找出待排序的数组中最大和最小的元素
     *  2.统计数组中每个值为i的元素出现的次数，存入数组C的第i项
     *  3.对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）
     *  4.反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1
     */
    public int[] sort(int[] arr) {
        return new int[0];
    }
}
