package com.aliencat.springboot.nebula.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "nebula")
public class NebulaProperties {

    private String address;

    private String userName;

    private String passworld;

    private String spaceName;

    private String pagerankSpaceName;

    private int minConnsSize;

    private int maxConnSize;

    private int timeout;

    private int idleTime;

    private int logNum;
    //  zeroToZero :普通账号对普通账号  zeroToOne :普通账号对认证账号  oneToZero :认证账号对普通账号  oneToOne :认证账号对认证账号
    private BigDecimal zeroToZero;
    private BigDecimal zeroToOne;
    private BigDecimal oneToZero;
    private BigDecimal oneToOne;


    private BigDecimal like;
    private BigDecimal retransmission;
    private BigDecimal reply;
    private BigDecimal quote;

    //普通账号对普通账号,单方面,无关注
    private BigDecimal pToP_OneToZero;
    //普通账号对普通账号,单方面,有关注
    private BigDecimal pToP_OneToOne;
    //普通账号对普通账号,互相关注
    private BigDecimal pToP_Two;
    //普通账号对普通账号,无互相关注，有互动
    private BigDecimal pToP_Three;
    //普通账号对普通账号,互相关注，并有互动
    private BigDecimal pToP_Four;


    //普通账号对认证账号,单方面,无关注
    private BigDecimal pToR_OneToZero;
    //普通账号对认证账号,单方面,有关注
    private BigDecimal pToR_OneToOne;
    //普通账号对认证账号,互相关注
    private BigDecimal pToR_Two;
    //普通账号对认证账号,无互相关注，有互动
    private BigDecimal pToR_Three;
    //普通账号对认证账号,互相关注，并有互动
    private BigDecimal pToR_Four;


    //认证账号对普通账号,单方面,无关注
    private BigDecimal rToP_OneToZero;
    //认证账号对普通账号,单方面,有关注
    private BigDecimal rToP_OneToOne;
    //认证账号对普通账号,互相关注
    private BigDecimal rToP_Two;
    //认证账号对普通账号,无互相关注，有互动
    private BigDecimal rToP_Three;
    //认证账号对普通账号,互相关注，并有互动
    private BigDecimal rToP_Four;


    //认证账号对普通账号,单方面,无关注
    private BigDecimal rToR_OneToZero;
    //认证账号对普通账号,单方面,有关注
    private BigDecimal rToR_OneToOne;
    //认证账号对普通账号,互相关注
    private BigDecimal rToR_Two;
    //认证账号对普通账号,无互相关注，有互动
    private BigDecimal rToR_Three;
    //认证账号对普通账号,互相关注，并有互动
    private BigDecimal rToR_Four;

    // 用户亲密度系数
    private BigDecimal userIntimacyCoefficient;

    // 系统亲密度系数
    private BigDecimal systemIntimacyCoefficient;

    private String sKey;

    private String ivParameter;

    private String translateUri;

    private String nebulaEsUri;

    // 领英es大于的数字
    private String nebulaEsGte;

    // 领英es小于的数字
    private String nebulaEsLte;
}