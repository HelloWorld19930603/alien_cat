package com.aliencat.springboot.elesticsearch.repository;

import com.aliencat.springboot.elesticsearch.pojo.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderRepository extends ElasticsearchRepository<Order, Integer> {
}
