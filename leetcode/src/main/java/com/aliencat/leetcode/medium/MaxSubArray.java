package com.aliencat.leetcode.medium;

/**
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * 示例 1：
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 * <p>
 * 1 <= nums.length <= 3 * 104
 * -105 <= nums[i] <= 105
 */
public class MaxSubArray {


    /**
     * 解法一：动态规划
     * 定义一个函数f(n)，以第n个数为结束点的子数列的最大和，存在一个递推关系f(n) = max(f(n-1) + A[n], A[n]);
     * 将这些最大和保存下来后，取最大的那个就是，最大子数组和。因为最大连续子数组 等价于 最大的以n个数为结束点的子数列和 附代码
     */
    public int maxSubArray(int[] nums) {
        int[] result = new int[nums.length];
        result[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result[i] = nums[i];
            result[i] = Math.max(result[i], result[i] + result[i - 1]);
            max = Math.max(result[i], max);
        }
        return max;
    }


    /**
     * 解法二
     *
     * @param nums
     * @return
     */
    public int maxSubArray2(int[] nums) {
        int res = nums[0];
        int sum = 0;
        for (int num : nums) {
            if (sum > 0)
                sum += num;
            else
                sum = num;
            res = Math.max(res, sum);
        }
        return res;
    }
}
