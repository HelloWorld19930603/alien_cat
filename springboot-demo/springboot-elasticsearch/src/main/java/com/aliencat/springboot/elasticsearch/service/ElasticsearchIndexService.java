package com.aliencat.springboot.elasticsearch.service;

import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.Date;

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

    void messageBatchUpdate3(String cursorMark);

    void contactBatchUpdate(String cursorMark);

    void contactTransferEs(String fromIndex,String toIndex);

    SearchResponse queryMessageByTime(long start, long end) throws IOException;

    SearchResponse updateMessageByTime(long start, long end) throws IOException;

    void setMessageSwitch1(boolean flag);

    void setMessageSwitch2(boolean flag);

    void setPause(boolean flag);

    void messageBatchUpdateByDate(Date start, Date end) throws IOException, InterruptedException;

}
