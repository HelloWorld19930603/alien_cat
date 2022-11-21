
package com.aliencat.springboot.nebula.ocean.dao.impl;


import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.common.GraphHelper;
import com.aliencat.springboot.nebula.ocean.dao.GraphVertexTypeFactory;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexType;
import com.aliencat.springboot.nebula.ocean.domain.impl.GraphVertexTypeBuilder;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.exception.NebulaException;

/**
 * Description  NebulaGraphVertexTypeFactory is used for
 *
 * @author Anyzm
 * Date  2021/7/16 - 16:41
 * @version 1.0.0
 */
public class DefaultGraphVertexTypeFactory implements GraphVertexTypeFactory {

    @Override
    public <T> GraphVertexType<T> buildGraphVertexType(Class<T> clazz) throws NebulaException {
        GraphVertex graphVertex = clazz.getAnnotation(GraphVertex.class);
        if (graphVertex == null) {
            return null;
        }
        String vertexName = graphVertex.value();
        //主键策略：hash uuid string
        GraphKeyPolicy graphKeyPolicy = graphVertex.keyPolicy();
        boolean idAsField = graphVertex.idAsField();
        GraphVertexTypeBuilder builder = GraphVertexTypeBuilder.builder();
        GraphHelper.collectGraphProperties(builder, clazz, idAsField, idAsField);
        return builder.graphKeyPolicy(graphKeyPolicy).idAsField(idAsField).graphLabelName(vertexName).labelClass(clazz).build();
    }

}
