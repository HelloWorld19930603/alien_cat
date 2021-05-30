package com.aliencat.javabase.bit;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Random;

public class Arithmetic {

    public static int add(Integer a, Integer b) {
        int i = 1;
        int result = 0;
        boolean flag = false;  //进位的标记
        do {//第几次循环就对应第几个bit位
            int ai = a & i;
            int bi = b & i;
            if(ai > 0 && bi > 0){ //a和b在同一bit位都是1
                if(flag){
                    result ^= i;
                }
                flag = true; // true代表了2进1
            }else if((ai ^ bi) != 0){  //a和b在同一bit位有1个1和1个0的情况
               if(flag){
                   flag = true;
               }else{
                   //低一位没有进1的情况下,result在该bit位置为1
                   //0 ^ 1 = 1
                   result ^= i;
               }
            }else if(flag){//a和b在同一bit位都是0的情况,有进位则该bit位置为1
               result ^= i;
               flag = false;
            }
            i <<= 1;  // i左移一位
        }while (i != 0);
        return result;
    }

    public static int minus(Integer a,Integer b){
        if(b == 0){
            return a;
        }else{
            // b为负数情况下：(b ^ 0x7FFFFFFF) + 1 ^ 0x80000000 等于 b的绝对值
            // 即 a - b = a + |b|
            // b为正数情况下我们需要把b转为负数
            // b mod (0x1 0000 0000) = (b ^ 0xFFFF FFFF)
            // a - b 相当于 a + (-b)
            // (b ^ 0xFFFFFFFF) + 1 = (b ^ 0x80000000 ^ 0x7FFFFFFF) + 1 等于把b置为负数求其反码，最后加一就是补码
            return add(a, add(b ^ 0xFFFFFFFF, 1) );
        }
    }

    public static int multiply(Integer a , Integer b){
        //return multiplyV1(a,b);
        return multiplyV2(a,b);
    }

    //乘法v1 类似穷举法的思想进行累加
    public static int multiplyV1(Integer a,Integer b){
        int result = 0;
        boolean flag = false;
        if(b < 0){
            b = abs(b);   //取b的绝对值
            if(a > 0){  // ab异号情况下
                flag = true;
            }else{
                a = abs(a);   //取a的绝对值
            }
        }
        while( b > 0){
            result = add(result,a);; // b个a相加
            b = minus(b,1);
        }
        if(flag){ //ab异号则result必为负数
            result = add((result ^  0xFFFFFFFF) , 1);      //对a的符号位取反求再其补码
        }
        return result;
    }

    //乘法v2  优化版
    public static int multiplyV2(Integer a,Integer b){
        int result = 0;
        boolean flag = false;
        if(b < 0){
            b = abs(b);   //取b的绝对值
            if(a > 0){  // ab异号情况下
                flag = true;
            }else{
                a = abs(a);   //取a的绝对值
            }
        }
        //最多循环七次
        while (b > 0){
            if((b & 1) == 0){
                a <<= 1;
                b >>= 1;
            }else{
                result = add(result,a);
                b ^= 0x00000001;
            }
        }
        if(flag){ //ab异号则result必为负数
            result = add((result ^  0xFFFFFFFF) , 1);      //对a的符号位取反求再其补码
        }
        return result;
    }


    public static int divide(Integer a,Integer b){
        if(b == 0){
            throw new IllegalArgumentException("除数不能为0");
        }
        int result = 0;
        boolean flag = false;
        if((a ^ b) < 0){
            flag = true;
        }
        a = abs(a);
        b = abs(b);
        if(a < b){
            return 0;
        }
        a = minus(a,b);
        while(a >= 0){
            a = minus(a,b);
            result = add(result,1);
        }
        if(flag){ //ab异号则result必为负数
            result = add((result ^  0xFFFFFFFF) , 1);      //对a的符号位取反求再其补码
        }
        return result;
    }

    //求a的绝对值
    public static int abs(Integer a){
        if(a >= 0){
            return a;
        }
        //a为负值情况下取a的补码再对其符号位取反就是其绝对值
        //等价于 add(b ^ 0xFFFFFFFF, 1)
        return add((a ^ 0x7FFFFFFF) , 1) ^ 0x80000000;
    }

    public static void check(String methodName) throws Exception {
        Class aclass = Arithmetic.class;
        Method method = aclass.getMethod(methodName, Integer.class, Integer.class);
        Random random = new Random();
        for(int i = 0;i < 1000;i++) {
            //生成[-1000,1000]的随机数
            Integer a = random.nextInt(2000) - 1000;
            Integer b = random.nextInt(2000) - 1000;
            int result = (int) method.invoke(aclass, a, b);
            switch (methodName){
                case "add" : if (result != (a + b)) {
                    //若是实现的算法计算结果不对，直接抛出错误
                    throw new Exception(methodName+"方法计算错误！！！参与计算的值：a=" + a + ",b=" + b +
                            ";计算结果为："+result+",正确结果为："+(a+b));
                };break;
                case "minus" : if (result != (a - b)) {
                    //若是实现的算法计算结果不对，直接抛出错误
                    throw new Exception(methodName+"方法计算错误！！！参与计算的值：a=" + a + ",b=" + b +
                            ";计算结果为："+result+",正确结果为："+(a-b));
                };break;
                case "multiply" : if (result != (a * b)) {
                    //若是实现的算法计算结果不对，直接抛出错误
                    throw new Exception(methodName+"方法计算错误！！！参与计算的值：a=" + a + ",b=" + b +
                            ";计算结果为："+result+",正确结果为："+(a*b));
                };break;
                case "divide" : if (result != (a / b)) {
                    //若是实现的算法计算结果不对，直接抛出错误
                    throw new Exception(methodName+"方法计算错误！！！参与计算的值：a=" + a + ",b=" + b +
                            ";计算结果为："+result+",正确结果为："+(a/b));
                };break;
            }

            System.out.println("a="+a+",b="+b+",result="+result);
        }
    }

    @Test
    public void test1() throws Exception {
        check("add");
    }

    @Test
    public void test2() throws Exception {
        check("minus");
    }

    @Test
    public void test3() throws Exception {
        check("multiply");
    }

    @Test
    public void test4() throws Exception {
        check("divide");
    }

    public static void main(String[] args) throws Exception {
        check("add");
        check("minus");
        check("multiply");
        check("divide");

    }

}
