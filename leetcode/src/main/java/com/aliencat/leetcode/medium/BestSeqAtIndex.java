package com.aliencat.leetcode.medium;

import java.util.Arrays;

//面试题 17.08. 马戏团人塔
public class BestSeqAtIndex {

    public static void main(String[] args) {
        int[] height = {2868, 5485, 1356, 1306, 6017, 8941, 7535, 4941, 6331, 6181};
        int[] weight = {5042, 3995, 7985, 1651, 5991, 7036, 9391, 428, 7561, 8594};
        System.out.println(new BestSeqAtIndex().bestSeqAtIndex(height, weight));
    }

    /**
     * 有个马戏团正在设计叠罗汉的表演节目，一个人要站在另一人的肩膀上。出于实际和美观的考虑，
     * 在上面的人要比下面的人矮一点且轻一点。已知马戏团每个人的身高和体重，请编写代码计算叠罗汉最多能叠几个人。
     * 示例：
     * 输入：height = [65,70,56,75,60,68] weight = [100,150,90,190,95,110]
     * 输出：6
     * 解释：从上往下数，叠罗汉最多能叠 6 层：(56,90), (60,95), (65,100), (68,110), (70,150), (75,190)
     * 提示：
     * height.length == weight.length <= 10000
     *
     * @param height
     * @param weight
     * @return
     */
    public int bestSeqAtIndex(int[] height, int[] weight) {
        int len = height.length;
        int[][] person = new int[len][2];
        for (int i = 0; i < len; ++i)
            person[i] = new int[]{height[i], weight[i]};

        Arrays.sort(person, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
        int[] dp = new int[len];
        int res = 0;
        for (int[] pair : person) {
            int i = Arrays.binarySearch(dp, 0, res, pair[1]);
            if (i < 0)
                i = -(i + 1);
            dp[i] = pair[1];
            if (i == res)
                ++res;
        }
        return res;
    }
}
