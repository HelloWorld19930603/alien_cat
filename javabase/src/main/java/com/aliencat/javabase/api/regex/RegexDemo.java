package com.aliencat.javabase.api.regex;

import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
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
    @Test
    public void test1() {
        String testStr = "你好，{}【】{}%……&*（ 这是对标点符号\n 进行正则匹配的！@#￥%，。-———_=+测试。";
        Pattern p = Pattern.compile("[\\p{P} | \\p{Z} | \\p{S} | \\p{C}]");
        testStr = Arrays.stream(p.split(testStr)).collect(Collectors.joining());
        System.out.println(testStr);
    }

    /**
     * 常用字符串匹配正则表达式：
     * 汉字：^[\u4e00-\u9fa5]{0,}$
     * 英文和数字：^[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}$
     * 长度为3-20的所有字符：^.{3,20}$
     * 由26个英文字母组成的字符串：^[A-Za-z]+$
     * 由26个大写英文字母组成的字符串：^[A-Z]+$
     * 由26个小写英文字母组成的字符串：^[a-z]+$
     * 由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$
     * 由数字、26个英文字母或者下划线组成的字符串：^\w+$ 或 ^\w{3,20}
     * 中文、英文、数字包括下划线：^[\u4E00-\u9FA5A-Za-z0-9_]+$
     * 中文、英文、数字但不包括下划线等符号：^[\u4E00-\u9FA5A-Za-z0-9]+$ 或 ^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$
     * 可以输入含有^%&',;=?$\"等字符：[^%&',;=?$\x22]+
     * 禁止输入含有~的字符[^~\x22]+
     */
    @Test
    public void test2() {
        String line = "AGBtest125abfrAGtest#Q%#@tes;";
        String rule = "(test)[a-zA-Z0-9_<>.]+";//正则表达式
        Pattern p = Pattern.compile(rule);//获取正则表达式中的分组，每一组小括号为一组
        Matcher m = p.matcher(line);//进行匹配
        if (m.find()) {//判断正则表达式是否匹配到
            for (int i = 0; i < m.groupCount(); i++) {
                System.out.println(m.group(i));
            }
        }
    }


    /**
     * Matcher.matches()/ Matcher.lookingAt()/ Matcher.find(),三个方法均返回boolean类型
     * matches()：尝试对整个目标字符展开匹配检测，也就是只有整个目标字符串完全匹配时才返回真值。
     * lookingAt()：对前面的字符串进行匹配，只有匹配到的字符串在最前面才会返回true。
     * find()：对字符串进行匹配，匹配到的字符串可以在任何位置。
     */
    @Test
    public void test3() {
        String info = "xyz123abc";
        Pattern p1 = Pattern.compile("\\d+");
        Pattern p2 = Pattern.compile("\\w+\\d+\\w+");
        Matcher matcher = p1.matcher(info);
        Matcher matcher1 = p2.matcher(info);
        //matches()对整个字符串进行匹配,只有整个字符串都匹配了才返回true 。
        System.out.println(matcher.matches());//false
        System.out.println(matcher1.matches());//true

        Pattern p = Pattern.compile("a+");
        Matcher m1 = p.matcher("aabbaa");
        System.out.println("this is the first :");
        System.out.println(m1.find() + " : " + m1.lookingAt() + " : " + m1.matches());
        Matcher m2 = p.matcher("bbaa");
        System.out.println("this is the second :");
        System.out.println(m2.find() + " : " + m2.lookingAt() + " : " + m2.matches());
        Matcher m3 = p.matcher("bb");
        System.out.println("this is the last :");
        System.out.println(m3.find() + " : " + m3.lookingAt() + " : " + m3.matches());
    }

}
