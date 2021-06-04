package com.aliencat.javabase.bit;

public class PlusI {



    public void iPlusPlus(){
        int i = 0;
        int j = i++;
    }

    public int iPlusPlus2(){
        int i = 0;
        return i++;
    }

    public void iPlusPlus3(){
        int i = 6;
        int a = 3;
        a = i;
        int b = 4;
        b = i;
        i = i+2;
    }

    public void plusPlusI(){
        int i = 0;
        int j = ++i;
    }

    public static void main(String[] args) {
        int i = 0;
        System.out.println(i++ + ++i);
    }
}
