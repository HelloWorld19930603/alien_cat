package com.aliencat.leetcode.medium;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 剑指 Offer 39. 数组中出现次数超过一半的数字
 * 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字。
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 * <p>
 * 示例 1:
 * 输入: [1, 2, 3, 2, 2, 2, 5, 4, 2]
 * 输出: 2
 * <p>
 * 限制：
 * 1 <= 数组长度 <= 50000
 */
public class MajorityElement {

    //方法一：使用hash表
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int len = nums.length / 2;
        for (int num : nums) {
            Integer n = map.get(num);
            if (n == null) {
                map.put(num, 1);
            } else if (n + 1 > len) {
                return num;
            } else {
                map.put(num, n + 1);
            }
        }
        return nums[0];
    }

    //方法二：排序后取中位数
    public int majorityElement2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    //方法三：摩尔投票，此方法最优解
    public int majorityElement3(int[] nums) {
        // 出现次数超过数组长度的一半，说明剩余数字的个数小于该数字的个数
        // count代表当前数字的绝对个数（比如有2个3和4个5，则5的绝对个数就是2个）
        // goal代表当前统计的是哪个数字
        int count = 0, goal = 0;
        for (int i : nums) {
            // 若绝对个数为0，则当前遍历到的数字作为要统计个数的数字
            if (count == 0)
                goal = i;
            if (goal == i) {
                // 遍历到的数字与正在统计个数的数字相同
                // 该数字个数加一
                count++;
            } else {
                // 否则个数减一
                count--;
            }
        }
        // for循环结束之后，goal则就是绝对个数最多的数字
        // 即代表其在数组中出现次数超过数组长度的一半
        return goal;
    }
}
