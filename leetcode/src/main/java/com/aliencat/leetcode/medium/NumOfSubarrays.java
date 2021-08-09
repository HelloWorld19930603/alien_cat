package com.aliencat.leetcode.medium;

/**
 * 1524. 和为奇数的子数组数目
 * 给你一个整数数组 arr 。请你返回和为 奇数 的子数组数目。
 * 由于答案可能会很大，请你将结果对 10^9 + 7 取余后返回。
 * 示例 1：
 * 输入：arr = [1,3,5]
 * 输出：4
 * 解释：所有的子数组为 [[1],[1,3],[1,3,5],[3],[3,5],[5]] 。
 * 所有子数组的和为 [1,4,9,3,8,5].
 * 奇数和包括 [1,9,3,5] ，所以答案为 4 。
 * 示例 2 ：
 * 输入：arr = [2,4,6]
 * 输出：0
 * 解释：所有子数组为 [[2],[2,4],[2,4,6],[4],[4,6],[6]] 。
 * 所有子数组和为 [2,6,12,4,10,6] 。
 * 所有子数组和都是偶数，所以答案为 0 。
 * 示例 3：
 * 输入：arr = [1,2,3,4,5,6,7]
 * 输出：16
 * 示例 4：
 * 输入：arr = [100,100,99,99]
 * 输出：4
 * 示例 5：
 * 输入：arr = [7]
 * 输出：1
 */
public class NumOfSubarrays {

    /**
     * 为了快速计算任意子数组的和，可以通过维护前缀和的方式。
     * 这道题只需要知道每个子数组的和的奇偶性，不需要知道子数组的和的具体值，
     * 因此不需要维护每一个前缀和，只需要维护奇数前缀和的数量与偶数前缀和的数量
     */
    public int numOfSubarrays(int[] arr) {
        int[] s = new int[]{1, 0};
        long result = 0;
        /**
         * 遍历数组 arr 并计算前缀和。对于下标 i 的位置的前缀和（即 arr[0]+arr[1]+…+arr[i]），根据奇偶性进行如下操作：
         * - 当下标 ii 的位置的前缀和是偶数时，如果下标 jj 满足 j < ij<i 且下标 jj 的位置的前缀和是奇数，
         *  则从下标 j+1j+1 到下标 ii 的子数组的和是奇数，因此，以下标 ii 结尾的子数组中，
         *  和为奇数的子数组的数量即为奇数前缀和的数量 \textit{odd}odd；
         * -当下标 ii 的位置的前缀和是奇数时，如果下标 jj 满足 j < ij<i 且下标 jj 的位置的前缀和是偶数，
         *  则从下标 j+1j+1 到下标 ii 的子数组的和是奇数，因此，以下标 ii 结尾的子数组中，
         *  和为奇数的子数组的数量即为偶数前缀和的数量 \textit{even}even。
         */
        for (int i = 0, sum = 0; i < arr.length; i++) {
            ++s[sum ^= arr[i] & 1];
            result += s[sum ^ 1];
        }
        return (int) (result % 1000000007);
    }
}
