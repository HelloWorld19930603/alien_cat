package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;

import java.util.List;

/**
 * Description  VertexUpdateEngine is used for
 * 目前顶点的更新，只适合单类型的vertex type，即单个tag，类似mysql的单表操作
 *
 * @author Anyzm
 * Date  2021/7/16 - 17:10
 * @version 1.0.0
 */
public interface VertexUpdateEngine extends GraphUpdateEngine {

    /**
     * 获取顶点实体
     *
     * @return
     */
    <T> List<GraphVertexEntity<T>> getGraphVertexEntityList();

    /**
     * 获取顶点类型
     *
     * @return
     */
    <T> GraphVertexType<T> getGraphVertexType();

}
