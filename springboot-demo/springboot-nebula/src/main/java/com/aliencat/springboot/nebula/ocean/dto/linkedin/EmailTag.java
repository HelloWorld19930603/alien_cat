package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 邮箱的点表
 */
@GraphVertex(value = "EmailTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class EmailTag extends BaseTagOrEdge {
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

    /**
     * 邮件地址
     */
    @GraphProperty(value = "emailAddress", required = true, dataType = GraphDataTypeEnum.STRING)
    private String emailAddress;

    /**
     * 类型
     */
    @GraphProperty(value = "type", required = true, dataType = GraphDataTypeEnum.STRING)
    private String type;

    /**
     * 类型中文翻译
     */
    @GraphProperty(value = "typeCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String typeCn;
}