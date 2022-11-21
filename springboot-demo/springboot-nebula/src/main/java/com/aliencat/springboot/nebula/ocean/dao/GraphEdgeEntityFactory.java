
package com.aliencat.springboot.nebula.ocean.dao;


import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

/**
 * @author Anyzm
 * @Description:
 * @Date: Created in 20:00 2021/7/18
 */
public interface GraphEdgeEntityFactory {

    /**
     * 构建GraphEdgeEntity
     *
     * @param input
     * @param <S>
     * @param <T>
     * @param <E>
     * @return
     * @throws NebulaException
     */
    public <S, T, E> GraphEdgeEntity<S, T, E> buildGraphEdgeEntity(E input) throws NebulaException;

}
