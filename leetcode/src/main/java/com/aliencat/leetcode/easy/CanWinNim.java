package com.aliencat.leetcode.easy;

/**
 * 难度：简单
 * 292. Nim 游戏
 * 你和你的朋友，两个人一起玩 Nim 游戏：
 * 桌子上有一堆石头。
 * 你们轮流进行自己的回合，你作为先手。
 * 每一回合，轮到的人拿掉 1 - 3 块石头。
 * 拿掉最后一块石头的人就是获胜者。
 * 假设你们每一步都是最优解。请编写一个函数，来判断你是否可以在给定石头数量为 n 的情况下赢得游戏。如果可以赢，返回 true；否则，返回 false 。
 * <p>
 * 示例 1：
 * 输入：n = 4
 * 输出：false
 * 解释：如果堆中有 4 块石头，那么你永远不会赢得比赛；
 * 因为无论你拿走 1 块、2 块 还是 3 块石头，最后一块石头总是会被你的朋友拿走。
 * 示例 2：
 * 输入：n = 1
 * 输出：true
 * 示例 3：
 * 输入：n = 2
 * 输出：true
 * <p>
 * 提示：
 * 1 <= n <= 231 - 1
 */
public class CanWinNim {

    /**
     * 如果n为4的倍数，那么第一个人无论取几个石头，第二个人总能取一定石头使得第一轮消耗的石头总数为4
     * 那么第二个人总能拿到最后一块石头
     * 如果n不为4的倍数，当第一个人取一定数目石头后使得剩下石头变为4的倍数,也就是会变成上面的情况
     * 那么第一人总能拿到最后一块石头
     */
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }
}
/**
 * 执行用时：
 * 0 ms
 * , 在所有 Java 提交中击败了
 * 100.00%
 * 的用户
 * 内存消耗：
 * 35.2 MB
 * , 在所有 Java 提交中击败了
 * 46.12%
 * 的用户
 **/