package com.aliencat.springboot.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class Consumer {
    @Value(value = "${rocketmq.namesrv}")
    private String nameservAddr;
    @Value(value = "${rocketmq.group}")
    private String group;
    @Value(value = "${rocketmq.topic}")
    private String topic;
    @Value(value = "${rocketmq.tag}")
    private String tag;
    DefaultMQPushConsumer consumer = null;

    @PostConstruct
    public void consumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(nameservAddr);
        consumer.setConsumerGroup(group);
        //默认不需要设置instanceName 如果有集群 则需要设置不同的instanceName 作为区分
        consumer.setInstanceName("consumer");
        //订阅某一个具体tag下的消息
        consumer.subscribe(topic, tag);
        //订阅某topic下所有tag的消息
        // consumer.subscribe(topic,"*");
        //第一次是从对头开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //消费者一次最多可消费多少条
        consumer.setConsumeMessageBatchMaxSize(100);
        //集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //最多重试次数
        // 消费失败默认重试16次，延迟等级为3~18。(messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h")
        consumer.setMaxReconsumeTimes(6);


        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    //获取重试次数
                    int reconsumeTimes = msg.getReconsumeTimes();
                    //获取topic
                    String topic1 = msg.getTopic();
                    //获取tag
                    String tags = msg.getTags();
                    System.out.println("消息id:" + msg.getMsgId() + "---" + new String(msg.getBody()));
                    if (reconsumeTimes > 10) {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;//下一次重试消费
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //一旦返回消费成功那么该条消息不会被消费
            }
        });
        consumer.start();
        log.info("消费者启动成功");
    }
}
