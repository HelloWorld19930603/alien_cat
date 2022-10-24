package com.aliencat.springboot.elasticsearch;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import com.aliencat.springboot.elasticsearch.solr.SearchSolr;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chengcheng
 * @Date 2022-07-04
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
@Slf4j
public class SearchSolrToEsTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    ElasticsearchIndexService elasticsearchIndexService;

    @Test
    public void testCount() {
        System.out.println(SearchSolr.queryJointest());
    }

    @Test
    public void testContactBatchUpdate() throws IOException {
        long start = System.currentTimeMillis();
        long total = 0L;
        SearchSolr.queryJointestByCursor();
        String index = "search4contact";
        String cursorMark;
        String nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        do {
            cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursorAsc(SearchSolr.getContactClient(), cursorMark);
            nextCursor = queryResponse.getNextCursorMark();
            SolrDocumentList results = queryResponse.getResults();
            total += results.size();

            int maxSize = results.size() / 10000;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .filter(b -> !b.isEmpty())
                    .forEach(list -> {
                        ContactRunable runableTest = new ContactRunable(index, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!cursorMark.equals(nextCursor));

        System.out.println("处理" + total + "条数据总耗时：" + (System.currentTimeMillis() - start) / 1000 + "s ");
    }

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 60L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(40));

    @Test
    public void testMessageBatchUpdate() {
        long start = System.currentTimeMillis();
        long total = 0L;
        SearchSolr.queryJointestByCursor();
        String index = "search4message";
        String cursorMark;
        String nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        do {
            cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursor(SearchSolr.getMessageClient(), cursorMark);
            nextCursor = queryResponse.getNextCursorMark();
            SolrDocumentList results = queryResponse.getResults();
            int maxSize = results.size() / 10000;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .filter(b -> !b.isEmpty())
                    .forEach(list -> {
                        MessageRunable runableTest = new MessageRunable(index, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!cursorMark.equals(nextCursor));
        System.out.println("处理" + total + "条数据总耗时：" + (System.currentTimeMillis() - start) / 1000 + "s ");
    }


    class MessageRunable implements Runnable {

        List<SolrDocument> results;
        String index;

        MessageRunable(String index, List<SolrDocument> results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            bulkPutMessageIndex(index, results);
        }
    }

    class ContactRunable implements Runnable {

        List<SolrDocument> results;
        String index;

        ContactRunable(String index, List<SolrDocument> results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            bulkPutContactIndex(index, results);
        }
    }

    long spendTime = 0L;
    long total = 0L;

    public void bulkPutContactIndex(String index, List<SolrDocument> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int size = list.size();
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            String account = map.get("contact_account").toString();
            String contact_type = map.get("contact_type").toString();
            if("2".equals(contact_type) || "1".equals(contact_type)){
                System.out.println("contact_type" + contact_type);
            }
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

    public void bulkPutMessageIndex(String index, List<SolrDocument> list) {
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


    @Test
    public void deleteIndex() throws IOException {
        //1、构建 删除索引请求
        //DeleteIndexRequest request = new DeleteIndexRequest("search4message");
        DeleteIndexRequest request = new DeleteIndexRequest("tg_contact");
        //2、客户段执行删除的请求
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功：" + response.isAcknowledged());
    }

    @Test
    public void testCreateMessageIndex() throws IOException {
        String index = "search4message";
        String mapping = IndexConstant.SEARCH4MESSAGE_MAPPING;
        createContactIndex(index, mapping);
    }


    @Test
    public void testCreateContactIndex() throws IOException {
        String index = "search4contact";
        String mapping = IndexConstant.SEARCH4CONTACT_MAPPING;
        createContactIndex(index, mapping);
    }

    public void createContactIndex(String index, String mapping) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(mapping, XContentType.JSON);
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards",6).build());
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.isAcknowledged());
    }


    @Test
    public void queryTotal() throws IOException {
        SearchResponse searchResponse = elasticsearchIndexService.queryMessageByTime(1640966400L, 1654012800);
        System.out.println(searchResponse.getHits().getTotalHits().value);

    }
}
