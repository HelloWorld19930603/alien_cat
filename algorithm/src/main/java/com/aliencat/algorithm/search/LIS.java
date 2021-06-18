package com.aliencat.algorithm.search;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 最长上升子序列(LIS)
 * LIS的定义
 * LIS指的是最长上升/递增子序列(Longest Increasing Subsequence)。
 * 首先给出上升序列的概念，如果某个序列有如下性质:
 * (x1, x2,...,xn)，x1 < x2 < ... < xn
 * 那么就称该序列是上升的。
 * 那么LIS类问题就是求0-n中最长的那个上升子序列
 */
public class LIS { //Longest Increasing Subsequence

    //LIS的长度求解
    //方法一：动态规划方法  时间复杂度：O(n^2)
    public int lengthOfLIS(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int max = 1;
        int[] dp = new int[len];
        Arrays.fill(dp, 1);
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                }
            }
            max = Math.max(dp[i], max);
        }
        return max;
    }

    //方法二：贪心+二分法  时间复杂度：O(nlogn)
    public int lengthOfLIS2(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }

        LinkedList<Integer> slow = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            int ele = nums[i];
            if (slow.isEmpty() || ele > slow.getLast()) {
                slow.add(ele);
            } else {
                int idx = binarySearchLargerEleIndex(slow, ele);
                slow.set(idx, ele);
            }
        }

        return slow.size();
    }

    private int binarySearchLargerEleIndex(LinkedList<Integer> low, int val) {
        int left = 0;
        int right = low.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int ele = low.get(mid);
            if (ele < val) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

}
