package com.aliencat.javabase.bit;

import java.lang.reflect.Method;
import java.util.Random;

public class Arithmetic {

    public static int add(Integer a, Integer b) {
        if (a < 0 && b < 0) {
            a = addOne(a ^ Integer.MAX_VALUE );
            b = addOne(b ^ Integer.MAX_VALUE );
        }else if(a < 0 && b > 0){
            a = addOne(a ^ Integer.MAX_VALUE );
        }else if(a > 0 && b < 0){
            b = addOne(b ^ Integer.MAX_VALUE );
        }
        return doAdd(a,b);
    }

    public static int doAdd(int a, int b){
        int i = 1;
        int result = 0;
        boolean flag = false;
        while (i > 0) {
            int ai = a & i;
            int bi = b & i;
            if (ai > 0 && bi > 0) {
                if (flag) {
                    result ^= i;
                }
                flag = true;
            } else if (ai > 0 || bi > 0) {
                if (!flag) {
                    result ^= i;
                }
            } else {
                if (flag) {
                    result ^= i;
                }
                flag = false;
            }
            i <<= 1;
        }
        if (a < 0 && b >= 0) {
            //对a取绝对值比较大小
            a = addOne(a ^ i ^ Integer.MAX_VALUE);
            if (a > b) {
                return addOne((result) ^ i ^ Integer.MAX_VALUE);
            }else{
                return result ^ i;
            }
        } else if (b < 0 && a >= 0) {
            //对b取绝对值比较大小
            b = b ^ i ^ Integer.MAX_VALUE;
            if (a < b) {
                return addOne((result) ^ i ^ Integer.MAX_VALUE) ;
            }else{
                return result ^ i;
            }
        } else if (a < 0 && b < 0) {
            return addOne((result) ^ i ^ Integer.MAX_VALUE);
        }
        return result;
    }

    public static int addOne(int a){
        int one = 1;
        if(a == 0){
            return one;
        }
        while((a & one) != 0){
            a ^= one;
            one <<= 1;
        }
        while((a & one) == 0){
            a ^= one;
            one <<= 1;
        }
        a ^= one;
        //去除符号位


        return a;
    }

    public static void check(String methodName) throws Exception {
        Class aclass = Arithmetic.class;
        Method method = aclass.getMethod(methodName, Integer.class, Integer.class);
        Random random = new Random();
        while (true) {
            Integer a = random.nextInt(2000) - 1000;
            Integer b = random.nextInt(2000) - 1000;
            int result = (int) method.invoke(aclass, a, b);
            if (result != (a + b)) {
                //若是实现的算法计算结果不对，直接抛出错误
                throw new Exception("方法计算错误！！！参与计算的值：a=" + a + ",b=" + b +
                        ";计算结果为："+result+",正确结果为："+(a+b));
            }
            System.out.println(result);
        }
    }




    public static void main(String[] args) throws Exception {
        //check("add");
         System.out.println(add(-520, 20));
    }

}
