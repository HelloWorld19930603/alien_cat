package com.aliencat.leetcode.easy;

/**
 * 914. 卡牌分组
 * 给定一副牌，每张牌上都写着一个整数。
 * 此时，你需要选定一个数字 X，使我们可以将整副牌按下述规则分成 1 组或更多组：
 * 每组都有 X 张牌。
 * 组内所有的牌上都写着相同的整数。
 * 仅当你可选的 X >= 2 时返回 true。
 * <p>
 * 示例 1：
 * 输入：[1,2,3,4,4,3,2,1]
 * 输出：true
 * 解释：可行的分组是 [1,1]，[2,2]，[3,3]，[4,4]
 * 示例 2：
 * 输入：[1,1,1,2,2,2,3,3]
 * 输出：false
 * 解释：没有满足要求的分组。
 * 示例 3：
 * 输入：[1]
 * 输出：false
 * 解释：没有满足要求的分组。
 * 示例 4：
 * 输入：[1,1]
 * 输出：true
 * 解释：可行的分组是 [1,1]
 * 示例 5：
 * 输入：[1,1,2,2,2,2]
 * 输出：true
 * 解释：可行的分组是 [1,1]，[2,2]，[2,2]
 * <p>
 * 提示：
 * 1 <= deck.length <= 10000
 * 0 <= deck[i] < 10000
 */
public class HasGroupsSizeX {

    public boolean hasGroupsSizeX(int[] deck) {
        int[] nums = new int[10000];
        //把相同的数归组
        for (int d : deck) {
            nums[d]++;
        }
        int g = 0;
        for (int n : nums) {
            g = gcd(g, n);
            if (g == 1)       //true说明存在一个n使得n与它前面的某个数的最大公约数为1
                return false;
        }
        return true;
    }

    //递归求最大公约数
    public int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }
}
