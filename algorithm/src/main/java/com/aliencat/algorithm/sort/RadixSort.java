package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 基数排序（radix sort）属于“分配式排序”（distribution sort），又称“桶子法”（bucket sort）或bin sort，
 * 顾名思义，它是透过键值的部份资讯，将要排序的元素分配至某些“桶”中，藉以达到排序的作用，
 * 基数排序法是属于稳定性的排序，其时间复杂度为O (nlog(r)m)，其中r为所采取的基数，而m为堆数，
 * 在某些时候，基数排序法的效率高于其它的稳定性排序法。
 * 其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。
 * 由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，所以基数排序也不是只能使用于整数。
 */
public class RadixSort implements Sort {


    @Override
    public int[] sort(int[] arr) {
        return new int[0];
    }
}
