package com.aliencat.springboot.nebula.ocean.dto.linkedin;

import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 *
 * 地址的点表
 */
@GraphVertex(value = "AddressTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class AddressTag extends BaseTagOrEdge{
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
     * 地址
     */
    @GraphProperty(value = "address", required = true, dataType = GraphDataTypeEnum.STRING)
    private String address;

    /**
     * 地址(中文翻译)
     */
    @GraphProperty(value = "addressCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String addressCn;

    @GraphProperty(value = "continent", required = true, dataType = GraphDataTypeEnum.STRING)
    private String continent;

    @GraphProperty(value = "country", required = true, dataType = GraphDataTypeEnum.STRING)
    private String country;

    @GraphProperty(value = "geo", required = true, dataType = GraphDataTypeEnum.STRING)
    private String geo;

    @GraphProperty(value = "locality", required = true, dataType = GraphDataTypeEnum.STRING)
    private String locality;

    @GraphProperty(value = "metro", required = true, dataType = GraphDataTypeEnum.STRING)
    private String metro;

    @GraphProperty(value = "name", required = true, dataType = GraphDataTypeEnum.STRING)
    private String name;

    @GraphProperty(value = "postalCode", required = true, dataType = GraphDataTypeEnum.STRING)
    private String postalCode;

    @GraphProperty(value = "region", required = true, dataType = GraphDataTypeEnum.STRING)
    private String region;

    @GraphProperty(value = "streetAddress", required = true, dataType = GraphDataTypeEnum.STRING)
    private String streetAddress;
}