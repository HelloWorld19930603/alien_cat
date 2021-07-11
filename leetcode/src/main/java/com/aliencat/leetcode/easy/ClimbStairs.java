package com.aliencat.leetcode.easy;

/**
 * 70. 爬楼梯
 * 假设你正在爬楼梯。需要 n阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 注意：给定 n 是一个正整数。
 * <p>
 * 示例 1：
 * 输入： 2
 * 输出： 2
 * 解释： 有两种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶
 * 2.  2 阶
 * <p>
 * 示例 2：
 * 输入： 3
 * 输出： 3
 * 解释： 有三种方法可以爬到楼顶。
 * 1.  1 阶 + 1 阶 + 1 阶
 * 2.  1 阶 + 2 阶
 * 3.  2 阶 + 1 阶
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/climbing-stairs
 */
public class ClimbStairs {

    /**
     * 动态规划
     * f(1) = 1;
     * f(2) = 2;
     * 对于阶层n的爬楼组合方法数 f(n) = f(n-1) + f(n-2);
     */
    public int climbStairs(int n) {
        if (n < 4) {
            return n;
        }
        int result[] = new int[n];
        result[0] = 1;
        result[1] = 2;
        result[2] = 3;
        for (int i = 3; i < n; i++) {
            result[i] = result[i - 1] + result[i - 2];
        }
        return result[n - 1];
    }


}
/**
 * 执行用时：
 * 0 ms
 * , 在所有 Java 提交中击败了
 * 100.00%
 * 的用户
 * 内存消耗：
 * 35 MB
 * , 在所有 Java 提交中击败了
 * 88.07%
 * 的用户
 */
