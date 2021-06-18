package com.aliencat.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 354. 俄罗斯套娃信封问题
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * 请计算 最多能有多少个 信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
 * 注意：不允许旋转信封。
 * <p>
 * 示例 1：
 * 输入：envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * 输出：3
 * 解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
 * 示例 2：
 * 输入：envelopes = [[1,1],[1,1],[1,1]]
 * 输出：1
 * <p>
 * 提示：
 * 1 <= envelopes.length <= 5000
 * envelopes[i].length == 2
 * 1 <= wi, hi <= 104
 */
public class MaxEnvelopes {

    public int maxEnvelopes(int[][] envelopes) {
        //对一维数组排序，在一维数组单调递增的情况下转化为求二维数组最长上升子序列问题
        Arrays.sort(envelopes, Comparator.comparingInt(o -> o[0]));
        int len = envelopes.length;
        int[] result = new int[envelopes.length];
        result[0] = 1;
        int max = 1;
        //动态规划
        for (int i = 1; i < result.length; i++) {
            result[i] = 1;
            for (int j = i - 1; j >= 0; j--) {
                //在j<i的情况下，如果result[i]比result[j]小，则j肯定不在0-i的最长子序列中
                if (envelopes[i][1] > envelopes[j][1] && envelopes[i][0] != envelopes[j][0]) {
                    result[i] = Math.max(result[i], result[j] + 1);
                }
            }
            //result[i]即为0-i的子串且比envelopes[i][1]小的数中最大上升子序列的长度
            max = Math.max(result[i], max);
        }
        return max;
    }

    public static void main(String[] args) {
        //int[][] envelopes = {{1, 3}, {3, 5}, {6, 7}, {6, 8}, {8, 4}, {9, 5}};
        //int[][] envelopes = {{1, 1}, {1, 1}, {1, 1}};
        //int[][] envelopes = {{7, 8}, {12, 16}, {12, 5}, {1, 8}, {4, 19}, {3, 15}, {4, 10}, {9, 16}};
        int[][] envelopes = {{46, 89}, {50, 53}, {52, 68}, {72, 45}, {77, 81}};

        //int[][] envelopes = {{5, 4}, {6, 4}, {6, 7}, {2, 3}};
/*        Arrays.sort(envelopes, (o1, o2) -> {
            if (o1[0] > o2[0]) {
                return 1;
            } else if (o1[0] == o2[0]) {
                return 1;
            } else {
                return -1;
            }
        });*/

        System.out.println(new MaxEnvelopes().maxEnvelopes(envelopes));
    }
}
