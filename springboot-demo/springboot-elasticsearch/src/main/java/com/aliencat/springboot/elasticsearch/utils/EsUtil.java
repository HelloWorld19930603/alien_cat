package com.aliencat.springboot.elasticsearch.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.security.MessageDigest;

/**
 * @Author chengcheng
 * @Date 2022-06-27
 **/
public class EsUtil {

    private static final RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
            RestClient.builder(
                    //若有多个，可以传一个数组
                    new HttpHost("127.0.0.1", 9200, "http")));

    public static RestHighLevelClient getRestHighLevelClient(){
        return restHighLevelClient;
    }


    public final static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
