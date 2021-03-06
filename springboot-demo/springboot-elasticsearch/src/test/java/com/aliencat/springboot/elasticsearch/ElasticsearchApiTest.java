package com.aliencat.springboot.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.aliencat.springboot.elasticsearch.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
@Slf4j
public class ElasticsearchApiTest {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * ??????????????????
     */
    @Test
    public void createIndex() throws IOException {
        //1????????? ?????????????????????
        CreateIndexRequest request = new CreateIndexRequest("my_index");//?????????
        //2????????????????????????,????????????
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        //3?????????
        System.out.println("???????????????????????????????????????" + response.index());
    }

    /**
     * ??????????????????
     */
    @Test
    public void getIndex() throws IOException {
        //1????????? ?????????????????????
        GetIndexRequest request = new GetIndexRequest("my_index");
        //2???????????????????????????????????????
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        //3?????????
        System.out.println("????????????????????????" + exists);
    }


    /**
     * ??????????????????
     */
    @Test
    public void deleteIndex() throws IOException {
        //1????????? ??????????????????
        DeleteIndexRequest request = new DeleteIndexRequest("search4contact");
        //2?????????????????????????????????
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //3?????????
        System.out.println("?????????????????????" + response.isAcknowledged());
    }


    /**
     * ????????????
     */
    @Test
    public void createDocument() throws IOException {
        User user = new User().setId(1).setUsername("??????");

        //1???????????????
        IndexRequest request = new IndexRequest("user_index");

        //2???????????????  PUT /user_index/user/_doc/1
        request.id("1");//??????id
        request.timeout(TimeValue.timeValueSeconds(1));//??????????????????

        //3??????????????????????????????,???JSON???????????????
        request.source(JSONObject.toJSONString(user), XContentType.JSON);

        //4????????????????????????,??????????????????
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        //5?????????
        System.out.println("???????????????" + response.toString());
    }

    /**
     * ????????????
     */
    @Test
    public void getDocument() throws IOException {
        //??????id???1??????????????????
        GetRequest request = new GetRequest("search4message", "qXE2kIEBKOvMc_HSXxZt");

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println("?????????????????????" + exists);
        //?????????????????????????????????
        if (exists) {
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            System.out.println("??????????????????" + response.getSourceAsString());
            response.getSource().entrySet().stream().forEach(System.out::println);
        }
    }

    /**
     * ????????????
     */
    @Test
    public void updateDocument() throws IOException {
        //??????id???1??????????????????
        UpdateRequest request = new UpdateRequest("user_index", "1");

        User user = new User().setUsername("??????");
        request.doc(JSONObject.toJSONString(user), XContentType.JSON);

        //???????????????????????????
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println("???????????????" + response.status());
    }

    /**
     * ????????????
     */
    @Test
    public void deleteDocument() throws IOException {
        //??????????????????
        DeleteRequest request = new DeleteRequest("user_index", "1");
        //???????????????????????????????????????????????????
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        //??????
        System.out.println("???????????????" + response.status());
    }

    /**
     * ??????????????????
     */
    @Test
    public void createBulkDocument() throws IOException {
        //???????????????????????????
        BulkRequest request = new BulkRequest();
        //??????????????????
        request.timeout("10s");

        //????????????
        List<User> list = new ArrayList<>();
        list.add(new User().setId(1).setUsername("??????"));
        list.add(new User().setId(2).setUsername("??????"));
        list.add(new User().setId(3).setUsername("??????"));
        list.add(new User().setId(4).setUsername("??????"));

        //????????????????????????
        for (int i = 0; i < list.size(); i++) {
            request.add(
                    new IndexRequest("user_index")//????????????
                            .id(String.valueOf(i + 1))//???????????????id??????????????????????????????????????????????????????
                            .source(JSONObject.toJSONString(list.get(i)), XContentType.JSON)//????????????????????????????????????JSON
            );
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println("???????????????????????????" + response.hasFailures());
    }

    /**
     * ??????
     */
    @Test
    public void query() throws IOException {
        //1?????????????????????
        SearchRequest request = new SearchRequest("user_index");

        //2??????????????????????????????????????????????????????
        SearchSourceBuilder builder = new SearchSourceBuilder();//???????????????

        //????????????????????????????????????QueryBuilders?????????
        //QueryBuilders.termQuery()???????????????
        //QueryBuilders.matchAllQuery()???????????????

        //??????????????????????????????
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("username.keyword", "??????");
        //MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        //WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("username", "???");
        builder.query(termQueryBuilder);

        //3???????????????????????????????????????
        request.source(builder);
        //4??????????????????????????????
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        //5???????????????
        SearchHit[] hits = response.getHits().getHits();
        System.out.println("????????????" + hits.length + "?????????");
        System.out.println("???????????????");
        for (int i = 0; i < hits.length; i++) {
            System.out.println(hits[i].getSourceAsString());
        }
    }


    String indexName = "search4message";
    public long countArticle(QueryBuilder query) throws IOException {
        CountRequest countRequest = new CountRequest(indexName);
        countRequest.query(query);
        long count = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT).getCount();
        return count;
    }

    @Test
    public void testCount() throws IOException {
        System.out.println(countArticle(QueryBuilders.matchAllQuery()));
    }


}
