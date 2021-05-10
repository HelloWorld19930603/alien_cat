package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 桶排序 (Bucket sort)或所谓的箱排序，是一个排序算法，工作的原理是将数组分到有限数量的桶子里。
 * 每个桶子再个别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排序）。
 * 桶排序是鸽巢排序的一种归纳结果。当要被排序的数组内的数值是均匀分配的时候，桶排序使用线性时间（Θ（n））。
 * 但桶排序并不是 比较排序，他不受到 O(n log n) 下限的影响。
 */
public class BucketSort implements Sort {

    @Override
    public int[] sort(int[] arr) {
        return new int[0];
    }
}
