package com.aliencat.springboot.elesticsearch.controller;

import com.aliencat.springboot.elesticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elesticsearch.service.ElasticsearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/message/")
@RestController
public class MessageController {


    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * 创建索引
     */
    @GetMapping("create")
    public String create() throws IOException {
        String indexName = IndexConstant.SEARCH4MESSAGE;
        String mapping = IndexConstant.SEARCH4MESSAGE_MAPPING;
        return "索引是否创建成功:" + elasticsearchIndexService.deleteIndex(indexName, mapping);
    }

    /**
     * 删除索引
     */
    @GetMapping("delete")
    public String delete() throws IOException {
        String indexName = IndexConstant.SEARCH4MESSAGE;
        String mapping = IndexConstant.SEARCH4MESSAGE_MAPPING;
        return "索引是否删除成功：" + elasticsearchIndexService.createIndex(indexName, mapping);
    }

    @RequestMapping("batch")
    public String batch() {
        new Thread(()->elasticsearchIndexService.messageBatchUpdate());
        return "OK";
    }
}

