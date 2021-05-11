package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
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
        if (arr == null || arr.length <= 1) {
            return arr;
        }

        return sort(arr,5);
    }

    //bucketSize为初始化桶的个数
    public int[] sort(int[] arr,int bucketSize){

        int maxValue = arr[0];
        int minValue = arr[0];
        //找打源数组中最大值和最小值
        for(int i = 1;i<arr.length;i++){
            if(maxValue < arr[i]){
                maxValue = arr[i];
            }else if(minValue > arr[i]){
                minValue = arr[i];
            }
        }
        int[][] buckets = new int[bucketSize][arr.length];
        int [] count = new int[bucketSize];
        int range = (maxValue - minValue) / bucketSize +1;
        for(int a : arr){
            //按a的取值范围找到对应的桶
            int b = (a - minValue) / range;
            buckets[b][count[b]++] = a;
        }
        for(int[] bucket : buckets){
            //此处对每个桶采用了插入排序，当然也可以替换为其它排序算法
            for (int i = 1; i < count[i]; i++) {
                for (int j = i; j > 0 && bucket[j] < bucket[j - 1]; j--) {
                    SortUtil.swapArr(bucket, j, j - 1);
                }
            }
        }
        //遍历每个桶，依次放到源数组中即完成排序
        for(int i = 0,pos = 0;i< bucketSize;i++){
            for(int j = 0;count[i]-- > 0;j++){
                arr[pos] = buckets[i][j];
            }
        }
        return arr;
    }

    public static void main(String[] args) throws Exception {
        while (true){
            SortUtil.printArr(20,100,new BucketSort());
        }
    }
}
