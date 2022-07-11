package com.aliencat.springboot.elesticsearch.service.impl;

import com.aliencat.springboot.elesticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elesticsearch.service.ElasticsearchIndexService;
import com.aliencat.springboot.elesticsearch.solr.SearchSolr;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author chengcheng
 * @Date 2022-07-11
 **/
@Service
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {

    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(100));


    @Override
    public boolean createIndex(String index, String mapping) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.isAcknowledged());
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public boolean deleteIndex(String index, String mapping) throws IOException {
        String indexName = IndexConstant.SEARCH4MESSAGE;
        //1、构建 删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        //2、客户段执行删除的请求
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功：" + response.isAcknowledged());
        return response.isAcknowledged();
    }

    @Override
    public void messageBatchUpdate() {
        long start = System.currentTimeMillis();
        long total = 0L;
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4MESSAGE;
        String cursorMark;
        String nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        do {
            cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursor(index, cursorMark);
            nextCursor = queryResponse.getNextCursorMark();
            SolrDocumentList results = queryResponse.getResults();
            total += results.size();
            MessageRunable runableTest = new MessageRunable(index, results);
            threadPoolExecutor.execute(runableTest);
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!cursorMark.equals(nextCursor));
        System.out.println("处理" + total + "条数据总耗时：" + (System.currentTimeMillis() - start) / 1000 + "s ");
    }


    @Override
    public void contactBatchUpdate()  {
        long start = System.currentTimeMillis();
        long total = 0L;
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4CONTACT;
        String cursorMark;
        String nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        do {
            cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursor(index, cursorMark);
            nextCursor = queryResponse.getNextCursorMark();
            SolrDocumentList results = queryResponse.getResults();
            total += results.size();
            ContactRunable runableTest = new ContactRunable(index, results);
            threadPoolExecutor.execute(runableTest);
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!cursorMark.equals(nextCursor));
        System.out.println("处理" + total + "条数据总耗时：" + (System.currentTimeMillis() - start) / 1000 + "s ");
    }

    class MessageRunable implements Runnable {

        SolrDocumentList results;
        String index;

        MessageRunable(String index, SolrDocumentList results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            bulkPutMessageIndex(index, results);
        }
    }


    long spendTime = 0L;
    long total = 0L;

    public void bulkPutMessageIndex(String index, SolrDocumentList list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int size = list.size();
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            String account = map.get("sender_account").toString();
            if (SearchSolr.jointestMap.containsKey(account)) {
                SolrDocument jointest = SearchSolr.jointestMap.get(account);
                jointest.remove("id");
                jointest.remove("account_number");
                map.putAll(jointest);
            }
            map.remove("_version_");
            request.add(new IndexRequest(index, "_doc", map.get("id").toString()).source(map, XContentType.JSON));
        }
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("发生异常，2s后重试 ：" + e);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            bulkPutMessageIndex(index, list);
        }
        spendTime = spendTime + bulk.getTook().getMillis();
        total = total + size;
        System.out.println(bulk.getTook() + " ---- " + spendTime / 1000L + " ---- " + total);
    }


    public void bulkPutContactIndex(String index, SolrDocumentList list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int size = list.size();
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            String account = map.get("contact_account").toString();
            if (SearchSolr.jointestMap.containsKey(account)) {
                SolrDocument jointest = SearchSolr.jointestMap.get(account);
                jointest.remove("id");
                jointest.remove("account_number");
                map.putAll(jointest);
            }
            map.remove("_version_");
            request.add(new IndexRequest(index, "_doc", map.get("id").toString()).source(map, XContentType.JSON));
        }
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("发生异常，2s后重试 ：" + e);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            bulkPutContactIndex(index, list);
        }
        spendTime = spendTime + bulk.getTook().getMillis();
        total = total + size;
        System.out.println(bulk.getTook() + " ---- " + spendTime / 1000L + " ---- " + total);
    }



    class ContactRunable implements Runnable {

        SolrDocumentList results;
        String index;

        ContactRunable(String index, SolrDocumentList results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            bulkPutContactIndex(index, results);
        }
    }
}
