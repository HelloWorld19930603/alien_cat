package com.aliencat.javabase.api.regex;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexDemo {

    /**
     * Unicode 编码并不只是为某个字符简单定义了一个编码，而且还将其进行了归类。
     * \pP 其中的小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀。
     * 大写 P 表示 Unicode 字符集七个字符属性之一：标点字符。
     * <p>
     * 其他六个是
     * L：字母；
     * M：标记符号（一般不会单独出现）；
     * Z：分隔符（比如空格、换行等）；
     * S：符号（比如数学符号、货币符号等）；
     * N：数字（比如阿拉伯数字、罗马数字等）；
     * C：其他字符
     */
    public static void main(String[] args) {
        String testStr = "你好，{}【】{}%……&*（ 这是对标点符号\n 进行正则匹配的！@#￥%，。-———_=+测试。";
        Pattern p = Pattern.compile("[\\p{P} | \\p{Z} | \\p{S} | \\p{C}]");
        testStr = Arrays.stream(p.split(testStr)).collect(Collectors.joining());
        System.out.println(testStr);
    }

}
