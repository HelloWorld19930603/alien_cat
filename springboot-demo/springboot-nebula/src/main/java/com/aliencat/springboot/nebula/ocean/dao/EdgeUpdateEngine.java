package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeType;

import java.util.List;

/**
 * Description  EdgeUpdateEngine is used for
 * 目前边的更新只适合单类型的边，类似mysql的单表操作
 *
 * @author Anyzm
 * Date  2021/7/16 - 17:08
 * @version 1.0.0
 */
public interface EdgeUpdateEngine<S, T, E> extends GraphUpdateEngine {

    /**
     * 获取边实体
     *
     * @return
     */
    List<GraphEdgeEntity<S, T, E>> getGraphEdgeEntityList();

    /**
     * 获取边类型
     *
     * @return
     */
    GraphEdgeType<S, T, E> getGraphEdgeType();


}
