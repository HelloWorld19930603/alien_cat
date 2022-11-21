
package com.aliencat.springboot.nebula.ocean.engine;


import com.aliencat.springboot.nebula.ocean.dao.GraphUpdateVertexEngineFactory;
import com.aliencat.springboot.nebula.ocean.dao.VertexUpdateEngine;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexEntity;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Description  NebulaUpdateVertexEngineFactory is used for
 *
 * @author Anyzm
 * Date  2021/7/19 - 10:52
 * @version 1.0.0
 */
@Slf4j
public class NebulaUpdateVertexEngineFactory implements GraphUpdateVertexEngineFactory {

    @Override
    public <T> VertexUpdateEngine build(List<GraphVertexEntity<T>> graphVertexEntities) throws NebulaException {
        return new NebulaBatchVertexUpdate<>(graphVertexEntities);
    }

}
