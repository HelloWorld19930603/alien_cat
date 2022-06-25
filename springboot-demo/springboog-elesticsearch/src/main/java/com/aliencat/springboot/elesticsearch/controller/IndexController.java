package com.aliencat.springboot.elesticsearch.controller;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/index/")
@RestController
public class IndexController {

    @Resource(name = "elasticsearchRestTemplate")
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource(name = "restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    @GetMapping("create")
    public String create(@RequestParam String indexName) {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName));
        if (indexOperations.exists()) {
            return "索引已存在";
        }
        indexOperations.create();
        return "索引创建成功";
    }

    /**
     * 删除索引
     */
    @GetMapping("delete")
    public String delete(@RequestParam String indexName) {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName));
        indexOperations.delete();
        return "索引删除成功";
    }

    public void search(@RequestParam String query){
    }
}

