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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author chengcheng
 * @Date 2022-06-27
 **/
@Slf4j
public class EsTest {

    //es???????????????
    private static RestHighLevelClient restHighLevelClient;
    //?????????????????????
    private static BulkProcessor bulkProcessor;

    static {
        List<HttpHost> httpHosts = new ArrayList<>();
        //????????????
        httpHosts.add(new HttpHost("localhost", 9200));
        //httpHosts.add(new HttpHost("localhost", 9200));
        //httpHosts.add(new HttpHost("localhost", 9200));
        //??????host??????
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(1000);
            requestConfigBuilder.setSocketTimeout(1000);
            requestConfigBuilder.setConnectionRequestTimeout(1000);
            return requestConfigBuilder;
        });

        //?????????????????????
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
                log.info("1. ???beforeBulk?????????[{}] ?????? {} ????????????", executionId, request.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (!response.hasFailures()) {
                    log.info("2. ???afterBulk-??????????????? [{}] ????????? {} ms", executionId, response.getTook().getMillis());
                } else {
                    BulkItemResponse[] items = response.getItems();
                    for (BulkItemResponse item : items) {
                        if (item.isFailed()) {
                            log.info("2. ???afterBulk-??????????????? [{}] ????????????????????? : {}", executionId, item.getFailureMessage());
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
                log.error("3. ???afterBulk-failure?????????es??????bluk??????,?????????esId??????{}", esIds, failure);
            }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(((bulkRequest, bulkResponseActionListener) -> {
            restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkResponseActionListener);
        }), listener);
        //??????10000????????????
        builder.setBulkActions(10000);
        //????????????8M?????????
        builder.setBulkSize(new ByteSizeValue(8L, ByteSizeUnit.MB));
        //?????????????????????10s
        builder.setFlushInterval(TimeValue.timeValueSeconds(10));
        //???????????????????????????????????????
        builder.setConcurrentRequests(8);
        //??????????????????
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1), 3));
        return builder.build();
    }


    /**
     * ????????????????????????
     */
    @Test
    public void testAsyncSingle() {
        IndexRequest indexRequest = new IndexRequest("test_demo");
        DemoDto demoDto = new DemoDto(2001L, "????????????????????????", "??????", new Date());
        indexRequest.source(JSON.toJSONString(demoDto), XContentType.JSON);
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //??????????????????????????????
        indexRequest.create(false);
        indexRequest.id(demoDto.getId() + "");
        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        log.error("???id??????{}???????????????ES???????????????????????????????????????{}", indexRequest.id(), failure.getCause());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                log.error("{}:??????es???????????????????????????", indexRequest.id(), e);
            }
        });
    }

    /**
     * ????????????????????????
     */
    @Test
    public void testSingleAdd() throws IOException {
        IndexRequest indexRequest = new IndexRequest("test_demo");
        DemoDto demoDto = new DemoDto(3004L, "?????????????????????????????????????????????????????????????????????????????????????????????30????????????", "??????", new Date());
        indexRequest.source(JSON.toJSONString(demoDto), XContentType.JSON);
        indexRequest.id("3004");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        indexRequest.create(true);
        indexRequest.id(demoDto.getId() + "");
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * ????????????
     */
    @Test
    public void testBatch() {
        List<IndexRequest> indexRequests = new ArrayList<>();
        ArrayList<DemoDto> demoDtos = new ArrayList<>();
        demoDtos.add(new DemoDto(1001L, "?????????????????????????????????????????????", "??????", new Date()));
        demoDtos.add(new DemoDto(1002L, "????????????????????????????????????", "??????", new Date()));
        demoDtos.add(new DemoDto(1003L, "????????????????????????????????????????????????", "??????", new Date()));
        demoDtos.add(new DemoDto(1004L, "????????????????????????????????????", "??????", new Date()));
        demoDtos.add(new DemoDto(1005L, "??????3???3???????????????200??????????????????", "??????", new Date()));
        demoDtos.add(new DemoDto(1006L, "????????????????????????2800????????????", "??????", new Date()));
        demoDtos.forEach(e -> {
            IndexRequest request = new IndexRequest("test_demo");
            //??????id
            request.id(e.getId() + "");
            //????????????id
            request.source(JSON.toJSONString(e), XContentType.JSON);
            request.opType(DocWriteRequest.OpType.CREATE);
            indexRequests.add(request);
        });
        indexRequests.forEach(bulkProcessor::add);
    }

    /**
     * ????????????
     */
    @Test
    public void testSingleUpdate() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest("test_demo", "3001");

        Map<String, Object> kvs = new HashMap<>();
        kvs.put("title", "es?????????????????????");
        updateRequest.doc(kvs);
        updateRequest.timeout(TimeValue.timeValueSeconds(1));
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //??????????????????????????????
        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    /**
     * ????????????????????????
     */
    @Test
    public void testSingleUpdateQuery() throws IOException {


        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest();
        updateByQueryRequest.indices("test_demo");

        updateByQueryRequest.setQuery(new TermQueryBuilder("id", 3001));

        updateByQueryRequest.setScript(new Script(ScriptType.INLINE,
                "painless",
                "ctx._source.tag='??????'", Collections.emptyMap()));
        //??????????????????????????????
        restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
    }

    /**
     * ????????????
     */
    @Test
    public void testBatchUpdate() {

        List<UpdateRequest> updateRequests=new ArrayList<>();

        //???????????????
        List<DemoDto> params=new ArrayList<>();
        params.add(new DemoDto(2001L));
        params.add(new DemoDto(3001L));

        params.forEach(e->{
            //??????id
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index("test_demo");
            //?????????id
            updateRequest.id(e.getId()+"");
            //???????????????
            Map<String,Object> map=new HashMap<>();
            map.put("title","??????????????????");

            updateRequest.doc(map);
            updateRequests.add(updateRequest);
        });
        updateRequests.forEach(bulkProcessor::add);
    }

    /**
     * ????????????
     */
    @Test
    public void testSingleDel() throws IOException {
        DeleteRequest deleteRequest=new DeleteRequest();
        deleteRequest.index("test_demo");
        deleteRequest.id("3001");
        restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    /**
     * ??????????????????
     */
    @Test
    public void testSingleDelQuery() throws IOException {
        DeleteByQueryRequest deleteByQueryRequest=new DeleteByQueryRequest();
        deleteByQueryRequest.indices("test_demo");
        deleteByQueryRequest.setQuery(new MatchQueryBuilder("title","??????"));
        //???????????????
        restHighLevelClient.deleteByQuery(deleteByQueryRequest,RequestOptions.DEFAULT);
    }

    /**
     * match??????
     */
    @Test
    public void testMatch() throws Exception {
        RestHighLevelClient restHighLevelClient = EsUtil.getRestHighLevelClient();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("test_demo");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", "??????");
        //??????????????????
        matchQueryBuilder.lenient();
        searchSourceBuilder.query(matchQueryBuilder);
        //??????Request?????????
        log.info("dsl???" + searchSourceBuilder);

        //????????????
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //???????????????
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            log.info("????????????:" + iterator.next().getSourceAsString());
        }
    }


    /**
     * bool??????
     * @throws IOException
     */
    @Test
    public void testBool() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //??????dsl??????
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //??????????????????
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        //boolQueryBuilder.filter(QueryBuilders.termQuery("title","???"));
        boolQueryBuilder.should(QueryBuilders.termsQuery("title",new String[]{"???","???"}));
        boolQueryBuilder.should(QueryBuilders.termsQuery("title",new String[]{"2","???"}));
        //boolQueryBuilder.filter(QueryBuilders.matchQuery("title","??????"));

        searchSourceBuilder.query(boolQueryBuilder);
        log.info("dsl:"+searchSourceBuilder);
        //?????????Request?????????
        searchRequest.indices("test_demo");
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = EsUtil.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            log.info("????????????:" + iterator.next().getSourceAsString());
        }
    }


    /**
     * constant_score??????
     * @throws IOException
     */
    @Test
    public void testConstantScore() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //??????dsl??????
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //??????????????????
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("title","??????"));
        /**
         * ??????constant_score??????
         */
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("title", "??????"));
        /**
         * ??????bool?????????should???
         */
        boolQueryBuilder.should(constantScoreQueryBuilder);
        /**
         * ???bool???????????????request?????????
         */
        searchSourceBuilder.query(boolQueryBuilder);
        log.info("dsl:"+searchSourceBuilder.toString());
        //?????????Request?????????
        remoteSearch(searchRequest, searchSourceBuilder);
    }

    //????????????
    private static void remoteSearch(SearchRequest searchRequest, SearchSourceBuilder searchSourceBuilder) throws IOException {
        searchRequest.indices("test_demo");
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = EsUtil.getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            log.info("????????????:" + next.getScore());
            log.info("????????????:" + next.getSourceAsString());
        }
    }

    /**
     * matchPhraseQuery??????
     */
    @Test
    public void testMatchPhraseQuery() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //??????dsl??????
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //??????????????????
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("title", "??????");
        matchPhraseQueryBuilder.slop(0);
        boolQueryBuilder.should(matchPhraseQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //?????????Request?????????
        remoteSearch(searchRequest, searchSourceBuilder);
    }

    /**
     * range????????????
     */
    @Test
    public void testRange() throws IOException {
        SearchRequest searchRequest=new SearchRequest();
        //??????dsl??????
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //??????????????????
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
        rangeQueryBuilder.gt("2022-05-01 12:12:12");

        boolQueryBuilder.filter(rangeQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //?????????Request?????????
        remoteSearch(searchRequest, searchSourceBuilder);
    }


    @Test
    public void test1(){
        String s = "{\"stateCode\":0,\"data\":{\"nextCursor\":\"DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAANy4WUy1PTUR6bmFUY09Vc2k0UEhPMnY2QQ==\",\"total\":16302967,\"list\":[{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42693704\",\"msgTime\":\"1645787600\",\"content\":\"{\\\\'content\\\\':\\\\'?????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42693702\",\"msgTime\":\"1645787540\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1614296539\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":true},\"recipient\":{\"account\":\"1136840674\",\"nickName\":\"???????????????????????????\",\"userName\":\"ququn_bot\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":true},\"message\":{\"id\":\"42697139\",\"msgTime\":\"1645787507\",\"content\":\"{\\\\'content\\\\':\\\\'/start\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":6,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1409101328\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690125\",\"msgTime\":\"1645787459\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"917232858\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690152\",\"msgTime\":\"1645784124\",\"content\":\"{\\\\'content\\\\':\\\\'?????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121735091\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690154\",\"msgTime\":\"1645784098\",\"content\":\"{\\\\'content\\\\':\\\\'tg?????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":5,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690156\",\"msgTime\":\"1645783689\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690158\",\"msgTime\":\"1645783688\",\"content\":\"{\\\\'content\\\\':\\\\'?????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2121518049\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690160\",\"msgTime\":\"1645783687\",\"content\":\"{\\\\'content\\\\':\\\\'????????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690162\",\"msgTime\":\"1645783421\",\"content\":\"{\\\\'content\\\\':\\\\'????????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690164\",\"msgTime\":\"1645783420\",\"content\":\"{\\\\'content\\\\':\\\\'??????????????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":6,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2140654677\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690166\",\"msgTime\":\"1645783419\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690168\",\"msgTime\":\"1645783055\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690170\",\"msgTime\":\"1645783051\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690172\",\"msgTime\":\"1645782962\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"1991020871\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690182\",\"msgTime\":\"1645782713\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2130971706\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690183\",\"msgTime\":\"1645782559\",\"content\":\"{\\\\'content\\\\':\\\\'??????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":2,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690185\",\"msgTime\":\"1645782422\",\"content\":\"{\\\\'content\\\\':\\\\'?????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":3,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690187\",\"msgTime\":\"1645782421\",\"content\":\"{\\\\'content\\\\':\\\\'????????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}},{\"sender\":{\"account\":\"2100638988\",\"nickName\":null,\"userName\":null,\"icon\":null,\"phone\":null,\"remark\":null,\"type\":null,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"recipient\":{\"account\":\"1444469804\",\"nickName\":\"???????????????????????????/????????????\",\"userName\":\"QuQunSou8\",\"icon\":null,\"phone\":null,\"remark\":null,\"type\":2,\"address\":null,\"labelList\":null,\"isImportantPerson\":false,\"isImportantGroup\":false,\"isContact\":false},\"message\":{\"id\":\"42690189\",\"msgTime\":\"1645782420\",\"content\":\"{\\\\'content\\\\':\\\\'????????????\\\\'}\",\"messageType\":30001,\"informationType\":10000,\"senderType\":10000,\"charCount\":4,\"isFavoriteMessage\":false}}]},\"error\":null,\"errorMsg\":null}";

        System.out.println(s);
        System.out.println(s.replaceAll("\"","\\\""));
    }
}
