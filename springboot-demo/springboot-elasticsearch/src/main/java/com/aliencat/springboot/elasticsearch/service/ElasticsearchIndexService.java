package com.aliencat.springboot.elasticsearch.service;

import java.io.IOException;

/**
 * @Author chengcheng
 * @Date 2022-07-11
 **/
public interface ElasticsearchIndexService {

    int getSolrSpendTime();

    String getMessageProcess();

    String getContactProcess();

    boolean createIndex(String index, String mapping) throws IOException;

    boolean deleteIndex(String index, String mapping) throws IOException;

    void messageBatchUpdate(String cursorMark);

    void messageBatchUpdate2(String cursorMark);

    void contactBatchUpdate(String cursorMark);

    void setMessageSwitch1(boolean flag);

    void setMessageSwitch2(boolean flag);

    void setPause(boolean flag);
}
