package com.aliencat.leetcode.hard;

/**
 * 5. 最长回文子串
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * <p>
 * 示例 1：
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * 示例 2：
 * 输入：s = "cbbd"
 * 输出："bb"
 * 示例 3：
 * 输入：s = "a"
 * 输出："a"
 * 示例 4：
 * 输入：s = "ac"
 * 输出："a"
 * <p>
 * 提示：
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母（大写和/或小写）组成
 */
public class LongestPalindrome {

    //动态规划问题
    public String longestPalindrome(String s) {
        char[] src = s.toCharArray();
        int start = 0;
        int max = 1;
        boolean[][] result = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) { //一个字符必定回文
                    result[i][j] = true;
                } else if (i - j == 1) { //两个字符比较它们是否相等即可
                    result[i][j] = src[i] == src[j];
                } else {  //比较i和j的位置是否相对，再看这两位置间的子串(i-1)到(j+1)是否回文
                    result[i][j] = src[i] == src[j] && result[i - 1][j + 1];
                }
                if (result[i][j] && (i - j + 1) > max) {
                    max = i - j + 1;
                    start = j;
                }
            }
        }
        if (max == 1) {
            return String.valueOf(src[0]);
        }
        char[] copy = new char[max];
        System.arraycopy(src, start, copy, 0,
                max);
        return new String(copy);
    }

}
