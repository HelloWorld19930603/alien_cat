package com.aliencat.springboot.elasticsearch.service;

import com.aliencat.springboot.elasticsearch.pojo.Order;
import com.aliencat.springboot.elasticsearch.pojo.PageResponse;

import java.util.List;

public interface OrderService {
    void saveAll(List<Order> orders);

    Order findById(Integer id);

    void deleteById(Integer id);

    void updateById(Order order);

    PageResponse<Order> findList(Order order, Integer pageIndex, Integer pageSize);

    PageResponse<Order> findAll(Integer pageIndex, Integer pageSize);

    PageResponse<Order> findHighlight(Order order, Integer pageIndex, Integer pageSize);
}

