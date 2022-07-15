package com.aliencat.springboot.elasticsearch.service.impl;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import com.aliencat.springboot.elasticsearch.solr.SearchSolr;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
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

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
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
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

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
            contact_total = contact_total + results.size();
            //如果两次游标一样，说明数据拉取完毕，可以结束循环了
        } while (!contact_cursorMark.equals(nextCursor));
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
            System.out.println(simpleDateFormat.format(new Date()) + "发生异常 ：" + e);
            System.out.println("当前message游标1：" + message_cursorMark);
            System.out.println("当前message游标2：" + message_cursorMark2);
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
            String contact_type = map.get("contact_type").toString();
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
            System.out.println("当前contact游标：" + contact_cursorMark + ";发生异常 ：" + e);
        }
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
}
