package com.aliencat.leetcode.medium;

/**
 * 985. 查询后的偶数和
 * 给出一个整数数组 A 和一个查询数组 queries。
 * 对于第 i 次查询，有 val = queries[i][0], index = queries[i][1]，我们会把 val 加到 A[index] 上。然后，第 i 次查询的答案是 A 中偶数值的和。
 * （此处给定的 index = queries[i][1] 是从 0 开始的索引，每次查询都会永久修改数组 A。）
 * 返回所有查询的答案。你的答案应当以数组 answer 给出，answer[i] 为第 i 次查询的答案。
 * 示例：
 * 输入：A = [1,2,3,4], queries = [[1,0],[-3,1],[-4,0],[2,3]]
 * 输出：[8,6,2,4]
 * 解释：
 * 开始时，数组为 [1,2,3,4]。
 * 将 1 加到 A[0] 上之后，数组为 [2,2,3,4]，偶数值之和为 2 + 2 + 4 = 8。
 * 将 -3 加到 A[1] 上之后，数组为 [2,-1,3,4]，偶数值之和为 2 + 4 = 6。
 * 将 -4 加到 A[0] 上之后，数组为 [-2,-1,3,4]，偶数值之和为 -2 + 4 = 2。
 * 将 2 加到 A[3] 上之后，数组为 [-2,-1,3,6]，偶数值之和为 -2 + 6 = 4。
 */
public class SumEvenAfterQueries {


    /**
     * 先用依次遍历求出所有偶数和；
     * 遍历查询数组，改变原数组值之后有四种情况：
     * 1、nums[index] 是偶数，val 是偶数，总和要加 val
     * 2、nums[index] 是偶数，val 是奇数，总和要加当前的 nums[index]，因为产生了一个新偶数
     * 3、nums[index] 是奇数，val 是偶数，不用管，因为原来的 nums[index] 是奇数
     * 4、nums[index] 是奇数，val 是奇数，总和要减先前的 nums[index]，即当前 nums[index]-val，少了一个偶数
     */
    public int[] sumEvenAfterQueries(int[] nums, int[][] queries) {
        int sum = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                sum += num;
            }
        }
        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int index = queries[i][1];
            int tmp = queries[i][0] + nums[index];
            if (nums[index] % 2 == 0) {
                sum -= nums[index];
            }
            if (tmp % 2 == 0) {
                sum += tmp;
            }
            nums[index] = tmp;
            answer[i] = sum;
        }
        return answer;
    }
}
