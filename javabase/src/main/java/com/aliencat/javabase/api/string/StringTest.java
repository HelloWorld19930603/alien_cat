package com.aliencat.javabase.api.string;

import org.junit.Test;

public class StringTest {

    @Test
    public void testReplace() {
        String str = "        git add 添加文件到仓库\n" +
                "        git status 查看仓库当前的状态，显示有变更的文件。\n" +
                "        git diff 比较文件的不同，即暂存区和工作区的差异。\n" +
                "        git commit 提交暂存区到本地仓库。\n" +
                "        git reset 回退版本。\n" +
                "        git rm 删除工作区文件。\n" +
                "        git rm --cache 删除暂存区文件。\n" +
                "        git mv 移动或重命名工作区文件。\n" +
                "        git log 查看历史提交记录\n" +
                "        git blame 以列表形式查看指定文件的历史修改记录\n" +
                "        git remote 远程仓库操作\n" +
                "        git fetch 从远程获取代码库\n" +
                "        git pull 下载远程代码并合并\n" +
                "        git push 上传远程代码并合并";

        str = str.replaceAll("        ", " ");
        str = str.replaceAll(" ", "|");
        str = str.replace("|git|", "|git ");
        String[] strings = str.split("\n");
        for (String s : strings) {
            System.out.println("" + s + "|");
        }


    }

    @Test
    public void testEqualsIgnoreCase() {
        //用户输入用户名
        String userName = "USERNAME";
        //数据库用户名"admin"
        if (userName.equalsIgnoreCase("userName")) {
            System.out.println("验证成功");
        }

    }

    /**
     * startsWith()和endsWith()
     * 检查一个字符串是否以指定字符串开头或者结尾
     */
    @Test
    public void testStartsWith() {
        String num1 = "22045612";
        String num2 = "21304578";
        System.out.println(num1.startsWith("22"));
        System.out.println(num1.endsWith("78"));
        System.out.println(num2.startsWith("22"));
        System.out.println(num2.endsWith("78"));
    }

    /**
     * substring获取字符串
     * substring方法用于返回一个字符串的子字符串
     * substring常用重载方法定义如下
     * 1).String substring(int beginIndex,int endIndex)**返回字符串中从下标beginIndex(包括)开始到endIndex(不包括)结束的子字符串
     * 2).String sunstring(int beginIndex)返回字符串从下标beginIndex开始到字符串结尾的子字符串
     */
    @Test
    public void testSubstring() {
        //身份证编号
        String carNo = "423545641545621248";
        /**
         * 1.获取地区码
         */
        String areaCode = carNo.substring(0, 6);
        System.out.println("地区码: " + areaCode);
        /**
         * 2.输入用户的生日
         * 使用三种方式表示生日,用到重要的两个方法
         * String的subString()方法
         * 基本数据类型包装类的valueOf()方法,例如Integer.vlueOf(String str)
         */
        //2.生日
        //2.1.第一种生日的表示方式(20201111)
        String bir1 = carNo.substring(6, 14);
        System.out.println("表示生日的第一种方式: " + bir1);
        //2.2.第二种生日的表示方式(2020年11月11日)
        String year2 = carNo.substring(6, 10);
        String month2 = carNo.substring(10, 12);
        String day2 = carNo.substring(12, 14);
        System.out.println("第二种表示生日的方式  :" + year2 + "年  " + month2 + "月  " + day2 + "日");
        /*
        2.3第三种表示生日的方式
           说明:2.2表示生日存在一个问题
             如果用的的生日是20200101   ---->2020年01月01日
             这样不好,我们预期看到的是
             2020年1月1日   , 把数字前面的0去除
         */
        String year3 = carNo.substring(6, 10);
        String month3 = carNo.substring(10, 12);
        //先给字符串转为int,数字01前面的0就没有了,使用Integer.valueOf(String str)方法
        //然后先给1转为字符窜,使用String.valueOf(Integer i)方法
        month3 = String.valueOf(Integer.valueOf(month3));
        String day3 = carNo.substring(12, 14);
        //处理方法同上
        day3 = String.valueOf(Integer.valueOf(day3));
        System.out.println("第三种方式表示生日 :" + year3 + "年" + month3 + "月" + day3 + "日 ");

        /**
         * 3.判断性别
         * 判断性别的要求:
         * 看身份证号倒数第二位数字的奇偶性,奇数男性,偶数女性
         */
        //过去倒数第二位的数字
        String sexString = carNo.substring(carNo.length() - 2, carNo.length() - 1);
        //转为数字判断奇偶性
        Integer sex = Integer.valueOf(sexString);
        if (sex % 2 == 0) {
            System.out.println("性别为:女性");
            return;//说明:此处一定要return,否则会出行逻辑错误
        }
        System.out.println("性别为:男性");

    }


    @Test
    public void testSplitBylimit() {
        String str = "abc,def,ghi,gkl";
        String[] split = str.split(",", 2);
        for (String s : split) {
            System.out.println(s);
        }
    }

    @Test
    public void testSplit() {
        String str = "abc,def,ghi,gkl";
        String[] split = str.split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }

    /**
     * java为了提高性能,静态字符串(字面量/常量/常量连接的结果)在常量池中创建,
     * 尽量使用同一个对象,重用静态字符串;
     * 对于重复出现的字符串直接量,JVM会首先在常量池中查找,如果存在即返回该对象
     */
    @Test
    public void testConstantPool() {
        String str1 = "Hello";
        //不会创建新的String对象,而是使用常量池中已有的"Hello".
        String str2 = "Hello";
        System.out.println(str1 == str2); // 输出true
        //使用new关键字会创建新的String对象
        String str3 = new String("Hello");
        System.out.println(str1 == str3); //输出false
    }


    /**
     * indexOf方法用于实现在字符串中检索另外一个字符串
     * String提供几个重载的indexOf方法
     * 1). **int indexOf(String str)在字符串中检索str,返回其第一次出现的位置,如果找不到则返回-1
     * 2). int indexOf(String str,int fromIndex)**从字符串的fromIndex位置开始检索
     * String还定义有lastIndexOf方法
     * 1). **int lastIndexOf(String str,int form)**str在字符串中多次出现时,将返回最后一个出现的位置
     */
    @Test
    public void testIndexOf() {
        String str = "I can because i think i can";
        int index = str.indexOf(" can");
        System.out.println(index);      // 2
        index = str.lastIndexOf(" can");
        System.out.println(index);     // 24
        index = str.indexOf("can", 6);
        System.out.println(index);    // 24
        index = str.indexOf(" Can");
        System.out.println(index);   // -1
    }


    /**
     * trim()
     * 去除首尾空格
     */
    @Test
    public void testTrim() {
        String userName = "      user name         ";
        userName = userName.trim();
        System.out.println(userName.length()); // 8
        System.out.println(userName);  // user name
    }


    /**
     * toCharArray()
     * 返回值为char数组类型。将字符串变成一个字符数组
     */
    @Test
    public void testToCharArray() {
        String str = "hello world";
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            System.out.println("转为数组输出:" + c[i]);
        }
    }

    /**
     * toUpperCase() 字符串转大写
     * toLowerCase() 字符串转小写
     * 字符串大小写的转换
     */
    @Test
    public void testToUpperCase() {
        String str = "Hello World!";
        System.out.println("将字符串转大写为：" + str.toUpperCase());
        System.out.println("将字符串转换成小写为：" + str.toUpperCase().toLowerCase());
    }
}
