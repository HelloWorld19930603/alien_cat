package com.aliencat.springboot.elesticsearch.dao;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public interface EsArticleDao {

    /**
     * 搜索文章
     *
     * @param query      条件过滤器
     * @param sortValues search_after 搜索游标
     * @param sortOrder  排序对象
     * @param size       每页大小
     * @return {@link SearchResponse}
     * @throws IOException ES引起的IO异常
     */
    SearchResponse searchArticle(QueryBuilder query,
                                 Object[] sortValues,
                                 SortOrder sortOrder,
                                 int size, int sortType) throws IOException;

}
