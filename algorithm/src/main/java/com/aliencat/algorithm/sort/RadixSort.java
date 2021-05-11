package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 基数排序（radix sort）属于“分配式排序”（distribution sort），又称“桶子法”（bucket sort）或bin sort，
 * 顾名思义，它是透过键值的部份资讯，将要排序的元素分配至某些“桶”中，藉以达到排序的作用，
 * 基数排序法是属于稳定性的排序，其时间复杂度为O (nlog(r)m)，其中r为所采取的基数，而m为堆数，
 * 在某些时候，基数排序法的效率高于其它的稳定性排序法。
 * 其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。
 */
public class RadixSort implements Sort {


    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10, 100, new RadixSort());
    }

    @Override
    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        //获取最大的那个值的长度，也就是它的位数
        int maxLen = getMaxLength(arr);
        int radix = 10, level = 1;
        for (int i = 0; i < maxLen; i++) {
            //考虑负数的情况下创建20个桶，其中0-9的下标放负数，10-19放正数
            //不考虑负数情况下只用创建10个桶即可
            int[][] buckets = new int[20][arr.length];
            int[] count = new int[20];
            for (int j = 0, k = 0; j < arr.length; j++) {
                //对level位以radix为基数取模
                int mod = (arr[j] / level) % radix + 10;
                buckets[mod][count[mod]++] = arr[j]; //count记录对应mod出现次数
            }
            int pos = 0;
            //将桶中数据依次放回源数组中
            for (int m = 0; m < buckets.length; m++) {
                for (int n = 0; count[m]-- > 0; n++) {
                    arr[pos++] = buckets[m][n];
                }
            }
            //位数增加一位
            level *= 10;
        }

        return arr;
    }

    public int getMaxLength(int[] arr) {
        int maxLen = (arr[0] + "").length();
        for (int i = 1; i < arr.length; i++) {
            int len = (arr[i] + "").length();
            if (maxLen < len) {
                maxLen = len;
            }
        }
        return maxLen;
    }
}
