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

    boolean createIndex(String index, String mapping, int shards) throws IOException;

    boolean deleteIndex(String index, String mapping) throws IOException;

    void messageBatchUpdate(String cursorMark);

    void messageBatchUpdateAsc(Date start, Date end);

    void messageBatchUpdate3(String cursorMark);

    void contactBatchUpdate(String cursorMark);

    void contactTransferEs(String fromIndex, String toIndex);

    SearchResponse queryMessageByTime(long start, long end) throws IOException;

    SearchResponse updateMessageByTime(long start, long end) throws IOException;

    void setMessageSwitch1(boolean flag);

    void setMessageSwitch2(boolean flag);

    void setPause(boolean flag);

    void messageBatchUpdateByDate(Date start, Date end) throws IOException, InterruptedException;

    void messageBatchUpdateByDateFromMysql(Date startDate, Date endDate) throws InterruptedException;


    void messageBatchUpdateByContactFromMysql(String contact);
}
