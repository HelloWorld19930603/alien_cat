package com.aliencat.springboot.elesticsearch.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

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
}
