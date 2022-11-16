package com.aliencat.springboot.elasticsearch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Author chengcheng
 * @Date 2022-09-16
 **/
@Component
@Slf4j
@ConfigurationProperties(prefix = "schedule")
public class ScheduledService {

    @Autowired
    ElasticsearchIndexService elasticsearchIndexService;

    @Scheduled(cron = "${schedule.start_cron_expression}")
    public void startTask() throws ParseException, InterruptedException {
        log.info("startTask");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        elasticsearchIndexService.setPause(false);
        elasticsearchIndexService.messageBatchUpdateByDateFromMysql(null, null);
    }



    @Scheduled(cron = "${schedule.end_cron_expression}")
    public void endTask() {
        log.info("endTask");
       elasticsearchIndexService.setPause(true);
    }
}
