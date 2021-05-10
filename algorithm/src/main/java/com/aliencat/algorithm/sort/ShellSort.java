package com.aliencat.algorithm.sort;


import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;

/**
 * 希尔排序(Shell Sort)是插入排序的一种又称“缩小增量排序”（Diminishing grouprement Sort），
 * 是直接插入排序算法的一种更高效的改进版本。希尔排序是非稳定排序算法。该方法因 D.L.Shell 于 1959 年提出而得名。
 * 希尔排序是把记录按下标的一定增量分组，对每组使用直接插入排序算法排序；随着增量逐渐减少，每组包含的关键词越来越多，
 * 当增量减至 1 时，整个文件恰被分成一组，算法便终止。
 */
public class ShellSort implements Sort {


    public static void main(String[] args) throws Exception {
        while (true)
            SortUtil.printArr(10,100,new ShellSort());
    }

    /**
     * 对序列分为group个组，对序列进行 lg(n) 趟排序；
     *
     * 每趟排序，根据分组数量，将待排序列分割成若干长度为 n / group 的子序列，分别对各子序列进行直接插入排序。
     * 当分组数为 1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。
     */
    public int[] sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return arr;
        }
        //group为分组数量，当group=1的时候，此次循环正好排序完成。第一次分组时，每个子序列长度为2
        for (int group = arr.length / 2; group > 0; group /= 2) {
            for (int i = 0; i < group; i++) {
                //对每组的数进行遍历
                for (int j = i + group; j < arr.length; j += group) {
                    //对每组进行插入排序
                    for (int k = j - group; k >= 0 && arr[k] > arr[k + group]; k -= group)
                        SortUtil.swapArr(arr, k, k + group);
                }
            }
        }
        return arr;
    }
}
