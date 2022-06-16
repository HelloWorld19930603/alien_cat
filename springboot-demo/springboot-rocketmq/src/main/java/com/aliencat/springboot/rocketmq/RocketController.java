package com.aliencat.springboot.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class RocketController {
    @Autowired
    private Producer producer;
    @Value(value = "${rocketmq.topic}")
    private String topic;
    @Value(value = "${rocketmq.tag}")
    private String tag;

    @RequestMapping(value = "sendMessage")
    @ResponseBody
    public String sendMessage(@RequestParam(value = "msgContent") String msgContent) {
        Message msg = new Message(topic, tag, msgContent.getBytes());
        msg.setKeys(msgContent);
        SendResult sendResult = null;
        try {
            sendResult = producer.getProducer().send(msg);
        } catch (MQClientException e) {
            log.error(e.getMessage() + String.valueOf(sendResult));
        } catch (RemotingException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
        } catch (MQBrokerException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage(), e);
        }
        // 当消息发送失败时如何处理
        if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
            // TODO
            log.error("消息发送失败！");
            return "false";
        } else {
            log.info("消息发送成功！");
            return "true";
        }
    }

}
