package com.aliencat.springboot.nebula.ocean.dto;


import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * AlgorithmEdge 算法边
 */
@Data
@GraphEdge(value = "AlgorithmEdge", srcVertex = AccountTag.class, dstVertex = AccountTag.class)
public class AlgorithmEdge {
    @GraphProperty(value = "fromId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)
    private String fromId;

    @GraphProperty(value = "toId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)
    private String toId;

    /**
     * num
     */
    @GraphProperty(value = "num", required = true, dataType = GraphDataTypeEnum.DOUBLE)
    private double num;
}
