package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 组织(学习,公司,组织)信息的点表
 */
@GraphVertex(value = "OrganizationTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class OrganizationTag extends BaseTagOrEdge{
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
     * 原名
     */
    @GraphProperty(value = "name", required = true, dataType = GraphDataTypeEnum.STRING)
    private String name;

    /**
     * 中文名字
     */
    @GraphProperty(value = "nameCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String nameCn;

    /**
     * 英文名字
     */
    @GraphProperty(value = "nameEn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String nameEn;

    /**
     * 土耳其名字
     */
    @GraphProperty(value = "nameTurkey", required = true, dataType = GraphDataTypeEnum.STRING)
    private String nameTurkey;

    /**
     * (1:组织 2:学校 3:企业)
     */
    @GraphProperty(value = "organizationType", required = true, dataType = GraphDataTypeEnum.STRING)
    private String organizationType;

    /**
     * 成立时间
     */
    @GraphProperty(value = "foundingTime", required = true, dataType = GraphDataTypeEnum.STRING)
    private String foundingTime;

    /**
     * 简称
     */
    @GraphProperty(value = "abbreviation", required = true, dataType = GraphDataTypeEnum.STRING)
    private String abbreviation;

    /**
     * 组织驻地（总部）
     */
    @GraphProperty(value = "address", required = true, dataType = GraphDataTypeEnum.STRING)
    private String address;

    /**
     * 服务地区（区域性、全球）
     */
    @GraphProperty(value = "serviceArea", required = true, dataType = GraphDataTypeEnum.STRING)
    private String serviceArea;

    /**
     * 组织宗旨组织目标
     */
    @GraphProperty(value = "organizationalPurpose", required = true, dataType = GraphDataTypeEnum.STRING)
    private String organizationalPurpose;

    /**
     * 制度
     */
    @GraphProperty(value = "system", required = true, dataType = GraphDataTypeEnum.STRING)
    private String system;


    /**
     * 网站情况
     */
    @GraphProperty(value = "websiteUrl", required = true, dataType = GraphDataTypeEnum.STRING)
    private String websiteUrl;

    /**
     * logo
     */
    @GraphProperty(value = "logo", required = true, dataType = GraphDataTypeEnum.STRING)
    private String logo;

    /**
     * 简介
     */
    @GraphProperty(value = "profile", required = true, dataType = GraphDataTypeEnum.STRING)
    private String profile;

    /**
     * 简介(中文翻译)
     */
    @GraphProperty(value = "profileCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String profileCn;

    /**
     * 简称(中文翻译)
     */
    @GraphProperty(value = "abbreviationCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String abbreviationCn;

    /**
     * 组织驻地（总部）(中文翻译)
     */
    @GraphProperty(value = "addressCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String addressCn;

    /**
     * 服务地区（区域性、全球）(中文翻译)
     */
    @GraphProperty(value = "serviceAreaCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String serviceAreaCn;

    /**
     * 组织宗旨组织目标(中文翻译)
     */
    @GraphProperty(value = "organizationalPurposeCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String organizationalPurposeCn;

    /**
     * 制度(中文翻译)
     */
    @GraphProperty(value = "systemCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String systemCn;
}