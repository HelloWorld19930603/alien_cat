package com.aliencat.springboot.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ThreadPoolConfig
 * @Description 暂无
 * @Author wys
 * @Date 2020/4/22
 * @Version 1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = ThreadPoolConfig.PREFIX)
public class ThreadPoolConfig {
    public static final String PREFIX = "thread-pool-factory";
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;
    private Long fileSize;
}
