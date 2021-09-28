package com.aliencat.leetcode.medium;

import java.util.HashMap;
import java.util.Map;

/**
 * 1371. 每个元音包含偶数次的最长子字符串
 * 给你一个字符串 s ，请你返回满足以下条件的最长子字符串的长度：每个元音字母，即 'a'，'e'，'i'，'o'，'u' ，在子字符串中都恰好出现了偶数次。
 * 示例 1：
 * 输入：s = "eleetminicoworoep"
 * 输出：13
 * 解释：最长子字符串是 "leetminicowor" ，它包含 e，i，o 各 2 个，以及 0 个 a，u 。
 * 示例 2：
 * 输入：s = "leetcodeisgreat"
 * 输出：5
 * 解释：最长子字符串是 "leetc" ，其中包含 2 个 e 。
 * 示例 3：
 * 输入：s = "bcbcbc"
 * 输出：6
 * 解释：这个示例中，字符串 "bcbcbc" 本身就是最长的，因为所有的元音 a，e，i，o，u 都出现了 0 次。
 * <p>
 * 提示：
 * 1 <= s.length <= 5 x 10^5
 * s 只包含小写英文字母。
 */
public class FindTheLongestSubstring {

    private static final char[] VOWELS = "aeiou".toCharArray();

    /**
     * 状态压缩＋哈希表
     * 将 55 个元音字母出现次数的奇偶视为一种状态，一共有 32 种状态，
     * 使用一个整数代表状态，第 0 位为 11 表示 a 出现奇数次，第一位为 1 表示 e 出现奇数次……
     * 以此类推。仅有状态 00 符合题意。
     * 而如果子串 [0，i] 与字串 [0,j] 状态相同，那么字串 [i+1,j] 的状态一定是 00，因此可以记录每个状态第一次出现的位置，此后再出现该状态时相减即可。
     * 需要注意状态 00 首次出现的位置应该设定为 -1。
     * 在计算状态的时候可以利用异或运算。
     */
    public int findTheLongestSubstring(String s) {
        Map<Integer, Integer> map = new HashMap();
        int size = s.length();
        int state = 0;
        int maxSize = 0;
        map.put(0, -1);
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < VOWELS.length; k++) {
                if (s.charAt(i) == VOWELS[k]) {
                    state ^= (1 << (VOWELS.length - k - 1));
                    break;
                }
            }
            if (map.containsKey(state)) {
                maxSize = Math.max(maxSize, i - map.get(state));
            }
            map.putIfAbsent(state, i);
        }
        return maxSize;
    }
}
