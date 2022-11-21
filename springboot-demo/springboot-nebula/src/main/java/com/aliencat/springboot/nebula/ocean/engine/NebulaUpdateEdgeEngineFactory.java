
package com.aliencat.springboot.nebula.ocean.engine;

import com.aliencat.springboot.nebula.ocean.dao.EdgeUpdateEngine;
import com.aliencat.springboot.nebula.ocean.dao.GraphUpdateEdgeEngineFactory;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphEdgeEntity;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Description  NebulaUpdateEdgeEngineFactory is used for
 *
 * @author Anyzm
 * Date  2021/7/19 - 10:52
 * @version 1.0.0
 */
@Slf4j
public class NebulaUpdateEdgeEngineFactory implements GraphUpdateEdgeEngineFactory {

    @Override
    public <S, T, E> EdgeUpdateEngine<S, T, E> build(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities) throws NebulaException {
        return new NebulaBatchEdgesUpdate<>(graphEdgeEntities);
    }

    @Override
    public <S, T, E> EdgeUpdateEngine<S, T, E> build(List<GraphEdgeEntity<S, T, E>> graphEdgeEntities,
                                                     List<GraphVertexEntity<S>> srcGraphVertexEntities,
                                                     List<GraphVertexEntity<T>> dstGraphVertexEntities) throws NebulaException {
        return new NebulaBatchEdgesUpdate<>(graphEdgeEntities, srcGraphVertexEntities, dstGraphVertexEntities);
    }

}
