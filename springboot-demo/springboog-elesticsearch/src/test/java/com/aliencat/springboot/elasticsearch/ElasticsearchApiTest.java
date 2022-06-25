package com.aliencat.springboot.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.aliencat.springboot.elesticsearch.ElasticsearchApplication;
import com.aliencat.springboot.elesticsearch.dao.EsArticleDao;
import com.aliencat.springboot.elesticsearch.pojo.User;
import com.aliencat.springboot.elesticsearch.repository.ArticleRepository;
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
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
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
    ArticleRepository articleRepository;

    @Autowired
    RestHighLevelClient restHighLevelClient;


    @Autowired
    EsArticleDao esArticleDao;
    /**
     * 创建索引测试
     */
    @Test
    public void createIndex() throws IOException {
        //1、构建 创建索引的请求
        CreateIndexRequest request = new CreateIndexRequest("my_index");//索引名
        //2、客户端执行请求,获取响应
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("创建成功，创建的索引名为：" + response.index());
    }

    /**
     * 获取索引测试
     */
    @Test
    public void getIndex() throws IOException {
        //1、构建 获取索引的请求
        GetIndexRequest request = new GetIndexRequest("my_index");
        //2、客户端判断该索引是否存在
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("该索引是否存在：" + exists);
    }


    /**
     * 删除索引测试
     */
    @Test
    public void deleteIndex() throws IOException {
        //1、构建 删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("my_index");
        //2、客户段执行删除的请求
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        //3、打印
        System.out.println("是否删除成功：" + response.isAcknowledged());
    }


    /**
     * 创建文档
     */
    @Test
    public void createDocument() throws IOException {
        User user = new User().setId(1).setUsername("张三");

        //1、构建请求
        IndexRequest request = new IndexRequest("user_index");

        //2、设置规则  PUT /user_index/user/_doc/1
        request.id("1");//设置id
        request.timeout(TimeValue.timeValueSeconds(1));//设置超时时间

        //3、将数据放入到请求中,以JSON的格式存放
        request.source(JSONObject.toJSONString(user), XContentType.JSON);

        //4、客户端发送请求,获取响应结果
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);

        //5、打印
        System.out.println("响应结果：" + response.toString());
    }

    /**
     * 获取文档
     */
    @Test
    public void getDocument() throws IOException {
        //获取id为1的文档的信息
        GetRequest request = new GetRequest("search4message", "qXE2kIEBKOvMc_HSXxZt");

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println("文档是否存在：" + exists);
        //如果存在，获取文档信息
        if (exists) {
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            System.out.println("文档内容为：" + response.getSourceAsString());
            response.getSource().entrySet().stream().forEach(System.out::println);
        }
    }

    /**
     * 更新文档
     */
    @Test
    public void updateDocument() throws IOException {
        //更新id为1的文档的信息
        UpdateRequest request = new UpdateRequest("user_index", "1");

        User user = new User().setUsername("李四");
        request.doc(JSONObject.toJSONString(user), XContentType.JSON);

        //客户端执行更新请求
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println("更新状态：" + response.status());
    }

    /**
     * 删除文档
     */
    @Test
    public void deleteDocument() throws IOException {
        //构建删除请求
        DeleteRequest request = new DeleteRequest("user_index", "1");
        //客户端执行删除请求，并获取响应结果
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        //打印
        System.out.println("删除状态：" + response.status());
    }

    /**
     * 批量插入数据
     */
    @Test
    public void createBulkDocument() throws IOException {
        //构建批量插入的请求
        BulkRequest request = new BulkRequest();
        //设置超时时间
        request.timeout("10s");

        //设置数据
        List<User> list = new ArrayList<>();
        list.add(new User().setId(1).setUsername("张三"));
        list.add(new User().setId(2).setUsername("李四"));
        list.add(new User().setId(3).setUsername("王五"));
        list.add(new User().setId(4).setUsername("赵六"));

        //批量插入请求设置
        for (int i = 0; i < list.size(); i++) {
            request.add(
                    new IndexRequest("user_index")//设置索引
                            .id(String.valueOf(i + 1))//设置文档的id，如果没有指定，会随机生成，自己测试
                            .source(JSONObject.toJSONString(list.get(i)), XContentType.JSON)//设置要添加的资源，类型为JSON
            );
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println("批量插入是否失败：" + response.hasFailures());
    }

    /**
     * 查询
     */
    @Test
    public void query() throws IOException {
        //1、构建搜索请求
        SearchRequest request = new SearchRequest("user_index");

        //2、设置搜索条件，使用该构建器进行查询
        SearchSourceBuilder builder = new SearchSourceBuilder();//生成构建器

        //查询条件我们可以用工具类QueryBuilders来构建
        //QueryBuilders.termQuery()：精确匹配
        //QueryBuilders.matchAllQuery()：全文匹配

        //构建精确匹配查询条件
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("username.keyword", "李四");
        //MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        //WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("username", "张");
        builder.query(termQueryBuilder);

        //3、将搜索条件放入搜索请求中
        request.source(builder);
        //4、客户端执行搜索请求
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        //5、打印测试
        SearchHit[] hits = response.getHits().getHits();
        System.out.println("共查询到" + hits.length + "条数据");
        System.out.println("查询结果：");
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

    @Test
    public void testSearch() throws IOException {
        System.out.println(esArticleDao.searchArticle(QueryBuilders.matchAllQuery(),null,null,10000,0));
    }
}
