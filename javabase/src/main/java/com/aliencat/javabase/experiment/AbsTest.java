package com.aliencat.javabase.experiment;

/**
 * 在Java中，整数的绝对值竟然不一定是正数!
 */
public class AbsTest {

    public static void main(String[] args) {
        int value = Integer.MIN_VALUE;
        System.out.println(value);           //-2147483648
        System.out.println(Math.abs(value));  //-2147483648
        System.out.println(Math.abs(value) < 0); // true

        /**
         * int的取值范围是-2^31 —— (2^31) - 1，即-2147483648 至 2147483647
         * 那么，当我们使用abs取绝对值时候，想要取得-2147483648的绝对值，那应该是2147483648。
         * 但是，2147483648大于了2147483647，即超过了int的取值范围。这时候就会发生越界。
         * 2147483647用二进制的补码表示是：
         * 01111111 11111111 11111111 11111111
         * 这个数 +1 得到：
         * 10000000 00000000 00000000 00000000
         * 这个二进制就是-2147483648的补码。
         * 虽然，这种情况发生的概率很低，只有当要取绝对值的数字是-2147483648的时候，得到的数字还是个负数。
         *
         * 那么，如何解决这个问题呢？
         * 既然是以为越界了导致最终结果变成负数，那就解决越界的问题就行了，那就是在取绝对值之前，把这个int类型转成long类型
         *
         */
        System.out.println(Math.abs((long) value)); // 2147483648
    }
}
