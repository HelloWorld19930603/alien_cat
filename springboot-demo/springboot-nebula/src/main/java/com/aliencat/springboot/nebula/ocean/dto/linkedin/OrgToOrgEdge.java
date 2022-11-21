package com.aliencat.springboot.nebula.ocean.dto.linkedin;

import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 组织与组织之间的关系
 */
@Data
@GraphEdge(value = "OrgToOrgEdge", srcVertex = OrganizationTag.class, dstVertex = OrganizationTag.class)
public class OrgToOrgEdge extends BaseTagOrEdge{
    @GraphProperty(value = "fromId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_SRC_ID)
    private String fromId;
    @GraphProperty(value = "toId", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_EDGE_DST_ID)
    private String toId;

//    /**
//     * 遍历
//     */
//    @GraphProperty(value = "ergodic", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String ergodic;
//
//    /**
//     * 来源
//     */
//    @GraphProperty(value = "source", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String source;

    /**
     * 关系
     */
    @GraphProperty(value = "relationship", required = true, dataType = GraphDataTypeEnum.STRING)
    private String relationship;

    /**
     * 状态
     */
    @GraphProperty(value = "state", required = true, dataType = GraphDataTypeEnum.BOOL)
    private Boolean state;

    @GraphProperty(value = "startDate", required = true, dataType = GraphDataTypeEnum.STRING)
    private String startDate;

    @GraphProperty(value = "endDate", required = true, dataType = GraphDataTypeEnum.STRING)
    private String endDate;
}
