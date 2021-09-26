package com.aliencat.leetcode.medium;

/**
 * 剑指 Offer II 099. 最小路径之和
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：一个机器人每次只能向下或者向右移动一步。
 * 示例 1：
 * 输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
 * 输出：7
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 * 示例 2：
 * 输入：grid = [[1,2,3],[4,5,6]]
 * 输出：12
 * <p>
 * 提示：
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * 0 <= grid[i][j] <= 100
 */
public class MinPathSum {

    public static void main(String[] args) {
        int[][] grid = {{1, 2, 3}, {4, 5, 6}};
        System.out.println(new MinPathSum().minPathSum(grid));
    }

    public int minPathSum(int[][] grid) {
        /**
         * 动态规划
         * 首先考虑边界条件,走第一行或者第一列只有一条路可以走,就是一直走右或者一直往下走
         * 则很容易知道第一行和第一列的每个元素的最小花费dp[i][0]+=dp[i-1][0],dp[0][i]+=dp[0][i-1]
         * 这里我直接用原数组来保存dp结果
         */
        for (int i = 1; i < grid[0].length; i++) {
            grid[0][i] += grid[0][i - 1];
        }
        for (int j = 1; j < grid.length; j++) {
            grid[j][0] += grid[j - 1][0];
        }

        /**
         *  到达dp[i][j]只有两条路,从dp[i-1][j]或者dp[i][j-1]
         *  然后计算他们的最小值，重复操作直到循环结束即得最终结果
         */
        for (int i = 1; i < grid.length; i++) {
            for (int j = 1; j < grid[0].length; j++) {
                grid[i][j] = Math.min(grid[i - 1][j], grid[i][j - 1]) + grid[i][j];
            }
        }
        return grid[grid.length - 1][grid[0].length - 1];
    }
}
/**
 * 执行用时：
 * 2 ms , 在所有 Java 提交中击败了 96.99% 的用户
 * 内存消耗：41.2 MB , 在所有 Java 提交中击败了44.37% 的用户
 * 通过测试用例：
 * 61 / 61
 */
