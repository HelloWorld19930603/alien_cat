package com.aliencat.springboot.elasticsearch.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.time.Duration;

@Configuration
@Data
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {


    @Value("${spring.elasticsearch.rest.uris}")
    private String uris;

    @Value("${spring.elasticsearch.rest.uris2}")
    private String uris2;

    /**
     * 配置RestHighLevelClient对象
     * 将该对象交给Spring容器去管理
     *
     * @return RestHighLevelClient对象
     */
    @Override
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient elasticsearchClient() {
        HttpHost httpHost1 = new HttpHost("192.168.3.56",9200);
        HttpHost httpHost2 = new HttpHost("192.168.3.57",9200);
        HttpHost httpHost3 = new HttpHost("192.168.3.57",9201);
        HttpHost httpHost4 = new HttpHost("192.168.3.58",9200);
        HttpHost httpHost5 = new HttpHost("192.168.3.58",9201);
        //核心,配置超时时间,添加configCallback,之后创建连接时会将超时设置到socket上
        RestClientBuilder.RequestConfigCallback requestConfigCallback = requestConfigBuilder ->
                requestConfigBuilder.setSocketTimeout(28_800_000);
        RestClientBuilder rclientBuilder = RestClient.builder(httpHost1,httpHost2,httpHost3,httpHost4,httpHost5)
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis()))
                .setRequestConfigCallback(requestConfigCallback);
        return new RestHighLevelClient(rclientBuilder);
    }

    @Bean(name = "restHighLevelClient2")
    public RestHighLevelClient elasticsearchClient2() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(uris2.split(","))
                .withSocketTimeout(60000)
                .withConnectTimeout(20000)
                .build();
        return RestClients.create(configuration).rest();
    }

}

