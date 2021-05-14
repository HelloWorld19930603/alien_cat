package com.aliencat.javabase.bit;

public class BitUtils {

    public static void printByteToBit(byte b){
        String result = "[ ";
        int pos = 1 << 7;
        int j = 1;
        while (pos != 0){
            if((b & pos) != 0){
                result += "1";
            }else{
                result += "0";
            }
            pos >>>= 1;
            if(j++ % 8 == 0){
                result += " ";
            }
        }
        result += "] ";
        System.out.println(result);
    }

    public static void printIntToBit(int i){
        String result = "[ ";
        int pos = 1 << 31;
        int j = 1;
        while (pos != 0){
            if((i & pos) != 0){
                result += "1";
            }else{
                result += "0";
            }
            pos >>>= 1;
            if(j++ % 8 == 0){
                result += " ";
            }
        }
        result += "] ";
        System.out.println(result);
    }

    //打印int反码
    public static void printInt1Complement(int i){
        printIntToBit(i ^ Integer.MAX_VALUE);
    }

    //打印int补码
    public static void printInt2Complement(int i){
        printIntToBit((i ^ Integer.MAX_VALUE) + 1);
    }

    //打印int反码
    public static void printByte1Complement(byte i){
        printByteToBit((byte)(i ^ Byte.MAX_VALUE));
    }

    //打印int补码
    public static void printByte2Complement(byte i){
        printByteToBit((byte)((i ^ Byte.MAX_VALUE) + 1));
    }

    public static void printInt(int i){
        System.out.println(i);
        System.out.print("原码：");
        printIntToBit(i);
        System.out.print("反码：");
        printInt1Complement(i);
        System.out.print("补码：");
        printInt2Complement(i);
    }

    public static void printByte(byte i){
        System.out.println(i);
        System.out.print("原码：");
        printByteToBit(i);
        System.out.print("反码：");
        printByte1Complement(i);
        System.out.print("补码：");
        printByte2Complement(i);
    }

    public static void main(String[] args) {

        int i = 255;
        System.out.println((byte)i);

        printInt(1);
        printInt(-1);
        printInt(-128);
        printInt(Integer.MAX_VALUE);

        printByte((byte)1);
        printByte((byte)-1);
        printByte(Byte.MAX_VALUE);
        printByte((byte)0);
        printByte((byte)-128);

    }
}
