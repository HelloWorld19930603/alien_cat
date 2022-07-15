package com.aliencat.springboot.elasticsearch.controller;

import com.aliencat.springboot.elasticsearch.pojo.IndexConstant;
import com.aliencat.springboot.elasticsearch.service.ElasticsearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/contact/")
@RestController
public class ContactController {


    private boolean flag = false;
    @Autowired
    private ElasticsearchIndexService elasticsearchIndexService;

    /**
     * 创建索引
     */
    @RequestMapping("create")
    public String create() throws IOException {
        String indexName = IndexConstant.SEARCH4CONTACT;
        String mapping = IndexConstant.SEARCH4CONTACT_MAPPING;
        return "索引是否创建成功:" + elasticsearchIndexService.createIndex(indexName, mapping);
    }

    /**
     * 删除索引
     */
    @RequestMapping("delete")
    public String delete() throws IOException {
        String indexName = IndexConstant.SEARCH4CONTACT;
        String mapping = IndexConstant.SEARCH4CONTACT_MAPPING;
        return "索引是否删除成功：" + elasticsearchIndexService.deleteIndex(indexName, mapping);
    }

    @RequestMapping("batch")
    public String batch(String cursor) {
        if(flag){
            return "contact批量程序已启动";
        }
        new Thread(()->elasticsearchIndexService.contactBatchUpdate(cursor)).start();
        flag = true;
        return "OK";
    }


    @RequestMapping("process")
    public String process() {

        return elasticsearchIndexService.getContactProcess();
    }
}

