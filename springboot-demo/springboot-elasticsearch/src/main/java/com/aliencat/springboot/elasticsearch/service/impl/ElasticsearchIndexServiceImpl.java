package com.aliencat.springboot.elasticsearch.service.impl;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import com.aliencat.springboot.elasticsearch.solr.SearchSolr;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chengcheng
 * @Date 2022-07-11
 **/
@Service
@Slf4j
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {

    long message_total = 0L;
    long message_total2 = 0L;
    long contact_total = 0L;
    long message_time = 0L;
    long message_time2 = 0L;
    long contact_time = 0L;

    String message_cursorMark;
    String message_cursorMark2;
    String contact_cursorMark;

    int qTime = 0;

    long esTime = 0;

    boolean messageSwitch1 = true;
    boolean messageSwitch2 = true;

    boolean pause = false;

    ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(20);

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 6,
            60L, TimeUnit.SECONDS, workQueue
            , (r, executor) -> {
        if (!executor.isShutdown()) {
            try {
                System.out.println("队列已满，等待重新入队");
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    });
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    boolean isJoin = true;
    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;


    //es操作客户端
    private static RestHighLevelClient restHighLevelClient2;
    //批量操作的对象
    private static BulkProcessor bulkProcessor;

    static {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200").withSocketTimeout(120000)
                .build();
        restHighLevelClient2 = RestClients.create(configuration).rest();;
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        QueryResponse queryResponse = SearchSolr.queryByCursor2(SearchSolr.getMessageClient(), CursorMarkParams.CURSOR_MARK_START);
        SolrDocumentList results = queryResponse.getResults();
        System.out.println(results.size());
        System.out.println(queryResponse.getQTime());
        System.out.println((System.currentTimeMillis() - start) / 1000);
        int maxSize = results.size() / 10000;
        Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .forEach(list -> {
                    System.out.println(list.size());
                });
    }

    @Override
    public int getSolrSpendTime() {
        return qTime;
    }

    @Override
    public String getMessageProcess() {
        String out = "处理开始时间：" + simpleDateFormat.format(new Date()) +
                "\n降序处理" + message_total + "条message数据耗时：" + (message_time == 0L ? 0 : (System.currentTimeMillis() - message_time) / 1000) + "s "
                + "\n升序处理" + message_total2 + "条message数据耗时：" + (message_time2 == 0L ? 0 : (System.currentTimeMillis() - message_time2) / 1000) + "s "
                + "\n共计处理：" + (message_total + message_total2)
                + "\n当前es插入接口耗时：" + esTime + "ms"
                + "\n当前solr查询接口耗时：" + qTime + "ms"
                + "\n当前solr降序查询游标：" + message_cursorMark
                + "\n当前solr升序查询游标：" + message_cursorMark2
                + "\n当前任务队列容量：" + workQueue.size()
                + "\n";
        System.out.println(out);
        return out;
    }

    @Override
    public String getContactProcess() {
        return "处理" + contact_total + "条message数据耗时：" + (System.currentTimeMillis() - contact_time) / 1000 + "s ";
    }

    @Override
    public boolean createIndex(String index, String mapping) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(mapping, XContentType.JSON);
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards",10).build());
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
    public void messageBatchUpdate(String cursorMark) {
        message_time = System.currentTimeMillis();
        System.out.println("message批处理程序启动：" + simpleDateFormat.format(new Date(message_time)));
        System.out.println("cursorMark：" + cursorMark);
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4MESSAGE;
        String nextCursor = Optional.ofNullable(cursorMark).orElse(CursorMarkParams.CURSOR_MARK_START);//游标初始化
        QueryResponse queryResponse;
        try {
            do {
                while (pause) {
                }
                message_cursorMark = nextCursor;
                queryResponse = SearchSolr.queryByCursor(SearchSolr.getMessageClient(), message_cursorMark);
                nextCursor = queryResponse.getNextCursorMark();
                qTime = queryResponse.getQTime();
                SolrDocumentList results = queryResponse.getResults();
                int maxSize = results.size() / 10000;
                Stream.iterate(0, n -> n + 1)
                        .limit(maxSize)
                        .parallel()
                        .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                        .filter(b -> !b.isEmpty())
                        .forEach(list -> {
                            MessageRunable runableTest = new MessageRunable(index, list);
                            threadPoolExecutor.execute(runableTest);
                        });
                message_total = message_total + results.size();
                System.out.println(simpleDateFormat.format(new Date()) + " 已处理message数量：" + message_total);
                //如果两次游标一样，说明数据拉取完毕，可以结束循环了
            } while (messageSwitch1 && !message_cursorMark.equals(nextCursor) && (message_total + message_total2) < queryResponse.getResults().getNumFound());
            message_time = (System.currentTimeMillis() - message_time) / 1000;
            System.out.println("降序处理" + message_total + "条message数据总耗时：" + message_time + "s ");
            System.out.println("message批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
        } catch (Exception e) {
            e.printStackTrace();
            messageBatchUpdate(cursorMark);
        }
    }

    @Override
    public void messageBatchUpdate2(String cursorMark) {
        message_time2 = System.currentTimeMillis();
        System.out.println("message2批处理程序启动：" + simpleDateFormat.format(new Date(message_time2)));
        System.out.println("cursorMark2：" + cursorMark);
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4MESSAGE;
        String nextCursor = Optional.ofNullable(cursorMark).orElse(CursorMarkParams.CURSOR_MARK_START);//游标初始化
        QueryResponse queryResponse;
        try {
            do {
                while (pause) {
                }
                message_cursorMark2 = nextCursor;
                queryResponse = SearchSolr.queryByCursor2(SearchSolr.getMessageClient(), message_cursorMark2);
                nextCursor = queryResponse.getNextCursorMark();
                qTime = queryResponse.getQTime();
                SolrDocumentList results = queryResponse.getResults();
                int maxSize = results.size() / 10000;
                Stream.iterate(0, n -> n + 1)
                        .limit(maxSize)
                        .parallel()
                        .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                        .filter(b -> !b.isEmpty())
                        .forEach(list -> {
                            MessageRunable runableTest = new MessageRunable(index, list);
                            threadPoolExecutor.execute(runableTest);
                        });
                message_total2 = message_total2 + results.size();
                System.out.println(simpleDateFormat.format(new Date()) + " 已处理message2数量：" + message_total2);
                //如果两次游标一样，说明数据拉取完毕，可以结束循环了
            } while (messageSwitch2 && !message_cursorMark2.equals(nextCursor) && (message_total + message_total2) < queryResponse.getResults().getNumFound());
            message_time2 = (System.currentTimeMillis() - message_time2) / 1000;
            System.out.println("升序处理" + message_total2 + "条message2数据总耗时：" + message_time2 + "s ");
            System.out.println("message2批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
        } catch (Exception e) {
            e.printStackTrace();
            messageBatchUpdate(cursorMark);
        }
    }

    volatile boolean flag = true;
    /**
     * 更新es message索引中的落地信息
     * @param cursorMark
     */
    @Override
    public void messageBatchUpdate3(String cursorMark) {
        contact_time = System.currentTimeMillis();
        log.info("messageBatchUpdate3批处理程序启动：" + simpleDateFormat.format(new Date(contact_time)));
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4MESSAGE;
        nextCursor = Optional.ofNullable(cursorMark).orElse(CursorMarkParams.CURSOR_MARK_START);//游标初始化
        int size;
        do {
            long start = System.currentTimeMillis();
            contact_cursorMark = nextCursor;
            SearchResponse searchResponse = null;
            try {
                searchResponse = scrollMessage();
                total = total + searchResponse.getHits().getTotalHits().value;
                nextCursor = searchResponse.getScrollId();
                log.debug("nextCursor: {}" , nextCursor);
            } catch (IOException e) {
                log.info("分页异常：",e);
            }
            List<Map<String, Object>> results = Stream.of(searchResponse.getHits().getHits())
                    .map(hit->{
                        Map<String, Object> map = hit.getSourceAsMap();
                        map.put("_id",hit.getId());
                        return map;
                    }).collect(Collectors.toList());
            qTime = (int) searchResponse.getTook().getMillis();
            MessageUpdateRunable runableTest = new MessageUpdateRunable(index, results);
            threadPoolExecutor.execute(runableTest);
            size = results.size();
            message_total = message_total + size;
            log.info("已处理" + message_total + "条contact数据,耗时：" + (System.currentTimeMillis() - start)  + "ms ");
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (flag);
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        System.out.println("处理" + message_total + "条message数据总耗时：" + contact_time + "s ");
        System.out.println("message批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void contactBatchUpdate(String cursorMark) {
        contact_time = System.currentTimeMillis();
        System.out.println("contact批处理程序启动：" + simpleDateFormat.format(new Date(contact_time)));
        SearchSolr.queryJointestByCursor();
        String index = IndexConstant.SEARCH4CONTACT;
        String nextCursor = Optional.ofNullable(cursorMark).orElse(CursorMarkParams.CURSOR_MARK_START);//游标初始化
        do {
            contact_cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursor2(SearchSolr.getContactClient(), contact_cursorMark);
            nextCursor = queryResponse.getNextCursorMark();
            qTime = queryResponse.getQTime();
            SolrDocumentList results = queryResponse.getResults();
            int maxSize = results.size() / 10000;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .parallel()
                    .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .filter(b -> !b.isEmpty())
                    .forEach(list -> {
                        ContactRunable runableTest = new ContactRunable(index, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!contact_cursorMark.equals(nextCursor));
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        log.info("处理" + contact_total + "条contact数据总耗时：" + contact_time + "s ");
        log.info("contact批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void contactTransferEs(String fromIndex,String toIndex) {
        contact_time = System.currentTimeMillis();
        log.info("contact迁移程序启动：" + simpleDateFormat.format(new Date(contact_time)));
        String index = IndexConstant.TG_CONTACT;
        nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        int size;
        do {
            long start = System.currentTimeMillis();
            contact_cursorMark = nextCursor;
            SearchResponse searchResponse = null;
            try {
                searchResponse = scrollContact(fromIndex);
                total = total + searchResponse.getHits().getTotalHits().value;
                nextCursor = searchResponse.getScrollId();
                log.debug("nextCursor: {}" , nextCursor);
            } catch (IOException e) {
                log.info("分页异常：",e);
            }
            List<Map<String, Object>> results = Stream.of(searchResponse.getHits().getHits())
                    .map(SearchHit::getSourceAsMap).collect(Collectors.toList());
            qTime = (int) searchResponse.getTook().getMillis();
            int maxSize = results.size() / 10000;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .parallel()
                    .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .filter(b -> !b.isEmpty())
                    .forEach(list -> {
                        ContactToEsRunable runableTest = new ContactToEsRunable(toIndex, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            size = results.size();
            contact_total = contact_total + size;
            log.info("已处理" + contact_total + "条contact数据,耗时：" + (System.currentTimeMillis() - start)  + "ms ");
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (size != 0);
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        System.out.println("处理" + contact_total + "条contact数据总耗时：" + contact_time + "s ");
        System.out.println("contact批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
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
            IndexRequest source = new IndexRequest(index, "_doc", map.get("id").toString()).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            esTime = bulk.getTook().getMillis();
        } catch (IOException e) {
            log.info(simpleDateFormat.format(new Date()) + "发生异常 ：" + e);
            log.info("当前message游标1：" + message_cursorMark);
            log.info("当前message游标2：" + message_cursorMark2);
        }
    }

    public void bulkUpdateMessageIndex(String index, List<Map<String, Object>> list) {
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
            }else if(!StringUtils.isEmpty(map.get("country"))){
                flag = false;
                continue;
            }else{
                continue;
            }
            String id = map.get("_id").toString();
            map.remove("_version_");
            map.remove("_id");
            UpdateRequest doc = new UpdateRequest(index,  id).doc(map, XContentType.JSON);
            request.add(doc);
        }
        BulkResponse bulk;
        try {
            bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            esTime = bulk.getTook().getMillis();
        } catch (IOException e) {
            log.info(simpleDateFormat.format(new Date()) + "发生异常 ：" + e);
        }
    }

    public void bulkPutContactIndex(String index, List<SolrDocument> list) {
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
            IndexRequest source = new IndexRequest(index, "_doc", map.get("id").toString()).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("当前contact游标：" + contact_cursorMark + ";发生异常 ：" + e);
        }
    }


    public void bulkPutContactIndex2(String index, List<Map<String, Object>> list) throws ParseException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        int size = list.size();
        BulkRequest request = new BulkRequest();
        int count = 0;
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            Object ex_k_updateTime = map.get("ex_k_updateTime");
            if(map.get("gmt_create") == null && ex_k_updateTime != null) {
                Date updateTime;
                if(ex_k_updateTime instanceof String){
                    updateTime = simpleDateFormat.parse(ex_k_updateTime.toString());
                }else {
                    updateTime = (Date) map.get("ex_k_updateTime");
                }
                map.put("gmt_create", updateTime.getTime() / 1000);
                map.put("ex_k_updateTime",updateTime);
            }
            String id = createId(map);
            if(id == null){
                count++;
                continue;
            }
            IndexRequest source = new IndexRequest(index, "_doc", id).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        BulkResponse bulk = null;
        try {
            if(!CollectionUtils.isEmpty(list) || count != list.size()) {
                bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
/*                BulkItemResponse[] items = bulk.getItems();
                for (int i = 0; i < items.length; i++) {
                    if(items[i].getFailure() != null && items[i].getFailure().getStatus().equals(RestStatus.CONFLICT)){
                        saveOrUpdate(index, list);
                        break;
                    }
                }*/
            }
        } catch (Exception e) {
/*            if(!CollectionUtils.isEmpty(list) || count != list.size()) {
                saveOrUpdate(index, list);
            }*/
            log.info("当前contact游标：" + contact_cursorMark + ";发生异常 ：" + e);
        }
    }

    public String createId(Map<String, Object> doc){
        String id = null;
        try {
            String contact_account = doc.get("contact_account").toString();
            String unique_account = doc.get("unique_account").toString();
            String contact_type = doc.get("contact_type").toString();
            if ("3".equals(contact_type)) {
                id = String.format("m_%s_%s", contact_account, unique_account);
            } else {
                id = String.format("c_%s_%s", contact_account, unique_account);
            }
        }catch (Exception e){
            log.info("创建id异常");
        }
        return id;
    }

    public void saveOrUpdate(String index,List<Map<String, Object>> docs)  {
        if (CollectionUtils.isEmpty(docs)) {
            return;
        }
        int size = docs.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = docs.get(i);
            IndexRequest source;
            String id = createId(map);
            if(id == null){
                continue;
            }
            source = new IndexRequest(index, "_doc", id).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            try {
                IndexResponse indexResponse = restHighLevelClient.index(source, RequestOptions.DEFAULT);
                if(indexResponse.status().equals(RestStatus.CONFLICT)){
                    UpdateRequest doc = new UpdateRequest(index, "_doc", id).doc(map, XContentType.JSON);
                    try {
                        restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                    } catch (Exception ex) {
                        log.info("更新contact异常：",ex);
                    }
                }
            }catch (Exception e){
                UpdateRequest doc = new UpdateRequest(index, "_doc", id).doc(map, XContentType.JSON);
                try {
                    restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                } catch (IOException ex) {
                    log.info("更新contact异常：",e);
                }
            }
        }
    }

    @Override
    public SearchResponse queryMessageByTime(long start, long end) throws IOException {
        SearchRequest searchRequest = new SearchRequest(IndexConstant.SEARCH4MESSAGE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder messageTimeQuery = QueryBuilders.rangeQuery("message_time");
        messageTimeQuery.gte(start);
        messageTimeQuery.lt(end);
        searchSourceBuilder.query(messageTimeQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return search;
    }

    @Override
    public SearchResponse updateMessageByTime(long start, long end) throws IOException {
        SearchRequest searchRequest = new SearchRequest(IndexConstant.SEARCH4MESSAGE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder messageTimeQuery = QueryBuilders.rangeQuery("message_time");
        messageTimeQuery.gte(start);
        messageTimeQuery.lt(end);
        searchSourceBuilder.query(messageTimeQuery);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return search;
    }

    @Override
    public void setMessageSwitch1(boolean flag) {
        messageSwitch1 = flag;
    }

    @Override
    public void setMessageSwitch2(boolean flag) {
        messageSwitch2 = flag;
    }

    @Override
    public void setPause(boolean flag) {
        pause = flag;
    }

    @Override
    public void messageBatchUpdateByDate(Date start, Date end) throws IOException, InterruptedException {
        long hour = 3600 * 1L;
        long startTime = start.getTime() / 1000;
        long endTime = end.getTime() / 1000;
        long total = 0L;
        if (isJoin) {
            isJoin = false;
            new Thread(() -> SearchSolr.queryJointestByCursor()).start();
            Thread.sleep(10000);
        }

        while (startTime <= endTime) {
            while (pause){
                Thread.sleep(1000);
            }
            long tmp = startTime + hour;
/*            SearchResponse searchResponse = queryMessageByTime(startTime, tmp);
            if (searchResponse != null && searchResponse.getHits().getTotalHits().value != 0) {
                log.info("查询结果数为： " + searchResponse.getHits().getTotalHits().value
                        +" 跳过时间段：" + simpleDateFormat.format(new Date(startTime * 1000))
                        + " - " + simpleDateFormat.format(new Date(tmp * 1000)));
                startTime = tmp;
                continue;


            }*/
            QueryResponse queryResponse = SearchSolr.queryByTime(IndexConstant.SEARCH4MESSAGE, startTime, tmp);
            if(queryResponse == null || queryResponse.getResults().isEmpty()){
                startTime = tmp;
                continue;
            }
            SolrDocumentList results = queryResponse.getResults();
            long numFound = results.getNumFound();
            if(numFound < 40000){
                hour = hour + hour;
            }else if(numFound > 160000){
                hour = hour / 2;
                continue;
            } else if (numFound > 120000) {
                hour = (long) (hour * 0.75);
            }
            long maxSize = results.size() / 10000;
            SolrDocumentList finalResults1 = results;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .parallel()
                    .map(a -> finalResults1.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .forEach(list -> {
                        MessageRunable runableTest = new MessageRunable(IndexConstant.SEARCH4MESSAGE, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            int pageSize = 100000;
            int currSize = pageSize;
            if (numFound > currSize) {
                while (currSize < numFound) {
                    queryResponse = SearchSolr.queryByTime(IndexConstant.SEARCH4MESSAGE, startTime, startTime + hour, currSize);
                    results = queryResponse.getResults();
                    maxSize = results.size() / 10000;
                    SolrDocumentList finalResults2 = results;
                    Stream.iterate(0, n -> n + 1)
                            .limit(maxSize)
                            .map(a -> finalResults2.stream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                            .forEach(list -> {
                                MessageRunable runableTest = new MessageRunable(IndexConstant.SEARCH4MESSAGE, list);
                                threadPoolExecutor.execute(runableTest);
                            });
                    currSize += pageSize;
                }
            }
            startTime = tmp;
            total = total + numFound;
            log.info(simpleDateFormat.format(new Date(startTime * 1000)) + "-已处理：" + total);
        }
        log.info("处理完成");
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

    class MessageUpdateRunable implements Runnable {

        List<Map<String, Object>> results;
        String index;

        MessageUpdateRunable(String index, List<Map<String, Object>> results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            bulkUpdateMessageIndex(index, results);
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

    class ContactToEsRunable implements Runnable {

        List<Map<String, Object>> results;
        String index;

        ContactToEsRunable(String index, List<Map<String, Object>> results) {
            this.index = index;
            this.results = results;
        }

        @Override
        public void run() {
            try {
                bulkPutContactIndex2(index, results);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }


    String nextCursor = null;
    long total = 0L;

    public SearchResponse scrollContact(String index) throws IOException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchResponse searchResponse;
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        //修改为500MB
                        .HeapBufferedResponseConsumerFactory(500 * 1024 * 1024));
        if (StringUtils.isEmpty(nextCursor) || "*".equals(nextCursor)) {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery("contact_type",2));
            //查询第一页
            searchRequest.scroll(scroll);
            searchSourceBuilder.size(100);
            searchRequest.source(searchSourceBuilder);
            searchResponse = restHighLevelClient.search(searchRequest, builder.build());
        } else {
            //查询下一页
            SearchScrollRequest scrollRequest = new SearchScrollRequest(nextCursor);
            scrollRequest.scroll(scroll);
            searchResponse = restHighLevelClient.scroll(scrollRequest, builder.build());
        }
        return searchResponse;
    }

    public SearchResponse scrollMessage() throws IOException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchResponse searchResponse;
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        //修改为500MB
                        .HeapBufferedResponseConsumerFactory(500 * 1024 * 1024));
        if (StringUtils.isEmpty(nextCursor) || "*".equals(nextCursor)) {
            SearchRequest searchRequest = new SearchRequest(IndexConstant.SEARCH4MESSAGE);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.sort("gmt_create", SortOrder.DESC);
            //查询第一页
            searchRequest.scroll(scroll);
            searchSourceBuilder.size(10000);
            searchRequest.source(searchSourceBuilder);
            searchResponse = restHighLevelClient.search(searchRequest, builder.build());
        } else {
            //查询下一页
            SearchScrollRequest scrollRequest = new SearchScrollRequest(nextCursor);
            scrollRequest.scroll(scroll);
            searchResponse = restHighLevelClient.scroll(scrollRequest, builder.build());
        }
        return searchResponse;
    }
}
