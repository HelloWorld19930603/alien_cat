package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 组织(学习,公司,组织)信息的点表
 */
@Data
public class OrganizationTagDto {
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
     * 中文名字
     */
    private String nameCn;

    /**
     * 英文名字
     */
    private String nameEn;

    /**
     * 土耳其名字
     */
    private String nameTurkey;

    /**
     * (1:组织 2:学校 3:企业)
     */
    private String organizationType;

    /**
     * 成立时间
     */
    private String foundingTime;

    /**
     * 简称
     */
    private String abbreviation;

    /**
     * 组织驻地（总部）
     */
    private String address;

    /**
     * 服务地区（区域性、全球）
     */
    private String serviceArea;

    /**
     * 组织宗旨组织目标
     */
    private String organizationalPurpose;

    /**
     * 制度
     */
    private String system;


    /**
     * 网站情况
     */
    private String websiteUrl;

    /**
     * logo
     */
    private String logo;

    /**
     * 简介
     */
    private String profile;

    /**
     * 简介(中文翻译)
     */
    private String profileCn;

    /**
     * 简称(中文翻译)
     */
    private String abbreviationCn;

    /**
     * 组织驻地（总部）(中文翻译)
     */
    private String addressCn;

    /**
     * 服务地区（区域性、全球）(中文翻译)
     */
    private String serviceAreaCn;

    /**
     * 组织宗旨组织目标(中文翻译)
     */
    private String organizationalPurposeCn;

    /**
     * 制度(中文翻译)
     */
    private String systemCn;
}