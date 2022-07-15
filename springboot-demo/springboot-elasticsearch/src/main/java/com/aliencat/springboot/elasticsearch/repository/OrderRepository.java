package com.aliencat.springboot.elasticsearch.repository;

import com.aliencat.springboot.elasticsearch.pojo.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderRepository extends ElasticsearchRepository<Order, Integer> {
}
