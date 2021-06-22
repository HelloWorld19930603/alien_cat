package com.aliencat.leetcode;

/**
 * 670. 最大交换
 * 给定一个非负整数，你至多可以交换一次数字中的任意两位。返回你能得到的最大值。
 *
 * 示例 1 :
 * 输入: 2736
 * 输出: 7236
 * 解释: 交换数字2和数字7。
 * 示例 2 :
 * 输入: 9973
 * 输出: 9973
 * 解释: 不需要交换。
 * 注意:
 * 给定数字的范围是 [0, 10^8]
 */
public class MaximumSwap {

    public int maximumSwap(Integer num) {
        if(num < 11)    //小于11说明不需要交换
            return num;
        char[] chs = Integer.toString(num).toCharArray();
        int[] maxIndex = new int[chs.length];
        int max = chs.length - 1;
        //倒过来寻找，当前位置往右，最大的数的下标
        for(int j = chs.length - 2;j>=0;j--){
            if(chs[j] > chs[max]){
                max = j;
            }
            maxIndex[j] = max;
        }
        //正序遍历，找到第一个不是最大的数，将该位置和右边最大数换位置
        for(int i = 0;i<chs.length;i++){
            int iValue = chs[i] - '0';
            int maxValue = chs[maxIndex[i]] - '0';
            if(maxValue != iValue){
                chs[i] = (char) (maxValue + '0');
                chs[maxIndex[i]] = (char) (iValue + '0');
                break;
            }
        }
        return Integer.parseInt(new String(chs));
    }
}
