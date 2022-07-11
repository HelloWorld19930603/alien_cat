package com.aliencat.springboot.elasticsearch;

import com.aliencat.springboot.elesticsearch.ElasticsearchApplication;
import com.aliencat.springboot.elesticsearch.solr.SearchSolr;
import lombok.extern.slf4j.Slf4j;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60L, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(100));

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

    long spendTime = 0L;
    long total = 0L;

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


    @Test
    public void deleteIndex() throws IOException {
        //1、构建 删除索引请求
        //DeleteIndexRequest request = new DeleteIndexRequest("search4message");
        DeleteIndexRequest request = new DeleteIndexRequest("search4contact");
        //2、客户段执行删除的请求
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功：" + response.isAcknowledged());
    }

    @Test
    public void testCreateMessageIndex() throws IOException {
        String index = "search4message";

        String mapping = "{\n" +
                "    \"properties\" : {\n" +
                "        \"business_action_id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"contact_account\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"contact_account_id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"gmt_create\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"is_group\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"m_l_charCount\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"m_l_informationType\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"m_l_senderType\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"message_content\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\",\n" +
                "          \"fields\" : {\n" +
                "            \"keyword\" : {\n" +
                "              \"type\" : \"keyword\",\n" +
                "              \"ignore_above\" : 256\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"message_time\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"message_type\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"misc_long_orig\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"recipient_account\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"recipient_account_id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"sender_account\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"sender_account_id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"account_number\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"application_id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"area_code\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"city\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"country\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"operator\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"province\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "  }" +
                "    }";
        createContactIndex(index, mapping);
    }


    @Test
    public void testCreateContactIndex() throws IOException {
        String index = "search4contact";
        String mapping = "{\n" +
                "      \"properties\" : {\n" +
                "        \"business_action_id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"contact_account\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"contact_account_id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"contact_id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"contact_type\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"ex_k_age\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_bioInfo\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_groupType\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_icon\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_lastSeenTime\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_memberNum\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_name\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\",\n" +
                "          \"fields\" : {\n" +
                "            \"keyword\" : {\n" +
                "              \"type\" : \"keyword\",\n" +
                "              \"ignore_above\" : 256\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"ex_k_nickName\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\",\n" +
                "          \"fields\" : {\n" +
                "            \"keyword\" : {\n" +
                "              \"type\" : \"keyword\",\n" +
                "              \"ignore_above\" : 256\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"ex_k_offlineTime\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_phone\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_remark\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\",\n" +
                "          \"fields\" : {\n" +
                "            \"keyword\" : {\n" +
                "              \"type\" : \"keyword\",\n" +
                "              \"ignore_above\" : 256\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"ex_k_sex\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_tmp\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_updateTime\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"ex_k_userName\" : {\n" +
                "          \"type\" : \"text\",\n" +
                "          \"analyzer\": \"ik_max_word\",\n" +
                "          \"fields\" : {\n" +
                "            \"keyword\" : {\n" +
                "              \"type\" : \"keyword\",\n" +
                "              \"ignore_above\" : 256\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"gmt_create\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"unique_account\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"unique_account_id\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"account_number\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"application_id\" : {\n" +
                "          \"type\" : \"long\"\n" +
                "        },\n" +
                "        \"area_code\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"city\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"country\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"operator\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"province\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }";
        createContactIndex(index, mapping);
    }

    public void createContactIndex(String index, String mapping) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.isAcknowledged());
    }
}
