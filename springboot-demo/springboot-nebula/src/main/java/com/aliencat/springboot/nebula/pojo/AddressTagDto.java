package com.aliencat.springboot.nebula.pojo;

import lombok.Data;

/**
 * 地址的点表
 */
@Data
public class AddressTagDto {
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
     * 地址
     */
    private String address;

    /**
     * 地址(中文翻译)
     */
    private String addressCn;

    /**
     * 大陆;陆地;洲;欧洲大陆
     */
    private String continent;

    /**
     * 国家
     */
    private String country;

    /**
     * 地理位置 经纬度
     */
    private String geo;

    /**
     * 地点
     */
    private String locality;

    private String metro;

    private String name;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 区域
     */
    private String region;

    /**
     * 街道地址
     */
    private String streetAddress;
}