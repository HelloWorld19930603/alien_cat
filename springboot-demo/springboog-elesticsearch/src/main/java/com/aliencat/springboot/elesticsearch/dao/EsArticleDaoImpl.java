package com.aliencat.springboot.elesticsearch.dao;

import com.aliencat.springboot.elesticsearch.config.ElasticSearchConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EsArticleDaoImpl implements EsArticleDao {

    private final RestHighLevelClient restHighLevelClient;
    @Autowired
    private ElasticSearchConfig elasticSearchConfig;

    @Override
    public SearchResponse searchArticle(QueryBuilder query,
                                        Object[] sortValues,
                                        SortOrder sortOrder,
                                        int size, int sortType) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.size(size);
        if (Objects.nonNull(sortValues)) {
            searchSourceBuilder.searchAfter(sortValues);
        }
        SearchRequest searchRequest = new SearchRequest(elasticSearchConfig.getArticleIndex());
        searchRequest.source(searchSourceBuilder);
        log.info("搜索文章ES条件：\n {}", searchSourceBuilder.toString());
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return response;
    }


}
