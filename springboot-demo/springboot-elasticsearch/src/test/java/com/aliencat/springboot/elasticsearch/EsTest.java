package com.aliencat.springboot.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.aliencat.springboot.elasticsearch.pojo.DemoDto;
import com.aliencat.springboot.elasticsearch.utils.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author chengcheng
 * @Date 2022-06-27
 **/
@Slf4j
public class EsTest {

    //es操作客户端
    private static RestHighLevelClient restHighLevelClient;
    //批量操作的对象
    private static BulkProcessor bulkProcessor;

    static {
        List<HttpHost> httpHosts = new ArrayList<>();
        //填充数据
        httpHosts.add(new HttpHost("60.12.236.206", 13699));
        //httpHosts.add(new HttpHost("localhost", 9200));
        //httpHosts.add(new HttpHost("localhost", 9200));
        //填充host节点
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(1000);
            requestConfigBuilder.setSocketTimeout(1000);
            requestConfigBuilder.setConnectionRequestTimeout(1000);
            return requestConfigBuilder;
        });

        //填充用户名密码
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("userName", "password"));

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(30);
            httpClientBuilder.setMaxConnPerRoute(30);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });

        restHighLevelClient = new RestHighLevelClient(builder);
    }

    static {
        bulkProcessor=createBulkProcessor();
    }

    private static BulkProcessor createBulkProcessor() {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                log.info("1. 【beforeBulk】批次[{}] 携带 {} 请求数量", executionId, request.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (!response.hasFailures()) {
                    log.info("2. 【afterBulk-成功】批量 [{}] 完成在 {} ms", executionId, response.getTook().getMillis());
                } else {
                    BulkItemResponse[] items = response.getItems();
                    for (BulkItemResponse item : items) {
                        if (item.isFailed()) {
                            log.info("2. 【afterBulk-失败】批量 [{}] 出现异常的原因 : {}", executionId, item.getFailureMessage());
                            break;
                        }
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {

                List<DocWriteRequest<?>> requests = request.requests();
                List<String> esIds = requests.stream().map(DocWriteRequest::id).collect(Collectors.toList());
                log.error("3. 【afterBulk-failure失败】es执行bluk失败,失败的esId为：{}", esIds, failure);
            }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(((bulkRequest, bulkResponseActionListener) -> {
            restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkResponseActionListener);
        }), listener);
        //到达10000条时刷新
        builder.setBulkActions(10000);
        //内存到达8M时刷新
        builder.setBulkSize(new ByteSizeValue(8L, ByteSizeUnit.MB));
        //设置的刷新间隔10s
        builder.setFlushInterval(TimeValue.timeValueSeconds(10));
        //设置允许执行的并发请求数。
        builder.setConcurrentRequests(8);
        //设置重试策略
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1), 3));
        return builder.build();
    }


    /**
     * 单条数据异步插入
     */
    @Test
    public void testAsyncSingle() {
        IndexRequest indexRequest = new IndexRequest("test_demo");
        DemoDto demoDto = new DemoDto(2001L, "印度新冠疫情失控", "世界", new Date());
        indexRequest.source(JSON.toJSONString(demoDto), XContentType.JSON);
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //数据为存储而不是更新
        indexRequest.create(false);
        indexRequest.id(demoDto.getId() + "");
        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        log.error("将id为：{}的数据存入ES时存在失败的分片，原因为：{}", indexRequest.id(), failure.getCause());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error("{}:存储es时异常，数据信息为", indexRequest.id(), e);
            }
        });
    }

    /**
     * 单条数据同步插入
     */
    @Test
    public void testSingleAdd() throws IOException {
        IndexRequest indexRequest = new IndexRequest("test_demo");
        DemoDto demoDto = new DemoDto(3004L, "英国已沦为全球诈骗“重灾区”，每年因诈骗造成的损失金额飙升至近30亿英镑。", "国家", new Date());
        indexRequest.source(JSON.toJSONString(demoDto), XContentType.JSON);
        indexRequest.id("3004");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.create(true);
        indexRequest.id(demoDto.getId() + "");
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量插入
     */
    @Test
    public void testBatch() {
        List<IndexRequest> indexRequests = new ArrayList<>();
        ArrayList<DemoDto> demoDtos = new ArrayList<>();
        demoDtos.add(new DemoDto(1001L, "人民网：德云社该好好自我检视了", "新闻", new Date()));
        demoDtos.add(new DemoDto(1002L, "粮食主产区夏种加快推进热", "食品", new Date()));
        demoDtos.add(new DemoDto(1003L, "男生宣传母校被吐槽是“招生减章”", "教育", new Date()));
        demoDtos.add(new DemoDto(1004L, "俄乌冲突以来普京首次出访", "军事", new Date()));
        demoDtos.add(new DemoDto(1005L, "考生3年3地考北大赚200万？官方调查", "教育", new Date()));
        demoDtos.add(new DemoDto(1006L, "男子吃海鲜被要价2800后报警新", "食品", new Date()));
        demoDtos.forEach(e -> {
            IndexRequest request = new IndexRequest("test_demo");
            //填充id
            request.id(e.getId() + "");
            //先不修改id
            request.source(JSON.toJSONString(e), XContentType.JSON);
            request.opType(DocWriteRequest.OpType.CREATE);
            indexRequests.add(request);
        });
        indexRequests.forEach(bulkProcessor::add);
    }

    /**
     * 更新操作
     */
    @Test
    public void testSingleUpdate() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("test_demo", "3001");

        Map<String, Object> kvs = new HashMap<>();
        kvs.put("title", "es单数据更新啦！");
        updateRequest.doc(kvs);
        updateRequest.timeout(TimeValue.timeValueSeconds(1));
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //数据为存储而不是更新
        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    /**
     * 带条件的更新语句
     */
    @Test
    public void testSingleUpdateQuery() throws IOException {


        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices("test_demo");

        updateByQueryRequest.setQuery(new TermQueryBuilder("id", 3001));

        updateByQueryRequest.setScript(new Script(ScriptType.INLINE,
                "painless",
                "ctx._source.tag='电脑'", Collections.emptyMap()));
        //数据为存储而不是更新
        restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量更新
     */
    @Test
    public void testBatchUpdate() {

        List<UpdateRequest> updateRequests=new ArrayList<>();

        //更新的数据
        List<DemoDto> params=new ArrayList<>();
        params.add(new DemoDto(2001L));
        params.add(new DemoDto(3001L));

        params.forEach(e->{
            //获取id
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index("test_demo");
            //更新的id
            updateRequest.id(e.getId()+"");
            //更新的数据
            Map<String,Object> map=new HashMap<>();
            map.put("title","美国社会动荡");

            updateRequest.doc(map);
            updateRequests.add(updateRequest);
        });
        updateRequests.forEach(bulkProcessor::add);
    }

    /**
     * 单个删除
     */
    @Test
    public void testSingleDel() throws IOException {
        DeleteRequest deleteRequest=new DeleteRequest();
        deleteRequest.index("test_demo");
        deleteRequest.id("3001");
        restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    /**
     * 单个条件删除
     */
    @Test
    public void testSingleDelQuery() throws IOException {
        DeleteByQueryRequest deleteByQueryRequest=new DeleteByQueryRequest();
        deleteByQueryRequest.indices("test_demo");
        deleteByQueryRequest.setQuery(new MatchQueryBuilder("title","人民"));
        //分词式删除
        restHighLevelClient.deleteByQuery(deleteByQueryRequest,RequestOptions.DEFAULT);
    }

    /**
     * match查询
     */
    @Test
    public void testMatch() throws Exception {
        RestHighLevelClient restHighLevelClient = EsUtil.getRestHighLevelClient();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("test_demo");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", "海鲜");
        //忽略格式错误
        matchQueryBuilder.lenient();
        searchSourceBuilder.query(matchQueryBuilder);
        //放入Request对象中
        log.info("dsl：" + searchSourceBuilder);

        //组装语句
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //获取到结果
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            log.info("输出数据:" + iterator.next().getSourceAsString());
        }
    }


    /**
     * bool查询
     * @throws IOException
     */
    @Test
    public void testBool() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //构建dsl语句
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //boolQueryBuilder.filter(QueryBuilders.termQuery("title","英"));
        boolQueryBuilder.should(QueryBuilders.termsQuery("title",new String[]{"英","印"}));
        boolQueryBuilder.should(QueryBuilders.termsQuery("title",new String[]{"2","印"}));
        //boolQueryBuilder.filter(QueryBuilders.matchQuery("title","英国"));

        searchSourceBuilder.query(boolQueryBuilder);
        log.info("dsl:"+searchSourceBuilder);
        //填充到Request对象中
        searchRequest.indices("test_demo");
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = EsUtil.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            log.info("输出数据:" + iterator.next().getSourceAsString());
        }
    }


    /**
     * constant_score查询
     * @throws IOException
     */
    @Test
    public void testConstantScore() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //构建dsl语句
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("title","英国"));
        /**
         * 设置constant_score搜索
         */
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("title", "疫情"));
        /**
         * 放入bool查询的should中
         */
        boolQueryBuilder.should(constantScoreQueryBuilder);
        /**
         * 将bool查询放入到request对象中
         */
        searchSourceBuilder.query(boolQueryBuilder);
        log.info("dsl:"+searchSourceBuilder.toString());
        //填充到Request对象中
        remoteSearch(searchRequest, searchSourceBuilder);
    }

    //远程调用
    private static void remoteSearch(SearchRequest searchRequest, SearchSourceBuilder searchSourceBuilder) throws IOException {
        searchRequest.indices("test_demo");
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = EsUtil.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            log.info("输出分数:" + next.getScore());
            log.info("输出数据:" + next.getSourceAsString());
        }
    }

    /**
     * matchPhraseQuery搜索
     */
    @Test
    public void testMatchPhraseQuery() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //构建dsl语句
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("title", "英国");
        matchPhraseQueryBuilder.slop(0);
        boolQueryBuilder.should(matchPhraseQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //填充到Request对象中
        remoteSearch(searchRequest, searchSourceBuilder);
    }

    /**
     * range范围查询
     */
    @Test
    public void testRange() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //构建dsl语句
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
        rangeQueryBuilder.gt("2022-05-01 12:12:12");

        boolQueryBuilder.filter(rangeQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //填充到Request对象中
        remoteSearch(searchRequest, searchSourceBuilder);
    }


    @Test
    public void test1(){
        String s = "{\"stateCode\":0,\"data\":{\"nextCursor\":\"DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAANy4WUy1PTUR6bmFUY09Vc2k0UEhPMnY2QQ==\",\"total\":16302967,\"list\":[{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42693704\",\"msgTime\":\"1645787600\",\"content\":\"{\\\\'content\\\\':\\\\'小奶猫\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42693702\",\"msgTime\":\"1645787540\",\"content\":\"{\\\\'content\\\\':\\\\'主播\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1614296539\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":true},\"recipient\":{\"account\":\"1136840674\",\"nickName\":\"中文趣群搜索机器人\",\"userName\":\"ququn_bot\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":true},\"message\":{\"id\":\"42697139\",\"msgTime\":\"1645787507\",\"content\":\"{\\\\'content\\\\':\\\\'/start\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":6,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690125\",\"msgTime\":\"1645787459\",\"content\":\"{\\\\'content\\\\':\\\\'直播\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"917232858\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690152\",\"msgTime\":\"1645784124\",\"content\":\"{\\\\'content\\\\':\\\\'李赛高\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121735091\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690154\",\"msgTime\":\"1645784098\",\"content\":\"{\\\\'content\\\\':\\\\'tg代理线\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":5,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690156\",\"msgTime\":\"1645783689\",\"content\":\"{\\\\'content\\\\':\\\\'四方\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690158\",\"msgTime\":\"1645783688\",\"content\":\"{\\\\'content\\\\':\\\\'运营商\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690160\",\"msgTime\":\"1645783687\",\"content\":\"{\\\\'content\\\\':\\\\'菠菜资源\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690162\",\"msgTime\":\"1645783421\",\"content\":\"{\\\\'content\\\\':\\\\'欧盟担保\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690164\",\"msgTime\":\"1645783420\",\"content\":\"{\\\\'content\\\\':\\\\'出售对公账户\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":6,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690166\",\"msgTime\":\"1645783419\",\"content\":\"{\\\\'content\\\\':\\\\'珠海\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690168\",\"msgTime\":\"1645783055\",\"content\":\"{\\\\'content\\\\':\\\\'母狗\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690170\",\"msgTime\":\"1645783051\",\"content\":\"{\\\\'content\\\\':\\\\'同房\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690172\",\"msgTime\":\"1645782962\",\"content\":\"{\\\\'content\\\\':\\\\'生活\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690182\",\"msgTime\":\"1645782713\",\"content\":\"{\\\\'content\\\\':\\\\'居家\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2130971706\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690183\",\"msgTime\":\"1645782559\",\"content\":\"{\\\\'content\\\\':\\\\'私我\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690185\",\"msgTime\":\"1645782422\",\"content\":\"{\\\\'content\\\\':\\\\'阿拉棒\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690187\",\"msgTime\":\"1645782421\",\"content\":\"{\\\\'content\\\\':\\\\'约会交友\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"中文搜索大全，群组/频道分享\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690189\",\"msgTime\":\"1645782420\",\"content\":\"{\\\\'content\\\\':\\\\'接码平台\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}}]},\"error\":null,\"errorMsg\":null}";

        System.out.println(s);
        System.out.println(s.replaceAll("\"","\\\""));
    }

    @Test
    public void test2() throws ParseException {

        String date1 = "2021-01-01";
        String date2 = "2021-06-01";
        String date3 = "2022-01-01";
        String date4 = "2022-02-01";
        String date5 = "2022-03-01";
        String date6 = "2022-04-01";
        String date7 = "2022-05-01";
        String date8 = "2022-06-01";
        String date9 = "2022-07-01";
        String date10 = "2022-08-01";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(date1+"    "+simpleDateFormat.parse(date1).getTime() / 1000);
        System.out.println(date2+"    "+simpleDateFormat.parse(date2).getTime() / 1000);
        System.out.println(date3+"    "+simpleDateFormat.parse(date3).getTime() / 1000);
        System.out.println(date4+"    "+simpleDateFormat.parse(date4).getTime() / 1000);
        System.out.println(date5+"    "+simpleDateFormat.parse(date5).getTime() / 1000);
        System.out.println(date6+"    "+simpleDateFormat.parse(date6).getTime() / 1000);
        System.out.println(date7+"    "+simpleDateFormat.parse(date7).getTime() / 1000);
        System.out.println(date8+"    "+simpleDateFormat.parse(date8).getTime() / 1000);
        System.out.println(date9+"    "+simpleDateFormat.parse(date9).getTime() / 1000);
        System.out.println(date10+"    "+(simpleDateFormat.parse(date10).getTime() - simpleDateFormat.parse(date9).getTime()) / 1000);
    }

    @Test
    public void test3() throws ParseException {

        String date1 = "1970-01-01 00:00:00";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat2.format(1609466400000L  ));
        System.out.println(simpleDateFormat2.parse(date1).getTime());

        HashMap<String, String> stringObjectHashMap = new HashMap<>();
        HashMap<String, String> stringObjectHashMap2 = new HashMap<>();

    }
}
