package com.aliencat.leetcode;

/**
 * 1397. 找到所有好字符串
 * 给你两个长度为 n 的字符串 s1 和 s2 ，以及一个字符串 evil 。请你返回 好字符串 的数目。
 * 好字符串 的定义为：它的长度为 n ，字典序大于等于 s1 ，字典序小于等于 s2 ，且不包含 evil 为子字符串。
 * 由于答案可能很大，请你返回答案对 10^9 + 7 取余的结果。
 *
 * 示例 1：
 * 输入：n = 2, s1 = "aa", s2 = "da", evil = "b"
 * 输出：51
 * 解释：总共有 25 个以 'a' 开头的好字符串："aa"，"ac"，"ad"，...，"az"。还有 25 个以 'c' 开头的好字符串："ca"，"cc"，"cd"，...，"cz"。最后，还有一个以 'd' 开头的好字符串："da"。
 * 示例 2：
 * 输入：n = 8, s1 = "leetcode", s2 = "leetgoes", evil = "leet"
 * 输出：0
 * 解释：所有字典序大于等于 s1 且小于等于 s2 的字符串都以 evil 字符串 "leet" 开头。所以没有好字符串。
 * 示例 3：
 * 输入：n = 2, s1 = "gx", s2 = "gz", evil = "x"
 * 输出：2
 *
 * s1.length == n
 * s2.length == n
 * s1 <= s2
 * 1 <= n <= 500
 * 1 <= evil.length <= 50
 * 所有字符串都只包含小写英文字母。
 */
public class FindGoodStrings {

    //数位 DP + KMP 匹配
    public int findGoodStrings(int n, String s1, String s2, String evil) {
        int mod = (int) 1e9 + 7;
        int m = evil.length();
        //一个维度表示遍历的索引，一个维度表示当前可选字符的限制，一个维度表示已经匹配的evil的长度
        // 第二维度中， 0表示s1和s2都有限制，1表s1有限制， 2表示s2有限制， 3表示s1和s2无限制； 第三维度表示前面已经匹配的evil的长度
        long[][][] dp = new long[n + 1][4][m + 1];
        for (int i = 0; i < m; i++) {
            dp[n][0][i] = 1;
            dp[n][1][i] = 1;
            dp[n][2][i] = 1;
            dp[n][3][i] = 1;
        }
        char[] p = evil.toCharArray();
        int[] prefix = calcuPrefixFunction(p); // O(n)，计算前缀数组
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                // handle 0
                for (char k = s1.charAt(i); k <= s2.charAt(i); k++) {
                    int state = 0;
                    if (k == s1.charAt(i) && k == s2.charAt(i)) {
                        state = 0;
                    } else if (k == s1.charAt(i)) {
                        state = 1;
                    } else if (k == s2.charAt(i)) {
                        state = 2;
                    } else {
                        state = 3;
                    }
                    dp[i][0][j] += dp[i + 1][state][getNext(prefix, p, k, j)];
                    dp[i][0][j] %= mod;
                }
                // handle 1
                for (char k = s1.charAt(i); k <= 'z'; k++) {
                    int state = k == s1.charAt(i) ? 1 : 3;
                    dp[i][1][j] += dp[i + 1][state][getNext(prefix, p, k, j)];
                    dp[i][1][j] %= mod;
                }
                //handle 2
                for (char k = 'a'; k <= s2.charAt(i); k++) {
                    int state = k == s2.charAt(i) ? 2 : 3;
                    dp[i][2][j] += dp[i + 1][state][getNext(prefix, p, k, j)];
                    dp[i][2][j] %= mod;
                }
                // handle 3
                for (char k = 'a'; k <= 'z'; k++) {
                    int state = 3;
                    dp[i][3][j] += dp[i + 1][state][getNext(prefix, p, k, j)];
                    dp[i][3][j] %= mod;
                }
            }
        }
        return (int) dp[0][0][0];
    }

    private int[] calcuPrefixFunction(char[] p) { // 考虑边界情况， 即p的长度为0
        int n = p.length;
        int[] prefixArray = new int[n];  // 表示匹配的长度结果
        prefixArray[0] = 0;
        int j = 0;  // 表示匹配的长度
        for (int i = 1; i < n; i++) {
            while (j > 0 && p[i] != p[j]) {
                j = prefixArray[j - 1];
            }
            if (p[i] == p[j]) {
                j++;
            }
            prefixArray[i] = j;
        }
        return prefixArray;
    }

    private int getNext(int[] prefix, char[] p, char c, int j) {
        while (j > 0 && c != p[j]) {
            j = prefix[j - 1];
        }
        if (c == p[j]) {
            j++;
        }
        return j;
    }
}
