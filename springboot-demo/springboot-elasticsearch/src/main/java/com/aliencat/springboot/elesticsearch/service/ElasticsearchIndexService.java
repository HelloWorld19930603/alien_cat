package com.aliencat.springboot.elesticsearch.service;

import java.io.IOException;

/**
 * @Author chengcheng
 * @Date 2022-07-11
 **/
public interface ElasticsearchIndexService {
    public  boolean createIndex(String index, String mapping) throws IOException;

    public  boolean deleteIndex(String index, String mapping) throws IOException;

    void messageBatchUpdate();

    void contactBatchUpdate();
}
