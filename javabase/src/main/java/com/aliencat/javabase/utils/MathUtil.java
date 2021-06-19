package com.aliencat.javabase.utils;

import java.util.List;

/**
 * 数学相关方法工具类<br>
 */
public class MathUtil {

    //--------------------------------------------------------------------------------------------- Arrangement

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 排列数
     */
    public static long arrangementCount(int n, int m) {
        return Arrangement.count(n, m);
    }

    /**
     * 计算排列数，即A(n, n) = n!
     *
     * @param n 总数
     * @return 排列数
     */
    public static long arrangementCount(int n) {
        return Arrangement.count(n);
    }


    //--------------------------------------------------------------------------------------------- Combination

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 组合数
     */
    public static long combinationCount(int n, int m) {
        return Combination.count(n, m);
    }

    /**
     * 组合选择（从列表中选择n个组合）
     *
     * @param datas 待选列表
     * @param m     选择个数
     * @return 所有组合列表
     */
    public static List<String[]> combinationSelect(String[] datas, int m) {
        return new Combination(datas).select(m);
    }
}
