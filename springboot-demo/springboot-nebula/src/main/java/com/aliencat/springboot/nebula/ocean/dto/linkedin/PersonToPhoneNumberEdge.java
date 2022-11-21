package com.aliencat.springboot.nebula.ocean.dto.linkedin;

import com.aliencat.springboot.nebula.ocean.annotation.GraphEdge;
import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 人员与手机号码之间的关系
 */
@Data
@GraphEdge(value = "PersonToPhoneNumberEdge", srcVertex = PersonInfoTag.class, dstVertex = PhoneNumberTag.class)
public class PersonToPhoneNumberEdge extends BaseTagOrEdge{
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
}
