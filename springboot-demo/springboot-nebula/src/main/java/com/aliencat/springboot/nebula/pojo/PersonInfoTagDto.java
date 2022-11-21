package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 图数据库人员信息
 */
@Data
public class PersonInfoTagDto {
    /**
     * 生成的uuid.
     */
    private String id;

    /**
     * 遍历
     */
    private String ergodic;

    /**
     * 来源
     */
    private String source;

    /**
     * 原名
     */
    private String name;

    /**
     * fullName
     */
    private String fullName;

    /**
     * firstName
     */
    private String firstName;

    /**
     * middleName
     */
    private String middleName;

    /**
     * lastName
     */
    private String lastName;

    /**
     * 中文名字
     */
    private String nameCn;

    /**
     * 英文名字
     */
    private String nameEn;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private int age;

    /**
     * 出生年月
     */
    private String birthDate;

    private String birthYear;

    /**
     * 国家
     */
    private String countries;

    private String languages;

    /**
     * 技能
     */
    private String skills;

    /**
     * 技能(中文翻译)
     */
    private String skillsCn;

    /**
     * 身份证号
     */
    private String idNum;

    /**
     * 社保号
     */
    private String socialSecurityNumber;

    /**
     * 护照号
     */
    private String passportNumber;

    /**
     * 简介
     */
    private String profile;

    /**
     * 学历（文化程度）(中文翻译)
     */
    private String educationCn;


    /**
     * 国家(中文翻译)
     */
    private String countryCn;
}
