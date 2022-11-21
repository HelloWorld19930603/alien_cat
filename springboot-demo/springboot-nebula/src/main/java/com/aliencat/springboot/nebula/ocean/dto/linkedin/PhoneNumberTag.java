package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 手机号码的点表
 */
@GraphVertex(value = "PhoneNumberTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class PhoneNumberTag extends BaseTagOrEdge {
    /**
     * 生成的uuid.
     */
    @GraphProperty(value = "id", required = true,
            propertyTypeEnum = GraphPropertyTypeEnum.GRAPH_VERTEX_ID)
    private String id;

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
//
    /**
     * 手机号码
     */
    @GraphProperty(value = "phoneNumber", required = true, dataType = GraphDataTypeEnum.STRING)
    private String phoneNumber;
}