package com.aliencat.algorithm.sort;

import com.aliencat.algorithm.sort.common.SortUtil;
import com.aliencat.algorithm.sort.interfaces.Sort;


/**
 * 归并排序（sort Sort）是建立在归并操作上的一种有效，稳定的排序算法，
 * 该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。
 * 将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，
 * 再使子序列段间有序。若将两个有序表合并成一个有序表，称为二路归并。
 */
public class MergeSort implements Sort {


    public static void main(String[] args) throws Exception {
        SortUtil.printArr(10, 100, new MergeSort());
    }

    @Override
    public int[] sort(int[] arr) {

        return sort(arr, 0, arr.length - 1);
    }

    public int[] sort(int[] arr, int start, int end) {
        if (start >= end) {
            return new int[]{arr[end]};
        } else if (end - start == 1) {
            if (arr[start] > arr[end]) {
                return new int[]{arr[end],arr[start]};
            }
            return new int[]{arr[start],arr[end]};
        }
        int mid = (end + start) / 2;
        int[] left = sort(arr, start, mid);  //左分支
        int[] right = sort(arr, mid + 1, end);  //右分支
        return merge(left,right);
    }

    //二路归并
    public int[] merge(int[] left,int[] right){
        int[] arr = new int[left.length + right.length];
        int i = 0,j=0,k = 0;
        while(i < left.length && j < right.length){
            if(left[i] < right[j]){
                arr[k++] = left[i++];
            }else{
                arr[k++] = right[j++];
            }
        }
        //判断左右分支，若还有漏网之鱼则继续归并
        while (i < left.length){
            arr[k++] = left[i++];
        }
        while (j < right.length){
            arr[k++] = right[j++];
        }
        return arr;
    }
}
