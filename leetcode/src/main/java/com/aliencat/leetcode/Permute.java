package com.aliencat.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 46. 全排列
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 *
 * 示例 1：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 示例 2：
 * 输入：nums = [0,1]
 * 输出：[[0,1],[1,0]]
 * 示例 3：
 * 输入：nums = [1]
 * 输出：[[1]]
 *
 * 提示：
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * nums 中的所有整数 互不相同
 *
 * https://leetcode-cn.com/problems/permutations/
 */
public class Permute {

    List<List<Integer>> result = new ArrayList();
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> list = new ArrayList<>(nums.length);
        permute(nums.length,nums,new boolean[nums.length],new ArrayList<>());
        return result;
    }

    /**
     * 递归的方式解
     * @param n 递归的深度
     * @param nums 要遍历的数组
     * @param marks 每一层标记已经遍历过的数，true标记已经使用过的
     * @param list
     */
    public void permute(int n,int[] nums,boolean[] marks,ArrayList<Integer> list){
        if(n == 0){ //n=0说明所有数都已经使用过了
            result.add(list);
            return;
        }
        for(int i = 0;i<marks.length;i++){
            if(!marks[i]){
                marks[i] = true;  //下一次递归中要把该值置为true，表示其后不能再使用
                ArrayList<Integer> listClone = (ArrayList<Integer>) list.clone(); //通过clone复制一个新的list
                listClone.add(nums[i]);
                permute(n-1,nums,marks,listClone);
                marks[i]=false; //递归后要把这轮标记重置
            }
        }
    }
}
/**
 * 执行用时：
 * 1 ms
 * , 在所有 Java 提交中击败了
 * 97.59%
 * 的用户
 * 内存消耗：
 * 38.4 MB
 * , 在所有 Java 提交中击败了
 * 89.36%
 * 的用户
 */
