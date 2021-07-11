package com.aliencat.leetcode.medium;

/**
 * 788. 旋转数字
 * 我们称一个数 X 为好数, 如果它的每位数字逐个地被旋转 180 度后，我们仍可以得到一个有效的，且和 X 不同的数。要求每位数字都要被旋转。
 * 如果一个数的每位数字被旋转以后仍然还是一个数字， 则这个数是有效的。0, 1, 和 8 被旋转后仍然是它们自己；2 和 5 可以互相旋转成对方（在这种情况下，它们以不同的方向旋转，换句话说，2 和 5 互为镜像）；6 和 9 同理，除了这些以外其他的数字旋转以后都不再是有效的数字。
 * 现在我们有一个正整数 N, 计算从 1 到 N 中有多少个数 X 是好数？
 * <p>
 * 示例：
 * 输入: 10
 * 输出: 4
 * 解释:
 * 在[1, 10]中有四个好数： 2, 5, 6, 9。
 * 注意 1 和 10 不是好数, 因为他们在旋转之后不变。
 * <p>
 * 提示：
 * N 的取值范围是 [1, 10000]。
 */
public class RotatedDigits {

    //当数字含有3、4、7，over
    //当数字不包含任何2、5、6、9，over
    //使用字符串匹配的方式
    public int rotatedDigits(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            String s = String.valueOf(i);
            if (s.contains("2") || s.contains("5") || s.contains("6") || s.contains("9")) {
                if (!s.contains("3") && !s.contains("7") && !s.contains("4")) {
                    count++;
                }
            }
        }
        return count;
    }

    //方法二：使用数字匹配的方式，此方式效率高
    public int rotatedDigits2(int n) {
        int count = 0;
        while (n > 1) {
            if (checkDigits(n)) {
                count++;
            }
            n--;
        }
        return count;
    }

    public boolean checkDigits(int n) {
        boolean[] flags = new boolean[10];
        int count = 0;
        while (n > 1) {
            int num = n % 10;
            if (num == 3 || num == 4 || num == 7) {
                return false;
            } else {
                flags[num] = true;
                n /= 10;
            }
        }
        if (flags[2] || flags[5] || flags[6] || flags[9])
            return true;
        return false;
    }

}
