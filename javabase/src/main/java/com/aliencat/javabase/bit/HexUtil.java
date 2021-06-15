package com.aliencat.javabase.bit;


/**
 * Created by cc
 */
public class HexUtil {

    private HexUtil() {
    }


    //int类型转换为16进制字符串
    public static String intToHexString(int i) {
        String result = Integer.toHexString(i);
        return result.length() % 2 == 1 ? "0".concat(result) : result;
    }

    //byte类型转换为16进制字符串
    public static String byteToHexString(byte... b) {
        int v;
        if (b.length == 1) {
            v = b[0] & 0xFF;
            return intToHexString(v).toUpperCase();
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                v = b[i] & 0xFF;
                sb.append(intToHexString(v).toUpperCase());
            }
            return sb.toString();
        }

    }

    //byte类型转换为16进制字符串
    public static String byteToHexString2(byte... b) {
        int v;
        if (b.length == 1) {
            v = b[0] & 0xFF;
            return intToHexString(v).toUpperCase();
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                v = b[i] & 0xFF;
                sb.append(intToHexString(v).toUpperCase() + " ");
            }
            return sb.toString();
        }

    }

    //16进制数转化为int类型
    public static int hexToInt(byte... b) throws Exception {
        if (b.length > 4) {
            throw new Exception("传入字节数超过int字节数");
        }
        int result = 0;
        for (int i = b.length - 1, j = 0; i >= 0; i--, j++) {
            result = result | ((b[j] & 0xff) << (8 * i));
        }
        return result;
    }

    //16进制数转化为float类型
    public static float hexToFloat(byte... b) throws Exception {
        return (float) hexToInt(b);
    }

    //int类型转化为二进制
    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[4];
        for (int i = 3, j = 0; i >= 0; i--, j++) {
            bytes[j] = (byte) ((num >> i * 8) & 0xff);
        }
        return bytes;
    }

    //short类型转化为二进制
    public static byte[] shortToBytes(short num) {
        byte[] bytes = new byte[2];
        for (int i = 2, j = 0; i >= 0; i--, j++) {
            bytes[j] = (byte) ((num >> i * 8) & 0xff);
        }
        return bytes;
    }

    //byte类型转化为二进制
    public static byte[] byteToBits(byte b) {
        byte[] bits = new byte[8];
        for (int i = 7; i >= 0; i--) {
            if (b % 2 == 0) {
                bits[i] = 0;
            } else {
                bits[i] = 1;
            }
            b = (byte) (b >> 1);
        }
        return bits;
    }

    //多段16进制字符串转化为16进制数
    public static byte[] stringToHexes(String hexStr) {
        char[] chars = hexStr.toCharArray();
        byte[] result = new byte[chars.length / 2];
        int num;
        for (int i = 0, j = 0; i < chars.length; i += 2, j++) {
            num = Integer.parseInt("" + chars[i] + chars[i + 1], 16);
            result[j] = (byte) num;
        }
        return result;
    }

    //单个16进制字符串转化为16进制数
    public static byte stringToHex(String hexStr) {
        int num = Integer.parseInt(hexStr, 16);
        return (byte) num;
    }

}

