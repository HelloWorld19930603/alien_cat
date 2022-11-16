package com.aliencat.springboot.elasticsearch.controller;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RequestMapping("/message/")
@RestController
@Slf4j
public class MessageController {

    private boolean flag = false;
    private boolean flag2 = false;


    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * 创建索引
     */
    @RequestMapping("create")
    public String create() throws IOException {
        String indexName = IndexConstant.SEARCH4MESSAGE;
        String mapping = IndexConstant.SEARCH4MESSAGE_MAPPING;
        return "索引是否创建成功:" + elasticsearchIndexService.createIndex(indexName, mapping,200);
    }

    /**
     * 删除索引
     */
    @RequestMapping("delete")
    public String delete() throws IOException {
        String indexName = IndexConstant.SEARCH4MESSAGE;
        String mapping = IndexConstant.SEARCH4MESSAGE_MAPPING;
        return "索引是否删除成功：" + elasticsearchIndexService.deleteIndex(indexName, mapping);
    }

    /**
     * 降序查询
     * @param cursor
     * @return
     */
    @RequestMapping("batch")
    public String batch(String cursor) {
        if (flag) {
            return "message批量程序已启动\n";
        }
        new Thread(() -> elasticsearchIndexService.messageBatchUpdate(cursor)).start();
        flag = true;
        return "OK\n";
    }

    /**
     * 升序查询
     * @return
     */
    @RequestMapping("batchAsc")
    public String batch2(String start, String end) {

        log.info("message批量程序2已启动 - start:" + start + "\nend:" + end);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
            new Thread(() -> {
                try {
                    new Thread(() -> elasticsearchIndexService.messageBatchUpdateAsc(startDate,endDate)).start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
            return "发生异常：" + e;
        }
        return "OK\n";
    }

    /**
     * 按日期批量处理消息
     * @param start
     * @param end
     * @return
     */
    @RequestMapping("batchByDate")
    public String batchByDate(String start, String end) {
        log.info("start:" + start + "\nend:" + end);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
            new Thread(() -> {
                try {
                    elasticsearchIndexService.messageBatchUpdateByDate(startDate,endDate);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
            return "发生异常：" + e;
        }
        return "ok\n";
    }

    @RequestMapping("batchByDateFromMysql")
    public String batchByDateFromMysql(String start, String end,String[] contact) {
        log.info("start:" + start + "\nend:" + end);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date startDate = start != null ? simpleDateFormat.parse(start) : null;
            Date endDate = end != null ? simpleDateFormat.parse(end) : null;
            new Thread(() -> {
                try {
                    elasticsearchIndexService.messageBatchUpdateByDateFromMysql(startDate,endDate);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }catch(Exception e){
            log.info("batchByDateFromMysql异常:",e);
            return "发生异常：" + e;
        }
        return "ok\n";
    }

    @RequestMapping("batchByContactFromMysql")
    public String batchByContactFromMysql(String start, String end,String contact) {
        log.info("contact:" + contact);
        if(StringUtils.isEmpty(contact)){
            return "empty";
        }
        try {
            new Thread(() -> {
                try {
                    elasticsearchIndexService.messageBatchUpdateByContactFromMysql(contact);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }catch(Exception e){
            log.info("batchByContactFromMysql异常:",e);
            return "发生异常：" + e;
        }
        return "ok\n";
    }

    @RequestMapping("queryMessageByTime")
    public String queryMessageByTime(String start, String end) {
        log.info("start:" + start + "\nend:" + end);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
            SearchResponse searchResponse = elasticsearchIndexService.queryMessageByTime(startDate.getTime() / 1000, endDate.getTime() / 1000);
            return "查询结果：" + searchResponse.getHits().getTotalHits().value;
        }catch(Exception e){
            e.printStackTrace();
            return "发生异常：" + e;
        }
    }


    @RequestMapping("process")
    public String process() {
        return elasticsearchIndexService.getMessageProcess();
    }


    @RequestMapping("switch1")
    public String switch1(boolean flag) {
        elasticsearchIndexService.setMessageSwitch1(flag);
        return "ok\n";
    }

    @RequestMapping("switch2")
    public String switch2(boolean flag) {
        elasticsearchIndexService.setMessageSwitch2(flag);
        return "ok\n";
    }

    @RequestMapping("pause")
    public String pause(boolean flag) {
        elasticsearchIndexService.setPause(flag);
        return "ok\n";
    }

}

