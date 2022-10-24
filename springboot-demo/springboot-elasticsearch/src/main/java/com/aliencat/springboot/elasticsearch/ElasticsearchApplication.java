package com.aliencat.springboot.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableScheduling
public class ElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
