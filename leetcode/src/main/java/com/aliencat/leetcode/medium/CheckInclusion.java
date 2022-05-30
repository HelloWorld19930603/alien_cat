package com.aliencat.leetcode.medium;

import java.util.Arrays;

/**
 * 剑指 Offer II 014. 字符串中的变位词
 *
 * 给定两个字符串s1和s2，写一个函数来判断 s2 是否包含 s1的某个变位词。
 * 换句话说，第一个字符串的排列之一是第二个字符串的 子串 。
 *
 * 示例 1：
 * 输入: s1 = "ab" s2 = "eidbaooo"
 * 输出: True
 * 解释: s2 包含 s1 的排列之一 ("ba").
 * 示例 2：
 * 输入: s1= "ab" s2 = "eidboaoo"
 * 输出: False
 *
 * 提示：
 * 1 <= s1.length, s2.length <= 104
 * s1 和 s2 仅包含小写字母
 */
public class CheckInclusion {

    public boolean checkInclusion(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        if (m > n) {
            return false;
        }
        int[] cnt1 = new int[26];
        int[] cnt2 = new int[26];
        //滑动窗口法比较
        for (int i = 0; i < m; i++) {
            cnt1[s1.charAt(i) - 'a']++;
            cnt2[s2.charAt(i) - 'a']++;
        }
        if (Arrays.equals(cnt1, cnt2))
            return true;
        for (int i = m; i < n; i++) {
            cnt2[s2.charAt(i - m) - 'a']--;
            cnt2[s2.charAt(i) - 'a']++;
            if (Arrays.equals(cnt1, cnt2))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String s1 = "adc";
        String s2 = "dcda";

        System.out.println(new CheckInclusion().checkInclusion(s1, s2));

    }
}
