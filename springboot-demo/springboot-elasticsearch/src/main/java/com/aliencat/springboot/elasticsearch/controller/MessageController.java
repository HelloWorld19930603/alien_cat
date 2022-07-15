package com.aliencat.springboot.elasticsearch.controller;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/message/")
@RestController
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
        return "索引是否创建成功:" + elasticsearchIndexService.createIndex(indexName, mapping);
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

    @RequestMapping("batch")
    public String batch(String cursor) {
        if (flag) {
            return "message批量程序已启动\n";
        }
        new Thread(() -> elasticsearchIndexService.messageBatchUpdate(cursor)).start();
        flag = true;
        return "OK";
    }

    @RequestMapping("batch2")
    public String batch2(String cursor) {
        if (flag2) {
            return "message批量程序2已启动\n";
        }
        new Thread(() -> elasticsearchIndexService.messageBatchUpdate2(cursor)).start();
        flag2 = true;
        return "OK\n";
    }

    @RequestMapping("process")
    public String process() {
        return elasticsearchIndexService.getMessageProcess();
    }


    @RequestMapping("switch1")
    public String switch1(boolean flag) {
        elasticsearchIndexService.setMessageSwitch1(flag);
        return "ok";
    }

    @RequestMapping("switch2")
    public String switch2(boolean flag) {
        elasticsearchIndexService.setMessageSwitch2(flag);
        return "ok";
    }

    @RequestMapping("pause")
    public String pause(boolean flag) {
        elasticsearchIndexService.setPause(flag);
        return "ok";
    }
}

