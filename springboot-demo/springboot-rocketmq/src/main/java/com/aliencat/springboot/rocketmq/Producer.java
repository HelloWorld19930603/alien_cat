package com.aliencat.springboot.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Producer {
    @Value(value = "${rocketmq.namesrv}")
    private String nameservAddr;
    @Value(value = "${rocketmq.group}")
    private String group;
    @Value(value = "${rocketmq.topic}")
    private String topic;
    @Value(value = "${rocketmq.tag}")
    private String tag;
    private DefaultMQProducer producer = null;

    @PostConstruct
    public void Producer() throws MQClientException {
        //生产者和消费者的nameservAddr 和group 需要保持一致 生产者可根据场景生产不同topic下的不同tag下的不同消息
        producer = new DefaultMQProducer(group);
        producer.setNamesrvAddr(nameservAddr);
        //默认不需要设置instanceName 如果有集群 则需要设置不同的instanceName 作为区分
        producer.setInstanceName("producer");
        //重试次数
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();
        log.info("生产者启动成功！");
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
