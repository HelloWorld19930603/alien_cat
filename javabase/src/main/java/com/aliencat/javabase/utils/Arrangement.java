package com.aliencat.javabase.utils;


import java.io.Serializable;

/**
 * 排列A(n, m)<br>
 * 排列组合相关类
 */
public class Arrangement implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String[] datas;

    /**
     * 构造
     *
     * @param datas 用于排列的数据
     */
    public Arrangement(String[] datas) {
        this.datas = datas;
    }

    /**
     * 计算排列数，即A(n, n) = n!
     *
     * @param n 总数
     * @return 排列数
     */
    public static long count(int n) {
        return count(n, n);
    }

    /**
     * 计算排列数，即A(n, m) = n!/(n-m)!
     *
     * @param n 总数
     * @param m 选择的个数
     * @return 排列数
     */
    public static long count(int n, int m) {
        if (n == m) {
            return NumberUtil.factorial(n);
        }
        return (n > m) ? NumberUtil.factorial(n, n - m) : 0;
    }

    /**
     * 计算排列总数，即A(n, 1) + A(n, 2) + A(n, 3)...
     *
     * @param n 总数
     * @return 排列数
     */
    public static long countAll(int n) {
        long total = 0;
        for (int i = 1; i <= n; i++) {
            total += count(n, i);
        }
        return total;
    }


}
