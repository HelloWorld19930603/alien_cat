package com.aliencat.springboot.nebula.ocean.dto;

import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 社交帐号的点表
 */
@GraphVertex(value = "pagerank", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class PagerankTag {
    /**
     * 帐号生成的uuid.
     */
    @GraphProperty(value = "id", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_VERTEX_ID)
    private String id;

    /**
     * pagerank
     */
    @GraphProperty(value = "pagerank", required = true, dataType = GraphDataTypeEnum.DOUBLE)
    private Double pagerank;
}