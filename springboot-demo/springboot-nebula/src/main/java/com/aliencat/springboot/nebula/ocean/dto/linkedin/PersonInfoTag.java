package com.aliencat.springboot.nebula.ocean.dto.linkedin;


import com.aliencat.springboot.nebula.ocean.annotation.GraphProperty;
import com.aliencat.springboot.nebula.ocean.annotation.GraphVertex;
import com.aliencat.springboot.nebula.ocean.enums.GraphDataTypeEnum;
import com.aliencat.springboot.nebula.ocean.enums.GraphKeyPolicy;
import com.aliencat.springboot.nebula.ocean.enums.GraphPropertyTypeEnum;
import lombok.Data;

/**
 * 人员信息的点表
 */
@GraphVertex(value = "PersonInfoTag", keyPolicy = GraphKeyPolicy.string_key)
@Data
public class PersonInfoTag extends BaseTagOrEdge {
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
//    /**
//     * 用户系统id
//     */
//    @GraphProperty(value = "userId", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String userId;
    /**
     * 原名
     */
    @GraphProperty(value = "name", required = true, dataType = GraphDataTypeEnum.STRING)
    private String name;

    /**
     * fullName
     */
    @GraphProperty(value = "fullName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String fullName;

    /**
     * firstName
     */
    @GraphProperty(value = "firstName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String firstName;

    /**
     * middleName
     */
    @GraphProperty(value = "middleName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String middleName;

    /**
     * lastName
     */
    @GraphProperty(value = "lastName", required = true, dataType = GraphDataTypeEnum.STRING)
    private String lastName;

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
     * 性别
     */
    @GraphProperty(value = "gender", required = true, dataType = GraphDataTypeEnum.STRING)
    private String gender;

    /**
     * 年龄
     */
    @GraphProperty(value = "age", required = true, dataType = GraphDataTypeEnum.INT)
    private int age;
    /**
     * 出生年月
     */
    @GraphProperty(value = "birthDate", required = true, dataType = GraphDataTypeEnum.STRING)
    private String birthDate;

    @GraphProperty(value = "birthYear", required = true, dataType = GraphDataTypeEnum.STRING)
    private String birthYear;

    /**
     * 国家
     */
    @GraphProperty(value = "countries", required = true, dataType = GraphDataTypeEnum.STRING)
    private String countries;

    @GraphProperty(value = "languages", required = true, dataType = GraphDataTypeEnum.STRING)
    private String languages;

    /**
     * 技能
     */
    @GraphProperty(value = "skills", required = true, dataType = GraphDataTypeEnum.STRING)
    private String skills;

    /**
     * 技能(中文翻译)
     */
    @GraphProperty(value = "skillsCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String skillsCn;

    /**
     * 身份证号
     */
    @GraphProperty(value = "idNum", required = true, dataType = GraphDataTypeEnum.STRING)
    private String idNum;

    /**
     * 社保号
     */
    @GraphProperty(value = "socialSecurityNumber", required = true, dataType = GraphDataTypeEnum.STRING)
    private String socialSecurityNumber;

    /**
     * 护照号
     */
    @GraphProperty(value = "passportNumber", required = true, dataType = GraphDataTypeEnum.STRING)
    private String passportNumber;

    /**
     * 简介
     */
    @GraphProperty(value = "profile", required = true, dataType = GraphDataTypeEnum.STRING)
    private String profile;

//    /**
//     * 学历（文化程度）
//     */
//    @GraphProperty(value = "education", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String education;

//    /**
//     * 语言
//     */
//    @GraphProperty(value = "languages", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String languages;

    /**
     * 学历（文化程度）(中文翻译)
     */
    @GraphProperty(value = "educationCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String educationCn;


    /**
     * 国家(中文翻译)
     */
    @GraphProperty(value = "countryCn", required = true, dataType = GraphDataTypeEnum.STRING)
    private String countryCn;

//    /**
//     * 语言(中文翻译)
//     */
//    @GraphProperty(value = "languagesCn", required = true, dataType = GraphDataTypeEnum.STRING)
//    private String languagesCn;
}