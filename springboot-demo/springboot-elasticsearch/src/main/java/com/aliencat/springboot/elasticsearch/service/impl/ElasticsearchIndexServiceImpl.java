package com.aliencat.springboot.elasticsearch.service.impl;

import com.aliencat.springboot.elasticsearch.config.ThreadPoolConfig;
import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import com.aliencat.springboot.elasticsearch.service.RedisService;
import com.aliencat.springboot.elasticsearch.solr.SearchSolr;
import com.aliencat.springboot.elasticsearch.utils.EsUtil;
import com.aliencat.springboot.elasticsearch.utils.FileUtils;
import com.aliencat.springboot.elasticsearch.utils.TableToEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
@Lazy(false)
public class ElasticsearchIndexServiceImpl implements ElasticsearchIndexService {

    //es操作客户端
    //  private static final RestHighLevelClient restHighLevelClient2;
    //批量操作的对象
    private static BulkProcessor bulkProcessor;

    static {
/*        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200").withSocketTimeout(120000)
                .build();
        restHighLevelClient2 = RestClients.create(configuration).rest();*/
    }

    long message_total = 0L;
    long message_total2 = 0L;
    long contact_total = 0L;
    long message_time = 0L;
    long message_time2 = 0L;
    long contact_time = 0L;
    String message_cursorMark;
    ThreadLocal<String> message_cursorMark2 = new ThreadLocal<>();
    String contact_cursorMark;
    int qTime = 0;
    long esTime = 0;
    boolean messageSwitch1 = true;
    boolean messageSwitch2 = true;
    boolean pause = false;

    ThreadLocal<Thread> queryThread = new ThreadLocal<>();
    ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(30);
    ThreadPoolExecutor threadPoolExecutor;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    boolean isJoin = true;
    volatile boolean flag = true;
    String nextCursor = null;
    long total = 0L;
    @Autowired
    RedisService redisService;
    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private ThreadPoolConfig threadPoolConfig;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        QueryResponse queryResponse = SearchSolr.queryByCursorAsc(SearchSolr.getMessageClient(), CursorMarkParams.CURSOR_MARK_START);
        SolrDocumentList results = queryResponse.getResults();
        log.info(String.valueOf(results.size()));
        log.info(String.valueOf(queryResponse.getQTime()));
        log.info(String.valueOf((System.currentTimeMillis() - start) / 1000));
        int maxSize = results.size() / 10000;
        Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> results.parallelStream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .forEach(list -> {
                    log.info(String.valueOf(list.size()));
                });
    }

    @PreDestroy
    public void destroy() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
        }
    }

    @PostConstruct
    private void initExecutor() {
        if (threadPoolExecutor != null) {
            return;
        }
        synchronized (ElasticsearchIndexServiceImpl.class) {
            if (threadPoolExecutor != null) {
                return;
            }
            threadPoolExecutor = new ThreadPoolExecutor(
                    threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaxPoolSize(),
                    threadPoolConfig.getKeepAliveSeconds(), TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(threadPoolConfig.getQueueCapacity()),
                    (r, executor) -> {
                        if (!executor.isShutdown()) {
                            try {
                                log.info("队列已满，等待重新入队-" + Thread.currentThread().getName());
                                Thread.sleep(10000);
                                executor.getQueue().put(r);
                            } catch (InterruptedException e) {
                                log.info("executor:", e);
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
        }
        log.info("线程池创建完毕");
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
        log.info(out);
        return out;
    }

    @Override
    public String getContactProcess() {
        return "处理" + contact_total + "条message数据耗时：" + (System.currentTimeMillis() - contact_time) / 1000 + "s ";
    }

    @Override
    public boolean createIndex(String index, String mapping, int shards) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.mapping(mapping, XContentType.JSON);
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards", shards).build());
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        log.info(String.valueOf(createIndexResponse.isAcknowledged()));
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
        log.info("是否删除成功：" + response.isAcknowledged());
        return response.isAcknowledged();
    }

    @Override
    public void messageBatchUpdate(String cursorMark) {
        message_time = System.currentTimeMillis();
        log.info("message批处理程序启动：" + simpleDateFormat.format(new Date(message_time)));
        log.info("cursorMark：" + cursorMark);
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
                log.info(simpleDateFormat.format(new Date()) + " 已处理message数量：" + message_total);
                //如果两次游标一样，说明数据拉取完毕，可以结束循环了
            } while (messageSwitch1 && !message_cursorMark.equals(nextCursor) && (message_total + message_total2) < queryResponse.getResults().getNumFound());
            message_time = (System.currentTimeMillis() - message_time) / 1000;
            log.info("降序处理" + message_total + "条message数据总耗时：" + message_time + "s ");
            log.info("message批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
            redisService.del("search4jointest");
        } catch (Exception e) {
            e.printStackTrace();
            messageBatchUpdate(cursorMark);
        }
    }

    @Override
    public void messageBatchUpdateAsc(Date start, Date end) {
        message_time2 = System.currentTimeMillis();
        log.info("message 升序批处理程序启动：" + simpleDateFormat.format(new Date(message_time2)));
        long startTime = start.getTime() / 1000;
        long endTime = end.getTime() / 1000;
        String index = IndexConstant.SEARCH4MESSAGE;
        String nextCursor = CursorMarkParams.CURSOR_MARK_START;//游标初始化
        QueryResponse queryResponse = null;
        try {
            do {
                while (pause) {
                    Thread.sleep(10000);
                }
                message_cursorMark2.set(nextCursor);
                try {
                    queryResponse = SearchSolr.queryByCursorAsc(SearchSolr.getMessageClient(), nextCursor, startTime, endTime);
                    nextCursor = queryResponse.getNextCursorMark();
                } catch (Exception e) {
                    log.info("solr查询异常", e);
                    continue;
                }
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
                log.info(simpleDateFormat.format(new Date()) + " 已处理message2数量：" + message_total2);
                //如果两次游标一样，说明数据拉取完毕，可以结束循环了
            } while (messageSwitch2 && !message_cursorMark2.get().equals(nextCursor) && (message_total + message_total2) < queryResponse.getResults().getNumFound());
            message_time2 = (System.currentTimeMillis() - message_time2) / 1000;
            log.info("升序处理" + message_total2 + "条message2数据总耗时：" + message_time2 + "s ");
            log.info("message2批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
            //redisService.del("search4jointest");
        } catch (Exception e) {
            e.printStackTrace();
            //messageBatchUpdate(nextCursor);
        }
    }

    /**
     * 更新es message索引中的落地信息
     *
     * @param cursorMark
     */
    @Override
    public void messageBatchUpdate3(String cursorMark) {
        contact_time = System.currentTimeMillis();
        log.info("messageBatchUpdate3批处理程序启动：" + simpleDateFormat.format(new Date(contact_time)));
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
                log.debug("nextCursor: {}", nextCursor);
            } catch (IOException e) {
                log.info("分页异常：", e);
            }
            List<Map<String, Object>> results = Stream.of(searchResponse.getHits().getHits())
                    .map(hit -> {
                        Map<String, Object> map = hit.getSourceAsMap();
                        map.put("_id", hit.getId());
                        return map;
                    }).collect(Collectors.toList());
            qTime = (int) searchResponse.getTook().getMillis();
            MessageUpdateRunable runableTest = new MessageUpdateRunable(index, results);
            threadPoolExecutor.execute(runableTest);
            size = results.size();
            message_total = message_total + size;
            log.info("已处理" + message_total + "条contact数据,耗时：" + (System.currentTimeMillis() - start) + "ms ");
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (flag);
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        log.info("处理" + message_total + "条message数据总耗时：" + contact_time + "s ");
        log.info("message批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void contactBatchUpdate(String cursorMark) {
        contact_time = System.currentTimeMillis();
        log.info("contact批处理程序启动：" + simpleDateFormat.format(new Date(contact_time)));
        String index = IndexConstant.SEARCH4CONTACT;
        String nextCursor = Optional.ofNullable(cursorMark).orElse(CursorMarkParams.CURSOR_MARK_START);//游标初始化
        do {
            contact_cursorMark = nextCursor;
            QueryResponse queryResponse = SearchSolr.queryByCursorAsc(SearchSolr.getContactClient(), contact_cursorMark);
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
            contact_total = contact_total + results.size();
            log.info("已处理" + contact_total + "条数据");
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!contact_cursorMark.equals(nextCursor));
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        log.info("处理" + contact_total + "条contact数据总耗时：" + contact_time + "s ");
        log.info("contact批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void contactTransferEs(String fromIndex, String toIndex) {
        contact_time = System.currentTimeMillis();
        log.info("contact迁移程序启动：" + simpleDateFormat.format(new Date(contact_time)));
        int size;
        do {
            long start = System.currentTimeMillis();
            contact_cursorMark = nextCursor;
            SearchResponse searchResponse = null;
            try {
                searchResponse = scrollContact(fromIndex);
                total = total + searchResponse.getHits().getTotalHits().value;
                nextCursor = searchResponse.getScrollId();
                log.debug("nextCursor: {}", nextCursor);
            } catch (IOException e) {
                log.info("分页异常：", e);
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
            log.info("已处理" + contact_total + "条contact数据,耗时：" + (System.currentTimeMillis() - start) + "ms ");
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (size != 0);
        contact_time = (System.currentTimeMillis() - contact_time) / 1000;
        log.info("处理" + contact_total + "条contact数据总耗时：" + contact_time + "s ");
        log.info("contact批处理程序结束时间：" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
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
            map.remove("recipient_account_id");
            map.remove("contact_account_id");
            map.remove("sender_account_id");
            String esId = EsUtil.MD5(map.get("sender_account").toString() + map.get("recipient_account")
                    + map.get("message_time") + map.get("message_type"));
            IndexRequest source = new IndexRequest(index).id(esId).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info(simpleDateFormat.format(new Date()) + "发生异常 ：", e);
        }
    }

    public void bulkUpdateMessageIndex(String index, List<Map<String, Object>> list) {
        bulkUpdateMessageIndex(index, list, true);
    }

    public void bulkUpdateMessageIndex(String index, List<Map<String, Object>> list, boolean isRetry) {
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
                map.put("country", jointest.get("country"));
                Object province = jointest.get("province");
                if (!StringUtils.isEmpty(province) || !"-".equals(province)) {
                    map.put("province", province);
                }
                Object city = jointest.get("city");
                if (!StringUtils.isEmpty(city) || !"-".equals(city)) {
                    map.put("city", city);
                }
            }
            map.remove("_version_");
            map.remove("recipient_account_id");
            map.remove("contact_account_id");
            map.remove("sender_account_id");
            String esId = EsUtil.MD5(map.get("sender_account").toString() + map.get("recipient_account")
                    + map.get("message_time") + map.get("message_type") + map.get("message_content"));
            IndexRequest source = new IndexRequest(index, "_doc",esId).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.INDEX);
            request.add(source);
        }
        try {
            BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
            long count = 0;
            if (bulk.hasFailures()) {
                BulkItemResponse[] items = bulk.getItems();
                count = Arrays.stream(items).filter(BulkItemResponse::isFailed).count();
                // Optional<BulkItemResponse> first = Arrays.stream(items).filter(BulkItemResponse::isFailed).findFirst();
                BulkItemResponse item = items[0];
                if (item != null && item.isFailed()) {
                    if (RestStatus.TOO_MANY_REQUESTS.equals(item.getFailure().getStatus())) {
                        Thread.sleep(20000);
                        MessageUpdateRunable runableTest = new MessageUpdateRunable(index, list);
                        threadPoolExecutor.execute(runableTest);
                    }
                    log.info("es返回的错误：" + item.getFailureMessage());
                }
/*                Map<String, Object> map = list.get(0);
                map.entrySet().stream().forEach(e->{
                    log.info(e.getKey() + " : " + e.getValue());
                });*/
                if (count > 0) {
                    List<Map<String, Object>> failedCollect = Arrays.stream(items).map(bulkItemResponse ->
                            list.get(bulkItemResponse.getItemId())).collect(Collectors.toList());
                    StringBuilder stringBuilder = new StringBuilder();
                    failedCollect.forEach(map -> {
                        stringBuilder.append(map.get("id")).append(",");
                    });
                    FileUtils.saveAsFileWriter("/usr/solr2es/ids.txt", stringBuilder.toString(), true);
                    Thread.sleep(5000);
                }
            }
            if (isRetry) {
                log.info("ES耗时：" + bulk.getTook().getMillis() + ",failed:" + count);
            } else {
                log.info("重试后-ES耗时：" + bulk.getTook().getMillis() + ",failed:" + count);
            }

        } catch (Exception e) {
            log.info(simpleDateFormat.format(new Date()) + "发生异常 ：", e);
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
            String id = createContactId(map);
            map.remove("id");
            map.remove("unique_account_id");
            map.remove("contact_account_id");
            map.remove("_version_");
            IndexRequest source = new IndexRequest(index).id(id).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("当前contact游标：" + contact_cursorMark + ";发生异常 ：", e);
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
            if (map.get("gmt_create") == null && ex_k_updateTime != null) {
                Date updateTime;
                if (ex_k_updateTime instanceof String) {
                    updateTime = simpleDateFormat.parse(ex_k_updateTime.toString());
                } else {
                    updateTime = (Date) map.get("ex_k_updateTime");
                }
                map.put("gmt_create", updateTime.getTime() / 1000);
                map.put("ex_k_updateTime", updateTime);
            }
            String id = createContactId(map);
            if (id == null) {
                count++;
                continue;
            }
            map.remove("id");
            map.remove("unique_account_id");
            map.remove("contact_account_id");
            map.remove("_version_");
            IndexRequest source = new IndexRequest(index).id(id).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            request.add(source);
        }
        BulkResponse bulk = null;
        try {
            if (!CollectionUtils.isEmpty(list) || count != list.size()) {
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

    /**
     * 创建contact索引id
     *
     * @param doc
     * @return
     */
    public String createContactId(Map<String, Object> doc) {
        String id = null;
        try {
            String contact_account = doc.get("contact_account").toString();
            String unique_account = doc.get("unique_account").toString();
            String contact_type = doc.get("contact_type").toString();
            if ("3".equals(contact_type)) {
                id = String.format("m_%s_%s", unique_account, contact_account);
            } else {
                id = String.format("c_%s_%s", contact_account, unique_account);
            }
        } catch (Exception e) {
            log.info("创建contact索引id异常");
        }
        return id;
    }

    public void saveOrUpdateContact(String index, List<Map<String, Object>> docs) {
        if (CollectionUtils.isEmpty(docs)) {
            return;
        }
        int size = docs.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = docs.get(i);
            IndexRequest source;
            String id = createContactId(map);
            if (id == null) {
                continue;
            }
            source = new IndexRequest(index, "_doc", id).source(map, XContentType.JSON);
            source.opType(DocWriteRequest.OpType.CREATE);
            try {
                IndexResponse indexResponse = restHighLevelClient.index(source, RequestOptions.DEFAULT);
                if (indexResponse.status().equals(RestStatus.CONFLICT)) {
                    UpdateRequest doc = new UpdateRequest(index, "_doc", id).doc(map, XContentType.JSON);
                    try {
                        restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                    } catch (Exception ex) {
                        log.info("更新contact异常：", ex);
                    }
                }
            } catch (Exception e) {
                UpdateRequest doc = new UpdateRequest(index, "_doc", id).doc(map, XContentType.JSON);
                try {
                    restHighLevelClient.update(doc, RequestOptions.DEFAULT);
                } catch (IOException ex) {
                    log.info("更新contact异常：", e);
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

    @PostConstruct
    public void queryJointest() {
/*        Object search4jointest = redisService.get("search4jointest");
        if (search4jointest == null && isJoin) {
            isJoin = false;
            SearchSolr.queryJointestByCursor();
            redisService.set("search4jointest",SearchSolr.jointestMap,3600 * 12);
        }else{
            SearchSolr.jointestMap = (Map<String, SolrDocument>) search4jointest;
        }*/
        log.info("初始化落地信息");
        if (isJoin) {
            isJoin = false;
            new Thread(() -> SearchSolr.queryJointestByCursor()).start();
        }
    }

    @Override
    public void messageBatchUpdateByDate(Date start, Date end) throws IOException, InterruptedException {
        long hour = 3600 * 1L;
        long startTime = start.getTime() / 1000;
        long endTime = end.getTime() / 1000;
        long total = 0L;

        while (startTime <= endTime) {
            while (pause) {
                Thread.sleep(10000);
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
            if (queryResponse == null) {
                hour = hour / 2;
                continue;
            } else if (queryResponse.getResults().isEmpty()) {
                hour = hour + hour;
                continue;
            }
            SolrDocumentList results = queryResponse.getResults();
            long numFound = results.getNumFound();
            if (numFound < 40000) {
                hour = hour + hour;
            } else if (numFound < 75000) {
                hour = (long) (hour * 1.2);
            } else if (numFound > 160000) {
                hour = hour / 2;
                continue;
            } else if (numFound > 120000) {
                hour = (long) (hour * 0.7);
                continue;
            } else if (numFound > 100000) {
                hour = (long) (hour * 0.8);
                continue;
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
        //redisService.del("search4jointest");
    }

    private Date currentDate;
    @Override
    public void messageBatchUpdateByDateFromMysql(Date start, Date end) {
        if(currentDate != null){
            start = currentDate;
        }
        long hour = 3600 * 1000L;
        long startTime = start.getTime();
        long endTime = end.getTime();
        long total = 0L;
        Thread.currentThread().setName("messageBatchUpdateByDateFromMysql");
        while (startTime <= endTime) {
            if (pause) {
                break;
            }
            long tmp = startTime + hour;
            String startDate = simpleDateFormat.format(new Date(startTime));
            String tmpDate = simpleDateFormat.format(new Date(tmp));
            List<Map<String, Object>> messageMaps = TableToEntityUtils.getMessageMaps(startDate
                    , tmpDate, null);
            int size = messageMaps.size();
            log.info("当前size:" + size + " ,start from " + startDate + " to " + tmpDate);
            if (size < 80000) {
                hour = hour * 2;
                continue;
            } else if (size > 200000) {
                hour = hour / 2;
            }
            long numFound = size;
            long maxSize = numFound / 10000;
            List<Map<String, Object>> finalResults1 = messageMaps;
            Stream.iterate(0, n -> n + 1)
                    .limit(maxSize)
                    .map(a -> finalResults1.stream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                    .forEach(list -> {
                        MessageUpdateRunable runableTest = new MessageUpdateRunable(IndexConstant.SEARCH4MESSAGE, list);
                        threadPoolExecutor.execute(runableTest);
                    });
            startTime = tmp;
            total = total + numFound;
            log.info(simpleDateFormat.format(new Date(startTime)) + "-已处理：" + total);
            currentDate = new Date(startTime);
        }
        TableToEntityUtils.close();
        log.info("处理完成: " + simpleDateFormat.format(start) + " - " + simpleDateFormat.format(end));
    }

    @Override
    public void messageBatchUpdateByContactFromMysql(String contact) {
        long total = 0L;
        Thread.currentThread().setName("messageBatchUpdateByContactFromMysql");
        List<Map<String, Object>> messageMaps = TableToEntityUtils.getMessageMaps(null
                , null, contact);
        int size = messageMaps.size();
        long numFound = size;
        log.info("size:" + size);
        long maxSize = (long) Math.ceil(numFound / 10000);
        List<Map<String, Object>> finalResults1 = messageMaps;
        Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .map(a -> finalResults1.stream().skip(a * 10000).limit(10000).collect(Collectors.toList()))
                .forEach(list -> {
                    MessageUpdateRunable runableTest = new MessageUpdateRunable(IndexConstant.SEARCH4MESSAGE, list);
                    threadPoolExecutor.execute(runableTest);
                });
        total = total + numFound;
        log.info("已处理：" + total);
        TableToEntityUtils.close();
        log.info("处理完成: " + contact);
    }

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
            //searchSourceBuilder.query(QueryBuilders.termQuery("contact_type",2));
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
}
