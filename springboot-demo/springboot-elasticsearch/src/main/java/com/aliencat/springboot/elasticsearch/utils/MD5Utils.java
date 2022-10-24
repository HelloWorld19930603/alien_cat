package com.aliencat.springboot.elasticsearch.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {


    /**
     * 对字符串进行md5加密
     */
    public static String getMD5Str(String strValue) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
            return newstr;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("md5异常");;
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String md5 = getMD5Str("aliencat");
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
