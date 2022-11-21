package com.aliencat.springboot.nebula.ocean.dto;

import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 社交帐号职务关系的边
 */
@Data
@GraphEdge(value = "SocialAccountPostEdge", srcVertex = AccountTag.class, dstVertex = AccountTag.class)
public class SocialAccountPostEdge {
    @GraphProperty(value = "fromId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)
    private String fromId;

    @GraphProperty(value = "toId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)
    private String toId;

    /**
     * 职务名称
     */
    @GraphProperty(value = "titleName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String titleName;

    /**
     * 状态
     */
    @GraphProperty(value = "state", required = true, dataType = GraphDataTypeEnum.BOOL)
    private Boolean state;

    public SocialAccountPostEdge(String fromId, String toId, String titleName,Boolean state) {
        this.fromId = fromId;
        this.toId = toId;
        this.titleName = titleName;
        this.state=state;
    }
}
