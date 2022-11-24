package com.aliencat.springboot.component.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengcheng
 * @Date 2022-11-24
 **/
@Data
@ConfigurationProperties(prefix = "config")
@Configuration
public class ConfigData {


    private String uploadFilePath;

}
